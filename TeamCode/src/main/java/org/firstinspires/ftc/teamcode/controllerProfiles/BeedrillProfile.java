package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.composite.IntakingSequenceCommands;
import org.firstinspires.ftc.teamcode.commands.composite.ShootingSequenceCommands;
import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.kicker.KickerCommands;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public class BeedrillProfile implements ControlProfile{
    private final Gamepad chassisOp;
    private final Gamepad subsystemsOp;
    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakingSequence;
    private final Command outtakingSequence;
    private final Command shootingSequence;
    private final Command kickIn;
    private boolean previousOptions = false;
    public BeedrillProfile(
            Gamepad gamepad1,
            Gamepad gamepad2,
            RobotContainer robot
    ) {
        this.chassisOp = gamepad1;
        this.subsystemsOp = gamepad2;

        driveCommand = DrivetrainCommands.driveFieldCentric(
                robot.drivetrain,
                this::getDriverForward,
                this::getDriverStrafe,
                this::getDriverTurn
        );

        resetYawCommand = instant(robot.drivetrain::resetYaw);

        intakingSequence = IntakingSequenceCommands.intakingSequence(robot.feeder, robot.intake, robot.kicker);
        outtakingSequence = IntakingSequenceCommands.outtakingSequence(robot.feeder, robot.intake, robot.kicker);
        kickIn = KickerCommands.kickIn(robot.kicker);

        shootingSequence = ShootingSequenceCommands.shootingSequence(robot.feeder, robot.intake, robot.shooter);

    }

    @Override
    public double getDriverStrafe() {
        return chassisOp.left_stick_x;
    }

    @Override
    public double getDriverForward() {
        return -chassisOp.left_stick_y;
    }

    @Override
    public double getDriverTurn() {
        return chassisOp.right_stick_x;
    }

    @Override
    public void start() {
        driveCommand.schedule();
    }

    @Override
    public void update() {
        updateChassisOp();
        updateSubsystemsOp();
    }

    @Override
    public void stop() {
        cancelIfScheduled(driveCommand);
        cancelIfScheduled(resetYawCommand);
        cancelIfScheduled(intakingSequence);
        cancelIfScheduled(outtakingSequence);
    }

    private void updateChassisOp() {
        boolean currentOptions = chassisOp.options;

        if (currentOptions && !previousOptions) {
            resetYawCommand.schedule();
        }
        previousOptions = currentOptions;
    }

    private void updateSubsystemsOp() {
        if(subsystemsOp.b) {
            scheduleIfNeeded(kickIn);
        } else {
            cancelIfScheduled(kickIn);
        }

        if(subsystemsOp.right_trigger_pressed) {
            cancelIfScheduled(intakingSequence);
            cancelIfScheduled(outtakingSequence);
            scheduleIfNeeded(shootingSequence);
        }
        else if (subsystemsOp.right_bumper) {
            cancelIfScheduled(outtakingSequence);
            cancelIfScheduled(shootingSequence);
            scheduleIfNeeded(intakingSequence);
        } else if (subsystemsOp.left_bumper) {
            cancelIfScheduled(intakingSequence);
            cancelIfScheduled(shootingSequence);
            scheduleIfNeeded(outtakingSequence);
        } else {
            cancelIfScheduled(intakingSequence);
            cancelIfScheduled(outtakingSequence);
            cancelIfScheduled(shootingSequence);
        }
    }

    private void scheduleIfNeeded(Command command) {
        if (!command.isScheduled()) {
            command.schedule();
        }
    }

    private void cancelIfScheduled(Command command) {
        if (command.isScheduled()) {
            command.cancel();
        }
    }

    @Override
    public void configureButtonBinding(RobotContainer robot) {

    }
}
