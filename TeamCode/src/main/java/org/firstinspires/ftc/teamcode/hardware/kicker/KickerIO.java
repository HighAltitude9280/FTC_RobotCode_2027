package org.firstinspires.ftc.teamcode.hardware.kicker;

public interface KickerIO {
    void setKickerServoPower(double kickerPower);

    default void stop() {setKickerServoPower(0);}
}
