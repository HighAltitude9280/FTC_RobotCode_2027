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

        Command intakeIn =
                IntakeCommands.intakeIn(robot.intake);

        Command intakeOut =
                IntakeCommands.intakeOut(robot.intake);

        double shooterTestRPM = LowAltitudeConstants.SHOOTER.SHOOT_RPM;

        Command shooterShoot =
                ShooterCommands.shooterShoot(
                        robot.shooter,
                        shooterTestRPM
                );

        Command driveRobotOriented =
                DrivetrainCommands.driveRobotOriented(
                        robot.drivetrain,
                        () -> -gamepad1.left_stick_y,
                        () -> gamepad1.left_stick_x,
                        () -> gamepad1.right_stick_x
                );

        telemetry.addLine("Subsystem Test listo");
        telemetry.update();

        waitForStart();

        driveRobotOriented.schedule();

        while (opModeIsActive()) {

            // =========================
            // PERIODIC
            // =========================

            robot.drivetrain.periodic();
            robot.shooter.periodic();

            // =========================
            // INTAKE
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
            // SHOOTER
            // A = mantener shooter activo
            // =========================

            if (gamepad1.a) {

                if (!shooterShoot.isScheduled()) {
                    shooterShoot.schedule();
                }

            } else {

                if (shooterShoot.isScheduled()) {
                    shooterShoot.cancel();
                }
            }


            // =========================
            // IVY SCHEDULER
            // =========================

            Scheduler.execute();


            // =========================
            // DRIVETRAIN TELEMETRY
            // =========================

            telemetry.addData(
                    "Heading radians",
                    robot.drivetrain.getHeadingRadians()
            );

            telemetry.addData(
                    "Heading degrees",
                    robot.drivetrain.getHeadingDegrees()
            );


            // =========================
            // SHOOTER TELEMETRY
            // =========================

            telemetry.addData(
                    "Shooter Target RPM",
                    robot.shooter.getTargetRPM()
            );

            telemetry.addData(
                    "Shooter Current RPM",
                    robot.shooter.getCurrentRPM()
            );

            telemetry.addData(
                    "Shooter Error RPM",
                    robot.shooter.getRPMError()
            );

            telemetry.addData(
                    "Shooter Ready",
                    robot.shooter.isAtTargetRPM()
            );

            telemetry.addData(
                    "Shooter Command",
                    shooterShoot.isScheduled()
            );


            // =========================
            // GAMEPAD TELEMETRY
            // =========================

            telemetry.addData(
                    "Left Y Joystick",
                    -gamepad1.left_stick_y
            );

            telemetry.addData(
                    "Left X Joystick",
                    gamepad1.left_stick_x
            );

            telemetry.addData(
                    "Right X Joystick",
                    gamepad1.right_stick_x
            );

            telemetry.update();
        }


        // =========================
        // CLEANUP
        // =========================

        if (intakeIn.isScheduled()) {
            intakeIn.cancel();
        }

        if (intakeOut.isScheduled()) {
            intakeOut.cancel();
        }

        if (shooterShoot.isScheduled()) {
            shooterShoot.cancel();
        }

        robot.drivetrain.stop();
        robot.shooter.stop();
    }
}