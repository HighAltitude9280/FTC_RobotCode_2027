package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.DrivetrainIO;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.DrivetrainIOInputs;

public class Drivetrain {

    private final DrivetrainIO io;
    private final DrivetrainIOInputs inputs = new DrivetrainIOInputs();

    public Drivetrain(DrivetrainIO io) {
        this.io = io;
    }

    public void driveRobotCentric(
            double forward,
            double strafe,
            double rotation
    ) {
        driveMecanum(forward, strafe, rotation);
    }

    public void driveFieldCentric(
            double forward,
            double strafe,
            double rotation
    ) {

        double heading = inputs.headingRadians;

        // Convierte el vector del campo al sistema de coordenadas del robot.
        double robotStrafe =
                strafe * Math.cos(-heading)
                        - forward * Math.sin(-heading);

        double robotForward =
                strafe * Math.sin(-heading)
                        + forward * Math.cos(-heading);

        driveMecanum(
                robotForward,
                robotStrafe,
                rotation
        );
    }

    private void driveMecanum(
            double forward,
            double strafe,
            double rotation
    ) {

        double frontLeftPower =
                forward + strafe + rotation;

        double frontRightPower =
                forward - strafe - rotation;

        double backLeftPower =
                forward - strafe + rotation;

        double backRightPower =
                forward + strafe - rotation;

        double maxMagnitude =
                Math.max(
                        1.0,
                        Math.max(
                                Math.max(
                                        Math.abs(frontLeftPower),
                                        Math.abs(frontRightPower)
                                ),
                                Math.max(
                                        Math.abs(backLeftPower),
                                        Math.abs(backRightPower)
                                )
                        )
                );

        frontLeftPower /= maxMagnitude;
        frontRightPower /= maxMagnitude;
        backLeftPower /= maxMagnitude;
        backRightPower /= maxMagnitude;

        io.setMotorPowers(
                frontLeftPower * LowAltitudeConstants.Drivetrain.CHASSIS_SPEED_MULTIPLIER,
                frontRightPower * LowAltitudeConstants.Drivetrain.CHASSIS_SPEED_MULTIPLIER,
                backLeftPower * LowAltitudeConstants.Drivetrain.CHASSIS_SPEED_MULTIPLIER,
                backRightPower * LowAltitudeConstants.Drivetrain.CHASSIS_SPEED_MULTIPLIER
        );
    }

    public void stop() {
        io.stop();
    }

    public void periodic() {
        io.updateInputs(inputs);
    }

    public void resetYaw() {
        io.resetYaw();
    }

    public double getHeadingRadians() {
        return inputs.headingRadians;
    }

    public double getHeadingDegrees() {
        return inputs.headingDegrees;
    }
}
