package org.firstinspires.ftc.teamcode.hardware.shooter;

public interface ShooterIO {
    void updateInputs(ShooterIOInputs inputs);

    void setTargetRPM(double targetRPM);

    void setVelocityPIDF(double p, double i, double d, double f);

    default void stop() {
        setTargetRPM(0.0);
    }
}
