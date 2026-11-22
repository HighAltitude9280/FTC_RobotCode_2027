package org.firstinspires.ftc.teamcode.hardware.feeder;

public interface FeederIO {
    void setFeederMotorPower(double feederPower);

    default void stop() {
        setFeederMotorPower(0.0);
    }
}
