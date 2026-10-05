package org.firstinspires.ftc.teamcode.drive.io;

import org.firstinspires.ftc.teamcode.drive.WheelPowers;

/** Acceso futuro a HardwareMap y motores FTC. Sin hardware ni salidas por ahora. */
public final class MecanumDriveIOReal implements MecanumDriveIO {
    @Override
    public void setWheelPowers(WheelPowers powers) {
        throw new UnsupportedOperationException("Configure real motors before using this IO");
    }
}
