package org.firstinspires.ftc.teamcode.hardware.shooter;

import org.firstinspires.ftc.teamcode.hardware.drivetrain.DriveTrainIOInputs;

public interface ShooterIO {
    void updateInputs(ShooterIOInputs inputs);
    void setShooterTargetMotorRPM(double shooterTargetRPM);

    void setVelocityPIDF(double p, double i, double d, double f);

    default void stop() {
        setShooterTargetMotorRPM(0.0);
    }
}
