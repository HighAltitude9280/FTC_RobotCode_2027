package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.controllerProfiles.BeedrilProfile;
import org.firstinspires.ftc.teamcode.controllerProfiles.ControlProfile;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Subsystem Test")
public class SubsystemTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        Scheduler.reset();

        RobotContainer robot = new RobotContainer(hardwareMap);

        ControlProfile profile =
                new BeedrilProfile(gamepad1, gamepad2, robot);

        try {
            telemetry.update();

            waitForStart();

            if (isStopRequested()) {
                return;
            }

            profile.start();

            while (opModeIsActive()) {
                robot.periodic();
                profile.update();
                Scheduler.execute();
                updateTelemetry(robot, profile);
            }

        } finally {

            profile.stop();

            robot.stopAll();

            Scheduler.reset();
        }
    }

    private void updateTelemetry(
            RobotContainer robot,
            ControlProfile profile
    ) {
        telemetry.addData(
                "Heading Radians",
                robot.drivetrain.getHeadingRadians()
        );

        telemetry.addData(
                "Heading Degrees",
                robot.drivetrain.getHeadingDegrees()
        );

        // SHOOTER
        telemetry.addData(
                "Shooter Target RPM",
                robot.shooter.getTargetRPM()
        );

        telemetry.addData(
                "Shooter Current RPM",
                robot.shooter.getCurrentRPM()
        );

        telemetry.addData(
                "Shooter RPM Error",
                robot.shooter.getRPMError()
        );

        telemetry.addData(
                "Shooter Ready",
                robot.shooter.isAtTargetRPM()
        );

        telemetry.addData(
                "Drive Forward",
                profile.getChassisDriverForward()
        );

        telemetry.addData(
                "Drive Strafe",
                profile.getChassisDriverStrafe()
        );

        telemetry.addData(
                "Drive Turn",
                profile.getChassisDriverTurn()
        );

        telemetry.update();
    }
}