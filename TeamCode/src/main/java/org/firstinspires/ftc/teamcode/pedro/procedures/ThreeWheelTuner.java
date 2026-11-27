package org.firstinspires.ftc.teamcode.pedro.procedures;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.Inputs;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.TuningOpMode;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.List;

public class ThreeWheelTuner extends Procedure {

    private static String leftEncoderName = "lf";
    private static String rightEncoderName = "feeder";
    private static String strafeEncoderName = "rr";

    public ThreeWheelTuner() {
        super("Three Wheel Tuner", "Tune three odometry pods");
    }

    // CAMBIO: para que ThreeWheelResolution pueda leer el encoder de strafe directamente
    static String strafeName() {
        return strafeEncoderName;
    }

    @Override
    public void run() throws InterruptedException {
        Inputs setup =
                inputs(
                        "Encoder Setup",
                        "Set the motor ports that the three odometry encoders are plugged into.");
        Inputs.Field<String> leftEncoder = setup.s("Left Encoder Motor Name").withDefault("lf");
        Inputs.Field<String> rightEncoder = setup.s("Right Encoder Motor Name").withDefault("feeder");
        Inputs.Field<String> strafeEncoder = setup.s("Strafe Encoder Motor Name").withDefault("rr");
        awaitInputs(setup);

        // CAMBIO: nombres forzados. Se ignora lo que llegue de la pantalla
        // (Panels puede recordar valores viejos como "lr").
        leftEncoderName = "lf";
        rightEncoderName = "feeder";
        strafeEncoderName = "rr";

        Inputs resolution =
                inputs(
                        "Encoder Resolution Identification",
                        "Set a positive push distance in inches. Keep the robot straight during"
                                + " each push.");
        Inputs.Field<Double> distance = resolution.d("Distance").withDefault(48.0);
        awaitInputs(resolution);
        if (!(distance.get() > 0.0)) {
            abort("Enter a positive distance in inches.");
            return;
        }

        List<Double> left = measure("Left", distance.get());
        if (left == null) {
            return;
        }
        List<Double> right = measure("Right", distance.get());
        if (right == null) {
            return;
        }
        List<Double> strafe = measure("Strafe", distance.get());
        if (strafe == null) {
            return;
        }

        double leftTicksPerInch = left.get(0);
        double rightTicksPerInch = right.get(0);
        double strafeTicksPerInch = strafe.get(0);
        double forwardTicksPerInch = 2.0 / (1.0 / leftTicksPerInch + 1.0 / rightTicksPerInch);

        double forward = 1.0 / forwardTicksPerInch;
        double lateral = 1.0 / strafeTicksPerInch;

        List<Double> leftOffsets =
                runOpMode(
                        new ThreeWheelOffsets(
                                true, forward, lateral, left.get(1), right.get(1), strafe.get(1)));
        if (leftOffsets == null) {
            abort(
                    "Left stage ended without parallel pod travel. Rotate 180 degrees CCW, then"
                            + " press Stop.");
            return;
        }
        List<Double> rightOffsets =
                runOpMode(
                        new ThreeWheelOffsets(
                                false, forward, lateral, left.get(1), right.get(1), strafe.get(1)));

        if (rightOffsets == null) {
            abort(
                    "Right stage ended without parallel pod travel. Rotate 180 degrees CCW, then"
                            + " press Stop.");
            return;
        }
        ThreeWheelConfig config =
                config(true, forward, lateral, left.get(1), right.get(1), strafe.get(1));
        config.leftPodY.set(leftOffsets.get(0));
        config.rightPodY.set(rightOffsets.get(0));
        config.turnTicksToRadians.set(forward);

        Double turn = runOpMode(new ThreeWheelTurn(config));
        if (turn == null) {
            abort(
                    "Turn stage ended without positive rotation. Rotate 360 degrees CCW, then press"
                            + " Stop.");
            return;
        }
        double strafeX = (leftOffsets.get(1) + rightOffsets.get(1)) / 2.0 * turn / lateral;

        result("leftEncoderName", leftEncoderName);
        result("rightEncoderName", rightEncoderName);
        result("strafeEncoderName", strafeEncoderName);
        result("leftPodY", leftOffsets.get(0));
        result("rightPodY", rightOffsets.get(0));
        result("strafePodX", strafeX);
        result("leftTicksPerInch", leftTicksPerInch);
        result("rightTicksPerInch", rightTicksPerInch);
        result("forwardTicksPerInch", forwardTicksPerInch);
        result("strafeTicksPerInch", strafeTicksPerInch);
        result("forwardTicksToInches", forward);
        result("strafeTicksToInches", lateral);
        result("turnTicksToRadians", turn);
        result("leftEncoderDirection", direction(left.get(1)));
        result("rightEncoderDirection", direction(right.get(1)));
        result("strafeEncoderDirection", direction(strafe.get(1)));

        code(
                Language.JAVA,
                "public static ThreeWheelConfig localizerConfig = new ThreeWheelConfig(c -> {\n"
                        + "    c.leftEncoderName.set(\""
                        + leftEncoderName
                        + "\");\n"
                        + "    c.rightEncoderName.set(\""
                        + rightEncoderName
                        + "\");\n"
                        + "    c.strafeEncoderName.set(\""
                        + strafeEncoderName
                        + "\");\n"
                        + "    c.leftPodY.set("
                        + leftOffsets.get(0)
                        + ");\n"
                        + "    c.rightPodY.set("
                        + rightOffsets.get(0)
                        + ");\n"
                        + "    c.strafePodX.set("
                        + strafeX
                        + ");\n"
                        + "    c.forwardTicksToInches.set("
                        + forward
                        + ");\n"
                        + "    c.strafeTicksToInches.set("
                        + lateral
                        + ");\n"
                        + "    c.turnTicksToRadians.set("
                        + turn
                        + ");\n"
                        + "    c.leftEncoderDirection.set("
                        + direction(left.get(1))
                        + ");\n"
                        + "    c.rightEncoderDirection.set("
                        + direction(right.get(1))
                        + ");\n"
                        + "    c.strafeEncoderDirection.set("
                        + direction(strafe.get(1))
                        + ");\n"
                        + "});");
    }

    private List<Double> measure(String pod, double distance) throws InterruptedException {
        List<Double> measured = runOpMode(new ThreeWheelResolution(pod, distance));
        if (measured == null) {
            // CAMBIO: el mensaje de error muestra qué nombres de encoder se usaron
            abort(
                    pod
                            + " stage ended without a nonzero measurement. Encoders usados: left="
                            + leftEncoderName
                            + ", right="
                            + rightEncoderName
                            + ", strafe="
                            + strafeEncoderName
                            + " | Lectura: "
                            + ThreeWheelResolution.lastDebug);
            return null;
        }
        return measured;
    }

    static String direction(double direction) {
        return direction == Encoder.REVERSE ? "Encoder.REVERSE" : "Encoder.FORWARD";
    }

    static ThreeWheelConfig config(
            boolean left,
            double forward,
            double strafe,
            double leftDirection,
            double rightDirection,
            double strafeDirection) {
        return new ThreeWheelConfig(
                c -> {
                    c.leftEncoderName.set(leftEncoderName);
                    c.rightEncoderName.set(rightEncoderName);
                    c.strafeEncoderName.set(strafeEncoderName);
                    c.leftPodY.set(left ? 0.0 : 1.0);
                    c.rightPodY.set(left ? -1.0 : 0.0);
                    c.strafePodX.set(0.0);
                    c.forwardTicksToInches.set(forward);
                    c.strafeTicksToInches.set(strafe);
                    c.turnTicksToRadians.set(0.0);
                    c.leftEncoderDirection.set(leftDirection);
                    c.rightEncoderDirection.set(rightDirection);
                    c.strafeEncoderDirection.set(strafeDirection);
                });
    }

    static ThreeWheelLocalizer localizer(HardwareMap map, ThreeWheelConfig config) {
        for (LynxModule hub : map.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        for (String name : new String[] {"lf", "lr", "rf", "rr"}) {
            DcMotorEx motor = map.get(DcMotorEx.class, name);
            motor.setPower(0);
            motor.setDirection(
                    name.equals("lf") || name.equals("lr")
                            ? DcMotorSimple.Direction.REVERSE
                            : DcMotorSimple.Direction.FORWARD);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        return new ThreeWheelLocalizer(map, config);
    }
}

class ThreeWheelResolution extends TuningOpMode<List<Double>> {

    static String lastDebug = "";  // CAMBIO

    String pod;
    double distance;

    ThreeWheelResolution(String pod, double distance) {
        super(
                pod + " Encoder Resolution and Direction",
                "After Start, push the robot "
                        + (pod.equals("Strafe") ? "left " : "forward ")
                        + distance
                        + " inches exactly without turning. Stop moving, press Stop to save this"
                        + " measurement.",
                true);
        this.pod = pod;
        this.distance = distance;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        ThreeWheelConfig config =
                ThreeWheelTuner.config(
                        !pod.equals("Right"),
                        1.0,
                        1.0,
                        Encoder.FORWARD,
                        Encoder.FORWARD,
                        Encoder.FORWARD);
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        Pose position = null;

        // CAMBIO: también leemos el encoder de strafe directo (ticks crudos)
        DcMotorEx strafeMotor = hardwareMap.get(DcMotorEx.class, ThreeWheelTuner.strafeName());
        int strafeStart = strafeMotor.getCurrentPosition();
        int strafeTicks = 0;
        int loops = 0;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            position = localizer.pose();
            strafeTicks = strafeMotor.getCurrentPosition() - strafeStart;
            loops++;
        }

        // CAMBIO: guardamos lo que se leyó para mostrarlo si hay error
        lastDebug = "loops=" + loops
                + ", strafeTicks=" + strafeTicks
                + (position == null ? ", pose=null" : ", x=" + position.x() + ", y=" + position.y());

        double movement;
        if (pod.equals("Strafe")) {
            // CAMBIO: si el localizer da y = 0, usamos los ticks crudos del encoder de strafe
            movement = (position != null && position.y() != 0.0) ? position.y() : strafeTicks;
        } else {
            if (position == null) {
                return null;
            }
            movement = position.x();
        }
        if (movement == 0.0) {
            return null;
        }
        return List.of(
                Math.abs(movement / distance), movement < 0 ? Encoder.REVERSE : Encoder.FORWARD);
    }
}

class ThreeWheelOffsets extends TuningOpMode<List<Double>> {

    boolean left;
    double forward;
    double strafe;
    double leftDirection;
    double rightDirection;
    double strafeDirection;

    ThreeWheelOffsets(
            boolean left,
            double forward,
            double strafe,
            double leftDirection,
            double rightDirection,
            double strafeDirection) {
        super(
                (left ? "Left" : "Right") + " Pod Offset Identification",
                "After Start, rotate exactly 180 degrees counterclockwise about the robot center."
                        + " Keep that center fixed. Stop moving, press Stop to save this measurement.",
                true);
        this.left = left;
        this.forward = forward;
        this.strafe = strafe;
        this.leftDirection = leftDirection;
        this.rightDirection = rightDirection;
        this.strafeDirection = strafeDirection;
    }

    @Override
    protected List<Double> runTuningOpMode() {
        ThreeWheelConfig config =
                ThreeWheelTuner.config(
                        left, forward, strafe, leftDirection, rightDirection, strafeDirection);
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        localizer.update();
        Pose position = null;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            position = localizer.pose();
        }

        if (position == null) {
            return null;
        }
        return List.of(-position.x() / Math.PI, position.y() / Math.PI);
    }
}

class ThreeWheelTurn extends TuningOpMode<Double> {

    ThreeWheelConfig config;

    ThreeWheelTurn(ThreeWheelConfig config) {
        super(
                "Turn Multiplier Identification",
                "After Start, rotate exactly 360 degrees counterclockwise. "
                        + "Stop moving, press Stop to save this measurement.",
                true);
        this.config = config;
    }

    @Override
    protected Double runTuningOpMode() {
        ThreeWheelLocalizer localizer = ThreeWheelTuner.localizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        localizer.update();
        double startHeading = localizer.getTotalHeading();
        Double heading = null;

        waitForStart();
        while (!isStopRequested()) {
            localizer.update();
            heading = localizer.getTotalHeading();
        }

        if (heading == null || heading <= startHeading) {
            return null;
        }
        return config.turnTicksToRadians.get() * (2.0 * Math.PI) / (heading - startHeading);
    }
}