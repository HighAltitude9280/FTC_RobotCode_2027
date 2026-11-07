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
                new MotorShooterIO(hardwareMap, LowAltitudeConstants.Shooter.MOTOR_NAME);

        Shooter shooter = new Shooter(shooterIO);

        Command shooterTuningCommand = ShooterCommands.tuneVelocity(shooter);

        TelemetryManager panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        telemetry.addLine("Ajuste del shooter listo: Shooter PIDF Tuning.");
        telemetry.addLine("A: activar el shooter.");
        telemetry.addLine("B: detener el shooter.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a && !shooterTuningCommand.isScheduled()) {
                shooterTuningCommand.schedule();
            }

            if (gamepad1.b && shooterTuningCommand.isScheduled()) {
                shooterTuningCommand.cancel();
            }

            Scheduler.execute();

            shooter.periodic();

            panelsTelemetry.addData("Shooter Enabled", shooterTuningCommand.isScheduled());

            panelsTelemetry.addData("Shooter Target RPM", shooter.getTargetRPM());

            panelsTelemetry.addData("Shooter Current RPM", shooter.getCurrentRPM());

            panelsTelemetry.addData("Shooter RPM Error", shooter.getRPMError());

            panelsTelemetry.addData("Shooter Ready", shooter.isAtTargetRPM());

            panelsTelemetry.addData("Shooter P", LowAltitudeConstants.Shooter.kP);

            panelsTelemetry.addData("Shooter I", LowAltitudeConstants.Shooter.kI);

            panelsTelemetry.addData("Shooter D", LowAltitudeConstants.Shooter.kD);

            panelsTelemetry.addData("Shooter F", LowAltitudeConstants.Shooter.kF);

            panelsTelemetry.update(telemetry);
        }

        if (shooterTuningCommand.isScheduled()) {
            shooterTuningCommand.cancel();
        }

        shooter.stop();

        Scheduler.reset();
    }
}
