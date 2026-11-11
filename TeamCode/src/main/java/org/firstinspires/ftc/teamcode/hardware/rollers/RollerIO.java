package org.firstinspires.ftc.teamcode.hardware.rollers;

public interface RollerIO {
    void setRollerMotorPower(double rollerPower);

    default void stop() {
        setRollerMotorPower(0.0);
    }
}
