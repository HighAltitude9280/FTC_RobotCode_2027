package org.firstinspires.ftc.teamcode.drive.io;

import org.firstinspires.ftc.teamcode.drive.WheelPowers;

/** Salidas logicas: positivo significa que cada rueda impulsa hacia adelante. */
public interface MecanumDriveIO {
    void setWheelPowers(WheelPowers powers);
}
