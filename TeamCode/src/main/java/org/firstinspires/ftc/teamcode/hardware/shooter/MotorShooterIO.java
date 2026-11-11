package org.firstinspires.ftc.teamcode.hardware.shooter;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.util.EncoderUtil;

public class MotorShooterIO implements ShooterIO {
    private final DcMotorEx shooterMotor;
    private double lastP = Double.NaN;
    private double lastI = Double.NaN;
    private double lastD = Double.NaN;
    private double lastF = Double.NaN;

    public MotorShooterIO(HardwareMap hardwareMap, String shooterMotorName) {
        shooterMotor = hardwareMap.get(DcMotorEx.class, shooterMotorName);

        shooterMotor.setDirection(LowAltitudeConstants.Shooter.DIRECTION);
        shooterMotor.setZeroPowerBehavior(LowAltitudeConstants.Shooter.ZERO_POWER_BEHAVIOR);
        shooterMotor.setMode(LowAltitudeConstants.Shooter.RUN_MODE);

        shooterMotor.setVelocityPIDFCoefficients(
                LowAltitudeConstants.Shooter.kP,
                LowAltitudeConstants.Shooter.kI,
                LowAltitudeConstants.Shooter.kD,
                LowAltitudeConstants.Shooter.kF);
    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        double ticksPerSecond = shooterMotor.getVelocity();

        inputs.currentRPM = EncoderUtil.shooterTicksPerSecondToRPM(ticksPerSecond);
    }

    @Override
    public void setTargetRPM(double targetRPM) {
        double ticksPerSecond = EncoderUtil.shooterRPMToTicksPerSecond(targetRPM);

        shooterMotor.setVelocity(ticksPerSecond);
    }

    @Override
    public void setVelocityPIDF(double p, double i, double d, double f) {
        if (p != lastP || i != lastI || d != lastD || f != lastF) {
            shooterMotor.setVelocityPIDFCoefficients(p, i, d, f);
        }
        lastP = p;
        lastI = i;
        lastD = d;
        lastF = f;
    }
}
