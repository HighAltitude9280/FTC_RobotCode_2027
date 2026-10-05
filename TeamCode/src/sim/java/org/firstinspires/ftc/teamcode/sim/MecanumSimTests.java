package org.firstinspires.ftc.teamcode.sim;

import org.firstinspires.ftc.teamcode.commands.DriveMecanum;
import org.firstinspires.ftc.teamcode.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.drive.WheelPowers;
import org.firstinspires.ftc.teamcode.drive.io.MecanumDriveIOSim;

/** Pruebas deterministas sin Android ni bibliotecas externas. Error = salida no cero. */
public final class MecanumSimTests {
    private static int checks;

    public static void main(String[] args) {
        MecanumDriveIOSim sim = new MecanumDriveIOSim(1.5, 0.4, 0.4);
        MecanumDrive drive = new MecanumDrive(sim, 0.3, 0.06);
        DriveMecanum command = new DriveMecanum(drive);
        command.execute(1, 0, 0, true);
        wheels(sim, .3, .3, .3, .3);
        command.execute(0, 1, 0, true);
        wheels(sim, -.3, .3, .3, -.3);
        command.execute(0, 0, 1, true);
        wheels(sim, -.3, .3, -.3, .3);
        command.execute(1, 1, 1, true);
        wheels(sim, -.1, .3, .1, .1);
        command.execute(.05, -.06, 0, true);
        wheels(sim, 0, 0, 0, 0);
        command.execute(1, 1, 1, false);
        wheels(sim, 0, 0, 0, 0);
        command.execute(1, 0, 0, true);
        command.execute(Double.NaN, 1, 1, true);
        wheels(sim, 0, 0, 0, 0);
        command.execute(Double.POSITIVE_INFINITY, 0, 0, true);
        wheels(sim, 0, 0, 0, 0);
        command.execute(10, 0, 0, true);
        wheels(sim, .3, .3, .3, .3);
        command.end();
        wheels(sim, 0, 0, 0, 0);

        // Analytic movement independent of the mixer: equal wheel speeds travel 0.45m in 1s.
        sim.setWheelPowers(new WheelPowers(.3, .3, .3, .3));
        steps(sim, 50);
        pose(sim, .45, 0, 0);
        sim.resetPose(0, 0, 0);
        sim.setWheelPowers(new WheelPowers(-.3, .3, .3, -.3));
        steps(sim, 50);
        pose(sim, 0, .45, 0);
        sim.resetPose(0, 0, 0);
        sim.setWheelPowers(new WheelPowers(-.3, .3, -.3, .3));
        steps(sim, 50);
        pose(sim, 0, 0, 1.125);
        sim.resetPose(0, 0, Math.PI / 2);
        sim.setWheelPowers(new WheelPowers(.3, .3, .3, .3));
        steps(sim, 50);
        pose(sim, 0, .45, Math.PI / 2);

        // Combined arc: vx=.3m/s, vy=0, omega=.75rad/s. Compare analytic circle.
        sim.resetPose(0, 0, 0);
        sim.setWheelPowers(new WheelPowers(0, .4, 0, .4));
        steps(sim, 50);
        pose(sim, .4 * Math.sin(.75), .4 * (1 - Math.cos(.75)), .75);

        sim.resetPose(0, 0, 0);
        double[][] square = {{1,0}, {0,1}, {-1,0}, {0,-1}};
        for (double[] leg : square) {
            command.execute(leg[0], leg[1], 0, true);
            steps(sim, 100);
        }
        pose(sim, 0, 0, 0);
        command.execute(1, 0, 0, true);
        steps(sim, 25);
        command.end();
        double stoppedX = sim.getXMeters();
        steps(sim, 50);
        pose(sim, stoppedX, 0, 0);
        sim.resetPose(0, 0, 0);
        wheels(sim, 0, 0, 0, 0);
        rejects(() -> sim.step(-.02));
        rejects(() -> sim.step(Double.NaN));
        rejects(() -> sim.step(.2));
        rejects(() -> new MecanumDriveIOSim(0, .4, .4));
        rejects(() -> new MecanumDrive(sim, 1.1, .06));
        rejects(() -> new WheelPowers(0, 0, Double.NaN, 0));
        rejects(() -> sim.resetPose(0, Double.NaN, 0));
        System.out.println("PASS: " + checks + " mecanum/simulation checks");
    }

    private static void steps(MecanumDriveIOSim sim, int count) {
        for (int i = 0; i < count; i++) sim.step(.02);
    }

    private static void pose(MecanumDriveIOSim s, double x, double y, double angle) {
        close(s.getXMeters(), x); close(s.getYMeters(), y); close(s.getHeadingRadians(), angle);
    }

    private static void wheels(MecanumDriveIOSim s, double fl, double fr, double rl, double rr) {
        WheelPowers p = s.getPowers();
        close(p.frontLeft, fl); close(p.frontRight, fr); close(p.rearLeft, rl); close(p.rearRight, rr);
    }

    private static void close(double actual, double expected) {
        checks++;
        if (!Double.isFinite(actual) || Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }

    private static void rejects(Runnable action) {
        checks++;
        try { action.run(); }
        catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Invalid input accepted");
    }
}
