package org.firstinspires.ftc.teamcode.subsystems.feeder;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.rollers.RollerIO;

public class Feeder {
    private final RollerIO io;

    public Feeder(RollerIO io) { this.io = io;}

    public void feedIn() {io.setRollerMotorPower(LowAltitudeConstants.Feeder.IN_POWER);}

    public void feedOut() {io.setRollerMotorPower(LowAltitudeConstants.Feeder.OUT_POWER);}

    public void stop() {io.stop();}
}
