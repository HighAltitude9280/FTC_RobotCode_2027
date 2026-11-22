package org.firstinspires.ftc.teamcode.subsystems.feeder;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.feeder.FeederIO;

public class Feeder {
    private final FeederIO io;

    public Feeder(FeederIO io) {
        this.io = io;
    }

    public void feedIn() {
        io.setFeederMotorPower(LowAltitudeConstants.Feeder.IN_POWER);
    }

    public void feedOut() {
        io.setFeederMotorPower(LowAltitudeConstants.Feeder.OUT_POWER);
    }

    public void stop() {
        io.stop();
    }
}
