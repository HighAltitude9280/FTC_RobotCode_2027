package org.firstinspires.ftc.teamcode.subsystems.shooter;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.rollers.RollerIO;
import org.firstinspires.ftc.teamcode.hardware.shooter.ShooterIO;
import org.firstinspires.ftc.teamcode.hardware.shooter.ShooterIOInputs;

public class Shooter {
    private final ShooterIO io;
    private final ShooterIOInputs inputs = new ShooterIOInputs();

    private double targetRPM = 0.0;

    public Shooter(ShooterIO io) {
        this.io = io;
    }

    public void setTargetRPM(double targetRPM) {
        this.targetRPM = targetRPM;
        io.setShooterTargetMotorRPM(targetRPM);
    }

    public void stop() {
        targetRPM = 0.0;
        io.stop();
    }

    public void periodic() {
        io.setVelocityPIDF(
                LowAltitudeConstants.SHOOTER.SHOOTER_P,
                LowAltitudeConstants.SHOOTER.SHOOTER_I,
                LowAltitudeConstants.SHOOTER.SHOOTER_D,
                LowAltitudeConstants.SHOOTER.SHOOTER_F
        );

        io.updateInputs(inputs);
    }

    public boolean isAtTargetRPM() {
        return Math.abs(inputs.currentRPM - targetRPM)
                <= LowAltitudeConstants.SHOOTER.RPM_TOLERANCE;
    }

    public double getCurrentRPM() {
        return inputs.currentRPM;
    }

    public double getTargetRPM() {
        return targetRPM;
    }

    public double getRPMError() {
        return targetRPM - inputs.currentRPM;
    }
}
