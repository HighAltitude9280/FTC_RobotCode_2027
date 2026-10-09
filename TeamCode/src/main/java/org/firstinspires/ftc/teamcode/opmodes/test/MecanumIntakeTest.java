package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Mecanum Intake Test")
public class MecanumIntakeTest extends LinearOpMode {

    @Override
    public void runOpMode() {
        RobotContainer robot = new RobotContainer(hardwareMap);

        telemetry.addLine("Mecanum listo");
        telemetry.update();

        Command intakeIn =
                IntakeCommands.intakeIn(robot.intake);

        Command intakeOut =
                IntakeCommands.intakeOut(robot.intake);

        waitForStart();

        while (opModeIsActive()) {

            robot.drivetrain.periodic();

            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double rotation = gamepad1.right_stick_x;

            robot.drivetrain.driveRobotCentric(
                    forward,
                    strafe,
                    rotation
            );

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

            Scheduler.execute();

            telemetry.addData(
                    "Heading radians",
                    robot.drivetrain.getHeadingRadians()
            );

            telemetry.addData(
                    "Heading degrees",
                    robot.drivetrain.getHeadingDegrees()
            );

            telemetry.addData("Left Y Joystick", -gamepad1.left_stick_y);
            telemetry.addData("Left X Joystick", gamepad1.left_stick_x);
            telemetry.addData("Right X Joystick", gamepad1.right_stick_x);

            telemetry.update();
        }

        if (intakeIn.isScheduled()) {
            intakeIn.cancel();
        }

        if (intakeOut.isScheduled()) {
            intakeOut.cancel();
        }

        robot.drivetrain.stop();

    }
}
