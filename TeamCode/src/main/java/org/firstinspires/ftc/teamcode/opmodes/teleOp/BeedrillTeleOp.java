package org.firstinspires.ftc.teamcode.opmodes.teleOp;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.controllerProfiles.BeedrillProfile;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@TeleOp(name = "BeedrillTeleOp", group = "TeleOp")
public class BeedrillTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Scheduler.reset();
        RobotContainer robot = new RobotContainer(hardwareMap);
        BeedrillProfile profile = new BeedrillProfile(gamepad1, gamepad2, robot);

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
            }
        } finally {
            profile.stop();
            robot.stopAll();
            Scheduler.reset();
        }
    }
}
