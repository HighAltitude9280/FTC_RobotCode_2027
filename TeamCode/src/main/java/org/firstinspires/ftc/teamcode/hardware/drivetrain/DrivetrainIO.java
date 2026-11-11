package org.firstinspires.ftc.teamcode.hardware.drivetrain;

public interface DrivetrainIO {

    void updateInputs(DrivetrainIOInputs inputs);

    void setMotorPowers(double frontLeft, double frontRight, double backLeft, double backRight);

    void resetYaw();

    default void stop() {
        setMotorPowers(0.0, 0.0, 0.0, 0.0);
    }
}
