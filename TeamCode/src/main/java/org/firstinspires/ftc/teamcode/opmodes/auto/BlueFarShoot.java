package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.commands.composite.ShootingSequenceCommands;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

@Autonomous(name = "Blue Far Shoot", group = "Autonomous")
public class BlueFarShoot extends LinearOpMode {
    @Override
    public void runOpMode() {
        Scheduler.reset();
        RobotContainer robot = new RobotContainer(hardwareMap);
        Follower follower = Constants.create(hardwareMap);

        PoseFactory poses = PoseFactory.degrees().mirrorX(70.75);
        Pose start = poses.of(82, 133, 270);
        Pose point1 = poses.of(115, 133, 270);
        Pose point2 = poses.of(115, 40, 270);
        Pose point3 = poses.of(135, 40, 270);
        follower.setPose(start);
        follower.update();

        Command shoot = deadline(
                waitMs(15_000),
                ShootingSequenceCommands.shootingSequence(
                        robot.feeder,
                        robot.intake,
                        robot.shooter,
                        robot.kicker
                )
        );
        Command blueFarPath = sequential(
                follow(follower, Paths.line(start, point1).constant(point1)),
                follow(follower, Paths.line(point1, point2).reverseTangent()),
                follow(follower, Paths.line(point2, point3).reverseTangent())
        );
        Command auto = sequential(shoot, blueFarPath);

        try {
            waitForStart();
            if (isStopRequested()) return;

            auto.schedule();
            while (opModeIsActive()) {
                robot.periodic();
                follower.update();
                Scheduler.execute();
            }
        } finally {
            if (auto.isScheduled()) {
                auto.cancel();
            }
            robot.stopAll();
            follower.stop();
            follower.update();
            Scheduler.reset();
        }
    }
}
