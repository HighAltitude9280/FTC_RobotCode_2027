package org.firstinspires.ftc.teamcode.opmodes.tuning;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.shooter.MotorShooterIO;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

@TeleOp(name = "Shooter PIDF Tuning", group = "Tuning")
public class ShooterPIDFTuning extends LinearOpMode {

    @Override
    public void runOpMode() {

        Scheduler.reset();

        MotorShooterIO shooterIO =
                new MotorShooterIO(
                        hardwareMap,
                        LowAltitudeConstants.SHOOTER.SHOOTER_NAME
                );

        Shooter shooter =
                new Shooter(shooterIO);

        Command shooterTune =
                ShooterCommands.shooterTune(shooter);

        TelemetryManager panelsTelemetry =
                PanelsTelemetry.INSTANCE.getTelemetry();

        telemetry.addLine("Shooter PIDF Tuning listo");
        telemetry.addLine("A = Shooter ON");
        telemetry.addLine("B = Shooter OFF");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !shooterTune.isScheduled()) {
                shooterTune.schedule();
            }

            if (gamepad1.b && shooterTune.isScheduled()) {
                shooterTune.cancel();
            }

            Scheduler.execute();

            shooter.periodic();

            panelsTelemetry.addData(
                    "Shooter Enabled",
                    shooterTune.isScheduled()
            );

            panelsTelemetry.addData(
                    "Shooter Target RPM",
                    shooter.getTargetRPM()
            );

            panelsTelemetry.addData(
                    "Shooter Current RPM",
                    shooter.getCurrentRPM()
            );

            panelsTelemetry.addData(
                    "Shooter RPM Error",
                    shooter.getRPMError()
            );

            panelsTelemetry.addData(
                    "Shooter Ready",
                    shooter.isAtTargetRPM()
            );

            panelsTelemetry.addData(
                    "Shooter P",
                    LowAltitudeConstants.SHOOTER.SHOOTER_P
            );

            panelsTelemetry.addData(
                    "Shooter I",
                    LowAltitudeConstants.SHOOTER.SHOOTER_I
            );

            panelsTelemetry.addData(
                    "Shooter D",
                    LowAltitudeConstants.SHOOTER.SHOOTER_D
            );

            panelsTelemetry.addData(
                    "Shooter F",
                    LowAltitudeConstants.SHOOTER.SHOOTER_F
            );

            panelsTelemetry.update(telemetry);
        }

        if (shooterTune.isScheduled()) {
            shooterTune.cancel();
        }

        shooter.stop();

        Scheduler.reset();
    }
}