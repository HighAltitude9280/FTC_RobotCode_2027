package org.firstinspires.ftc.teamcode.drive;

import java.util.Objects;
import org.firstinspires.ftc.teamcode.drive.io.MecanumDriveIO;

/** Mezcla relativa al robot: +avance, +izquierda, +giro antihorario. Sin Android. */
public final class MecanumDrive {
    private final MecanumDriveIO io;
    private final double maxPower;
    private final double deadband;
    private WheelPowers powers = WheelPowers.ZERO;

    public MecanumDrive(MecanumDriveIO io, double maxPower, double deadband) {
        this.io = Objects.requireNonNull(io);
        if (!Double.isFinite(maxPower) || maxPower <= 0 || maxPower > 1
                || !Double.isFinite(deadband) || deadband < 0 || deadband >= 1) {
            throw new IllegalArgumentException("Invalid power limit or deadband");
        }
        this.maxPower = maxPower;
        this.deadband = deadband;
    }

    public void drive(double forward, double left, double counterClockwise, boolean enabled) {
        if (!enabled || !Double.isFinite(forward) || !Double.isFinite(left)
                || !Double.isFinite(counterClockwise)) {
            stop();
            return;
        }
        double f = axis(forward), l = axis(left), r = axis(counterClockwise);
        double fl = f - l - r, fr = f + l + r;
        double rl = f + l - r, rr = f - l + r;
        double scale = maxPower / Math.max(1, Math.max(Math.max(Math.abs(fl), Math.abs(fr)),
                Math.max(Math.abs(rl), Math.abs(rr))));
        powers = new WheelPowers(fl * scale, fr * scale, rl * scale, rr * scale);
        io.setWheelPowers(powers);
    }

    private double axis(double value) {
        return Math.abs(value) <= deadband ? 0 : Math.max(-1, Math.min(1, value));
    }

    public void stop() {
        powers = WheelPowers.ZERO;
        io.setWheelPowers(powers);
    }

    public WheelPowers getPowers() { return powers; }
}
