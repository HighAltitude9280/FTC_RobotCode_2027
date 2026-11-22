package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.composite.IntakingSequenceCommands;
import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public class BeedrilProfile implements ControlProfile {
    private final Gamepad chassisOp;
    private final Gamepad subsystemsOp;

    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakeInCommand;
    private final Command intakeOutCommand;
    private final Command feedInCommand;
    private final Command feedOutCommand;
    private final Command intakingSequenceCommand;
    private final Command outtakingSequenceCommand;
    private final Command shooterCommand;

    private boolean previousOptions = false;

    public BeedrilProfile(
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

        intakeInCommand = IntakeCommands.intakeIn(robot.intake);

        intakeOutCommand = IntakeCommands.intakeOut(robot.intake);

        feedInCommand = FeederCommands.feedIn(robot.feeder);

        feedOutCommand = FeederCommands.feedOut(robot.feeder);

        intakingSequenceCommand = IntakingSequenceCommands.intakingSequence(robot.feeder, robot.intake);

        outtakingSequenceCommand = IntakingSequenceCommands.outtakingSequence(robot.feeder, robot.intake);

        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                LowAltitudeConstants.Shooter.TARGET_RPM
        );
    }

    @Override
    public double getDriverForward() {
        return -chassisOp.left_stick_y;
    }

    @Override
    public double getDriverStrafe() {
        return chassisOp.left_stick_x;
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
        cancelIfScheduled(intakeInCommand);
        cancelIfScheduled(intakeOutCommand);
        cancelIfScheduled(feedInCommand);
        cancelIfScheduled(feedOutCommand);
        cancelIfScheduled(intakingSequenceCommand);
        cancelIfScheduled(outtakingSequenceCommand);
        cancelIfScheduled(shooterCommand);
    }

    private void updateChassisOp() {
        boolean currentOptions = chassisOp.options;

        if (currentOptions && !previousOptions) {
            resetYawCommand.schedule();
        }
        previousOptions = currentOptions;
    }

    private void updateSubsystemsOp() {
        if (subsystemsOp.right_bumper) {
            scheduleIfNeeded(intakingSequenceCommand);
            cancelIfScheduled(outtakingSequenceCommand);
        } else if (subsystemsOp.left_bumper) {
            scheduleIfNeeded(outtakingSequenceCommand);
            cancelIfScheduled(outtakingSequenceCommand);
        } else {
            cancelIfScheduled(intakingSequenceCommand);
            cancelIfScheduled(outtakingSequenceCommand);
        }

        if (subsystemsOp.a) {
            scheduleIfNeeded(shooterCommand);
        } else {
            cancelIfScheduled(shooterCommand);
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