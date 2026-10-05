package org.firstinspires.ftc.teamcode.drive;

/** Potencias normalizadas, independientes de la inversion electrica de motores. */
public final class WheelPowers {
    public static final WheelPowers ZERO = new WheelPowers(0, 0, 0, 0);
    public final double frontLeft, frontRight, rearLeft, rearRight;

    public WheelPowers(double frontLeft, double frontRight, double rearLeft, double rearRight) {
        this.frontLeft = valid(frontLeft);
        this.frontRight = valid(frontRight);
        this.rearLeft = valid(rearLeft);
        this.rearRight = valid(rearRight);
    }

    private static double valid(double value) {
        if (!Double.isFinite(value) || Math.abs(value) > 1) {
            throw new IllegalArgumentException("Wheel power must be finite and within [-1, 1]");
        }
        return value;
    }
}
