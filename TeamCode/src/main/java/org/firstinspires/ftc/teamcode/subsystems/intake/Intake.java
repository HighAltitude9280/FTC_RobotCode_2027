package org.firstinspires.ftc.teamcode.subsystems.intake;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.rollers.RollerIO;

public class Intake {
    private final RollerIO io;

    public Intake(RollerIO io) {
        this.io = io;
    }

    public void intakeIn() {
        io.setRollerMotorPower(LowAltitudeConstants.Intake.IN_POWER);
    }

    public void intakeOut() {
        io.setRollerMotorPower(LowAltitudeConstants.Intake.OUT_POWER);
    }

    public void stop() {
        io.stop();
    }
}
