package org.firstinspires.ftc.teamcode.hardware.drivetrain;

public interface DrivetrainIO {

    void updateInputs(DrivetrainIOInputs inputs);

    void setMotorPowers(double frontLeftPower, double frontRightPower, double backLeftPower, double backRightPower);

    void resetYaw();

    default void stop() {
        setMotorPowers(0.0, 0.0, 0.0, 0.0);
    }
}
