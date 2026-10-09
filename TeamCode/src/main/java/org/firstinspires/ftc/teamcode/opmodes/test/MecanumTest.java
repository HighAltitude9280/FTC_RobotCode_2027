package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Mecanum Test")
public class MecanumTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        RobotContainer robot = new RobotContainer(hardwareMap);

        telemetry.addLine("Mecanum listo");
        telemetry.update();

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

        robot.drivetrain.stop();
    }
}