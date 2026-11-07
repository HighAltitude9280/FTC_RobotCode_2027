package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Subsystem Test")
public class SubsystemTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        Scheduler.reset();

        RobotContainer robot = new RobotContainer(hardwareMap);

        Command intakeIn = IntakeCommands.intakeIn(robot.intake);

        Command intakeOut = IntakeCommands.intakeOut(robot.intake);

        double shooterTestRPM = LowAltitudeConstants.Shooter.TARGET_RPM;

        Command shooterCommand = ShooterCommands.runAtRPM(robot.shooter, shooterTestRPM);

        Command driveRobotCentricCommand =
                DrivetrainCommands.driveRobotCentric(
                        robot.drivetrain,
                        () -> -gamepad1.left_stick_y,
                        () -> gamepad1.left_stick_x,
                        () -> gamepad1.right_stick_x);

        telemetry.addLine("Prueba de subsistemas lista: Subsystem Test.");
        telemetry.update();

        waitForStart();

        driveRobotCentricCommand.schedule();

        while (opModeIsActive()) {

            // =========================
            // Actualización de subsistemas
            // =========================

            robot.drivetrain.periodic();
            robot.shooter.periodic();

            // =========================
            // Intake
            // =========================

            if (gamepad1.right_bumper) {

                if (!intakeOut.isScheduled()) {
                    intakeOut.schedule();
                }

            } else if (gamepad1.left_bumper) {

                if (!intakeIn.isScheduled()) {
                    intakeIn.schedule();
                }

            } else {

                if (intakeIn.isScheduled()) {
                    intakeIn.cancel();
                }

                if (intakeOut.isScheduled()) {
                    intakeOut.cancel();
                }
            }

            // =========================
            // Shooter
            // A = mantener shooter activo
            // =========================

            if (gamepad1.a) {

                if (!shooterCommand.isScheduled()) {
                    shooterCommand.schedule();
                }

            } else {

                if (shooterCommand.isScheduled()) {
                    shooterCommand.cancel();
                }
            }

            // =========================
            // Ejecución de comandos Ivy
            // =========================

            Scheduler.execute();

            // =========================
            // Drivetrain TELEMETRY
            // =========================

            telemetry.addData("Heading (rad)", robot.drivetrain.getHeadingRadians());

            telemetry.addData("Heading (deg)", robot.drivetrain.getHeadingDegrees());

            // =========================
            // Shooter TELEMETRY
            // =========================

            telemetry.addData("Shooter Target RPM", robot.shooter.getTargetRPM());

            telemetry.addData("Shooter Current RPM", robot.shooter.getCurrentRPM());

            telemetry.addData("Shooter RPM Error", robot.shooter.getRPMError());

            telemetry.addData("Shooter Ready", robot.shooter.isAtTargetRPM());

            telemetry.addData("Shooter Command", shooterCommand.isScheduled());

            // =========================
            // Telemetría del gamepad
            // =========================

            telemetry.addData("Left Y Joystick", -gamepad1.left_stick_y);

            telemetry.addData("Left X Joystick", gamepad1.left_stick_x);

            telemetry.addData("Right X Joystick", gamepad1.right_stick_x);

            telemetry.update();
        }

        // =========================
        // Cierre de la prueba
        // =========================

        if (intakeIn.isScheduled()) {
            intakeIn.cancel();
        }

        if (intakeOut.isScheduled()) {
            intakeOut.cancel();
        }

        if (shooterCommand.isScheduled()) {
            shooterCommand.cancel();
        }

        robot.drivetrain.stop();
        robot.shooter.stop();
    }
}
