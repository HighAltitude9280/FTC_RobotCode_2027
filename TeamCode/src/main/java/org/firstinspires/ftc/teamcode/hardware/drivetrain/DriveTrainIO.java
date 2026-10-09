package org.firstinspires.ftc.teamcode.hardware.drivetrain;

public interface DriveTrainIO {

    void updateInputs(DriveTrainIOInputs inputs);

    void setMotorPowers(
            double frontLeft,
            double frontRight,
            double backLeft,
            double backRight
    );

    default void stop() {
        setMotorPowers(0.0, 0.0, 0.0, 0.0);
    }
}