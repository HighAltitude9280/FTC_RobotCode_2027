package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public final class EncoderUtil {

    private EncoderUtil() {}

    public static double shooterRPMToTicksPerSecond(double rpm) {
        return rpm / 60.0
                * LowAltitudeConstants.SHOOTER.SHOOTER_TICKS_PER_REV;
    }

    public static double shooterTicksPerSecondToRPM(double ticksPerSecond) {
        return ticksPerSecond
                / LowAltitudeConstants.SHOOTER.SHOOTER_TICKS_PER_REV
                * 60.0;
    }
}