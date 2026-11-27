package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.math.Pose;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Strafe Stage Test", group = "Test")
public class StrafeStageTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        // Igual que ThreeWheelTuner.localizer()
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
        for (String name : new String[] {"lf", "lr", "rf", "rr"}) {
            DcMotorEx motor = hardwareMap.get(DcMotorEx.class, name);
            motor.setPower(0);
            motor.setDirection(name.equals("lf") || name.equals("lr")
                    ? DcMotorSimple.Direction.REVERSE
                    : DcMotorSimple.Direction.FORWARD);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        // Igual que ThreeWheelTuner.config(true, 1.0, 1.0, FORWARD, FORWARD, FORWARD)
        ThreeWheelConfig config = new ThreeWheelConfig(c -> {
            c.leftEncoderName.set("lf");
            c.rightEncoderName.set("feeder");
            c.strafeEncoderName.set("rr");
            c.leftPodY.set(0.0);
            c.rightPodY.set(-1.0);
            c.strafePodX.set(0.0);
            c.forwardTicksToInches.set(1.0);
            c.strafeTicksToInches.set(1.0);
            c.turnTicksToRadians.set(0.0);
            c.leftEncoderDirection.set(Encoder.FORWARD);
            c.rightEncoderDirection.set(Encoder.FORWARD);
            c.strafeEncoderDirection.set(Encoder.FORWARD);
        });

        ThreeWheelLocalizer localizer = new ThreeWheelLocalizer(hardwareMap, config);
        localizer.setPose(new Pose(0, 0));
        DcMotorEx rr = hardwareMap.get(DcMotorEx.class, "rr");
        int rrStart = rr.getCurrentPosition();

        waitForStart();
        while (opModeIsActive()) {
            localizer.update();
            Pose p = localizer.pose();
            telemetry.addData("rr ticks (crudos)", rr.getCurrentPosition() - rrStart);
            telemetry.addData("localizer x", p.x());
            telemetry.addData("localizer y", p.y());
            telemetry.addData("localizer heading", p.heading());
            telemetry.update();
        }
    }
}