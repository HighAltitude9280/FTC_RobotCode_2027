package org.firstinspires.ftc.teamcode.drive.io;

import java.util.Objects;
import org.firstinspires.ftc.teamcode.drive.WheelPowers;

/** Cinematica ideal, NO odometria: potencia -> velocidad de rueda -> pose verdadera simulada.
 * No modela aceleracion, deslizamiento, bateria, colisiones ni friccion.
 */
public final class MecanumDriveIOSim implements MecanumDriveIO {
    private final double maxWheelSpeedMetersPerSecond;
    private final double rotationRadiusMeters;
    private WheelPowers powers = WheelPowers.ZERO;
    private double x, y, heading;

    public MecanumDriveIOSim(double maxWheelSpeedMetersPerSecond,
                             double wheelbaseMeters, double trackWidthMeters) {
        positive(maxWheelSpeedMetersPerSecond);
        positive(wheelbaseMeters);
        positive(trackWidthMeters);
        this.maxWheelSpeedMetersPerSecond = maxWheelSpeedMetersPerSecond;
        rotationRadiusMeters = wheelbaseMeters / 2 + trackWidthMeters / 2;
    }

    private static void positive(double value) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException("Simulation dimensions and speed must be positive");
        }
    }

    @Override
    public void setWheelPowers(WheelPowers powers) { this.powers = Objects.requireNonNull(powers); }

    /** Paso fijo recomendado: 0.02 segundos. Limite evita integrar pausas largas por accidente. */
    public void step(double dtSeconds) {
        if (!Double.isFinite(dtSeconds) || dtSeconds <= 0 || dtSeconds > 0.1) {
            throw new IllegalArgumentException("dt must be in (0, 0.1] seconds");
        }
        double fl = powers.frontLeft, fr = powers.frontRight;
        double rl = powers.rearLeft, rr = powers.rearRight;
        double vx = (fl + fr + rl + rr) * maxWheelSpeedMetersPerSecond / 4;
        double vy = (-fl + fr + rl - rr) * maxWheelSpeedMetersPerSecond / 4;
        double omega = (-fl + fr - rl + rr) * maxWheelSpeedMetersPerSecond
                / (4 * rotationRadiusMeters);
        double angle = omega * dtSeconds;
        double s = Math.abs(angle) < 1e-9 ? dtSeconds : Math.sin(angle) / omega;
        double c = Math.abs(angle) < 1e-9 ? 0 : (1 - Math.cos(angle)) / omega;
        double dx = vx * s - vy * c, dy = vx * c + vy * s;
        x += Math.cos(heading) * dx - Math.sin(heading) * dy;
        y += Math.sin(heading) * dx + Math.cos(heading) * dy;
        heading = Math.atan2(Math.sin(heading + angle), Math.cos(heading + angle));
    }

    public void resetPose(double xMeters, double yMeters, double headingRadians) {
        if (!Double.isFinite(xMeters) || !Double.isFinite(yMeters)
                || !Double.isFinite(headingRadians)) throw new IllegalArgumentException("Invalid pose");
        x = xMeters;
        y = yMeters;
        heading = Math.atan2(Math.sin(headingRadians), Math.cos(headingRadians));
        powers = WheelPowers.ZERO;
    }

    public double getXMeters() { return x; }
    public double getYMeters() { return y; }
    public double getHeadingRadians() { return heading; }
    public WheelPowers getPowers() { return powers; }
}
