package org.firstinspires.ftc.teamcode.subsystems.intake;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.intake.IntakeIO;

public class Intake {
    private final IntakeIO io;

    public Intake(IntakeIO io) {
        this.io = io;
    }

    public void intakeIn() {
        io.setIntakeMotorPower(LowAltitudeConstants.Intake.IN_POWER);
    }

    public void intakeOut() {
        io.setIntakeMotorPower(LowAltitudeConstants.Intake.OUT_POWER);
    }

    public void stop() {
        io.stop();
    }
}
