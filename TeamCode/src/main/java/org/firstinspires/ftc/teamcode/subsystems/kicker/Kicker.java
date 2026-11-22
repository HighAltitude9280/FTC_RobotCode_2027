package org.firstinspires.ftc.teamcode.subsystems.kicker;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.kicker.KickerIO;

public class Kicker {
    private final KickerIO io;

    public Kicker(KickerIO io) {
        this.io = io;
    }

    public void kickIn() {
        io.setKickerServoPower(LowAltitudeConstants.Kicker.IN_POWER);
    }

    public void kickOut() {
        io.setKickerServoPower(LowAltitudeConstants.Kicker.OUT_POWER);
    }

    public void stop() {
        io.stop();
    }
}
