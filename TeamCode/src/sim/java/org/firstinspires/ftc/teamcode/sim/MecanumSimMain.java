package org.firstinspires.ftc.teamcode.sim;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import org.firstinspires.ftc.teamcode.commands.DriveMecanum;
import org.firstinspires.ftc.teamcode.drive.MecanumDrive;
import org.firstinspires.ftc.teamcode.drive.WheelPowers;
import org.firstinspires.ftc.teamcode.drive.io.MecanumDriveIOSim;

/** Demo determinista de 18 s. Genera archivos; no requiere hub, Android ni motores. */
public final class MecanumSimMain {
    public static void main(String[] args) throws Exception {
        Path output = Paths.get(args.length == 0 ? "build/mecanum-sim" : args[0]);
        Files.createDirectories(output);
        MecanumDriveIOSim sim = new MecanumDriveIOSim(1.5, 0.4, 0.4);
        MecanumDrive drive = new MecanumDrive(sim, 0.3, 0.06);
        DriveMecanum command = new DriveMecanum(drive);

        try (WpiLogWriter log = new WpiLogWriter(output.resolve("mecanum-demo.wpilog"));
             BufferedWriter csv = Files.newBufferedWriter(output.resolve("mecanum-demo.csv"),
                     StandardCharsets.UTF_8)) {
            int pose = log.start("/Sim/Pose", "struct:Pose2d");
            int input = log.start("/Sim/InputForwardLeftCCW", "double[]");
            int powers = log.start("/Sim/WheelPowersFLFRRLRR", "double[]");
            int enabled = log.start("/DriverStation/Enabled", "boolean");
            int auto = log.start("/DriverStation/Autonomous", "boolean");
            int phase = log.start("/Sim/Phase", "string");
            int model = log.start("/Metadata/Model", "string");
            log.text(model, 0, "Ideal mecanum; 1.5 m/s wheel speed; wheelbase=track=0.4m; limit=0.3; NOT tuned");
            csv.write("Timestamp,/Sim/XMeters,/Sim/YMeters,/Sim/HeadingRadians,/Sim/FL,/Sim/FR,/Sim/RL,/Sim/RR,/Sim/Enabled\n");
            for (int tick = 0; tick <= 900; tick++) {
                // First integrate the previous interval, then sample state and the new command.
                if (tick > 0) sim.step(0.02);
                double t = tick * 0.02;
                double f = 0, l = 0, r = 0;
                boolean active = true;
                String name;
                if (tick < 100) { f = 1; name = "1-forward"; }
                else if (tick < 200) { l = 1; name = "2-left"; }
                else if (tick < 300) { f = -1; name = "3-backward"; }
                else if (tick < 400) { l = -1; name = "4-right-square-closed"; }
                else if (tick < 500) { r = Math.PI / 4 / 1.125; name = "5-turn-90-deg"; }
                else if (tick < 600) { f = 1; name = "6-forward-at-90-deg"; }
                else if (tick < 700) { f = 0.6; l = 0.3; r = 0.4; name = "7-combined-arc"; }
                else { f = 1; r = 1; active = false; name = "8-disabled-stop"; }
                command.execute(f, l, r, active);
                WheelPowers p = drive.getPowers();
                long us = tick * 20000L;
                log.numbers(pose, us, sim.getXMeters(), sim.getYMeters(), sim.getHeadingRadians());
                log.numbers(input, us, f, l, r);
                log.numbers(powers, us, p.frontLeft, p.frontRight, p.rearLeft, p.rearRight);
                log.bool(enabled, us, active);
                log.bool(auto, us, false);
                log.text(phase, us, name);
                csv.write(String.format(Locale.ROOT, "%.2f,%.9f,%.9f,%.9f,%.9f,%.9f,%.9f,%.9f,%s%n",
                        t, sim.getXMeters(), sim.getYMeters(), sim.getHeadingRadians(),
                        p.frontLeft, p.frontRight, p.rearLeft, p.rearRight, active));
            }
            command.end();
        } finally {
            command.end();
        }
        System.out.println("AdvantageScope: " + output.resolve("mecanum-demo.wpilog").toAbsolutePath());
        System.out.println("2D Field -> /Sim/Pose -> Robot; origin center, meters, radians.");
    }
}
