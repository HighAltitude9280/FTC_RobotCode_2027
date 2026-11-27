package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.Encoder;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.revhub.localizers.RevHubIMU;
import com.pedropathing.revhub.localizers.ThreeWheelConfig;
import com.pedropathing.revhub.localizers.ThreeWheelIMUConfig;
import com.pedropathing.revhub.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new ThreeWheelLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("lf");
        c.frontRightName.set("rf");
        c.backLeftName.set("lr");
        c.backRightName.set("rr");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static ThreeWheelConfig localizerConfig = new ThreeWheelConfig(c -> {
        c.leftEncoderName.set("lf");
        c.rightEncoderName.set("feeder");
        c.strafeEncoderName.set("rr");
        c.leftPodY.set(6.491639262733671);
        c.rightPodY.set(-7.478694477294898);
        c.strafePodX.set(-6.6201980650766474E-15);
        c.forwardTicksToInches.set(0.0020005970391955225);
        c.strafeTicksToInches.set(0.001993603854300785);
        c.turnTicksToRadians.set(0.004668308019557671);
        c.leftEncoderDirection.set(Encoder.FORWARD);
        c.rightEncoderDirection.set(Encoder.REVERSE);
        c.strafeEncoderDirection.set(Encoder.REVERSE);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.44222371695093343);
                Controller secondaryTranslationalForward = Controller.proportional(0.16338984697802655);
                Controller primaryTranslationalLateral = Controller.proportional(0.45220737542268635);
                Controller secondaryTranslationalLateral = Controller.proportional(0.20707854201507177);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.012106832418647831));
                c.brake.set(Controller.proportionalFeedforward(0.010290807555850656));

                c.headingFeedback.set(Controller.proportional(2.816210429942214));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.10704826758082685, 5.666789716817385E-4));

                c.linearBrakeCoefficients.set(Matrix.diag(0.09966852906966608, 0.04806025795528229));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0020285905628153788, 9.297545216111125E-4));

                c.maxAchievableForwardVelocity.set(84.07020398804421);
                c.maxAchievableStrafeVelocity.set(130.27697096172543);
                c.naturalForwardDeceleration.set(36.35168988949471);
                c.naturalStrafeDeceleration.set(173.91567186087144);
            }
    );
}
