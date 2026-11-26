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
import com.pedropathing.revhub.localizers.ThreeWheelIMUConfig;
import com.pedropathing.revhub.localizers.ThreeWheelIMULocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new ThreeWheelIMULocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static ThreeWheelIMUConfig localizerConfig = new ThreeWheelIMUConfig(c -> {
        c.leftEncoderName.set("frontLeft");
        c.rightEncoderName.set("feeder");
        c.strafeEncoderName.set("backRight");
        c.imuName.set("imu");
        c.imu.set(new RevHubIMU(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        )));
        c.leftPodY.set(8.502920191921781);
        c.rightPodY.set(-6.4210212968746525);
        c.strafePodX.set(-3.8913568646544787);
        c.forwardTicksToInches.set(0.0020324668347410594);
        c.strafeTicksToInches.set(0.001967030271125227);
        c.turnTicksToRadians.set(0.002185778319058593);
        c.leftEncoderDirection.set(Encoder.FORWARD);
        c.rightEncoderDirection.set(Encoder.REVERSE);
        c.strafeEncoderDirection.set(Encoder.REVERSE);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.13521363014480117);
                Controller secondaryTranslationalForward = Controller.proportional(0.04995782336376546);
                Controller primaryTranslationalLateral = Controller.proportional(1.7523478801987127);
                Controller secondaryTranslationalLateral = Controller.proportional(0.6474457181357028);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.0026870549036131846));
                c.brake.set(Controller.proportionalFeedforward(0.002283996668071207));

                c.headingFeedback.set(Controller.proportional(9.150812503198166));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.09629625293254855, 0.002225889600134286));

                c.linearBrakeCoefficients.set(Matrix.diag(0.04288424981304113, 0.07497794040995272));
                c.quadraticBrakeCoefficients.set(Matrix.diag(1.5885855565735292E-4, 1.9452521758816905E-4));

                c.maxAchievableForwardVelocity.set(337.12942050168596);
                c.maxAchievableStrafeVelocity.set(124.63708575638393);
                c.naturalForwardDeceleration.set(99.16811790233561);
                c.naturalStrafeDeceleration.set(281.42743564990985);
            }
    );
}
