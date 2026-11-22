package org.firstinspires.ftc.teamcode.hardware.intake;

public interface IntakeIO {
    void setIntakeMotorPower(double intakePower);

    default void stop() {
        setIntakeMotorPower(0.0);
    }
}
