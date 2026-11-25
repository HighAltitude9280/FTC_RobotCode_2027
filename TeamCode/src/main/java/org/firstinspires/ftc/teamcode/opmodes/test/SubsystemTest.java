package org.firstinspires.ftc.teamcode.opmodes.test;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.controllerProfiles.TestProfile;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Subsystem Test")
public class SubsystemTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        Scheduler.reset();

        RobotContainer robot = new RobotContainer(hardwareMap);

        TestProfile profile =
                new TestProfile(gamepad1, gamepad2, robot);

        try {
            telemetry.addLine("Gamepad 1: D-pad up/down cycles shooter RPM by 100 (0-3000); starts at 2000 RPM.");
            telemetry.addLine("Gamepad 1: hold B/A = kicker in/out; X/Y = feeder in/out.");
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
            TestProfile profile
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
        telemetry.addLine("Gamepad 1: D-pad up/down = +/-100 RPM (wraps 0-3000)");
        telemetry.addLine("Gamepad 1: hold B/A = kicker in/out; X/Y = feeder in/out.");
        telemetry.addData(
                "Shooter Selected RPM (D-pad)",
                profile.getSelectedShooterRPM()
        );
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
                profile.getDriverForward()
        );

        telemetry.addData(
                "Drive Strafe",
                profile.getDriverStrafe()
        );

        telemetry.addData(
                "Drive Turn",
                profile.getDriverTurn()
        );

        telemetry.update();
    }
}
