package org.firstinspires.ftc.teamcode.opmodes.teleOp;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.controllerProfiles.BeedrillStateProfile;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "Beedrill State TeleOp", group = "TeleOp")
public class BeedrillStateTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Scheduler.reset();
        RobotContainer robot = new RobotContainer(hardwareMap);
        BeedrillStateProfile profile = new BeedrillStateProfile(gamepad1, gamepad2, robot);

        try {
            waitForStart();
            if (isStopRequested()) {
                return;
            }

            profile.start();
            while (opModeIsActive()) {
                robot.periodic();
                profile.update();
                Scheduler.execute();

                boolean shooterReady = robot.shooter.getTargetRPM() > 0.0
                        && robot.shooter.isAtTargetRPM();
                telemetry.addData("Estado", profile.getMatchState());
                telemetry.addData("RPM", robot.shooter.getCurrentRPM());
                telemetry.addData("isReady", shooterReady);
                telemetry.update();
            }
        } finally {
            profile.stop();
            robot.stopAll();
            Scheduler.reset();
        }
    }
}
