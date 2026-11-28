package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.composite.IntakingSequenceCommands;
import org.firstinspires.ftc.teamcode.commands.composite.ShootingSequenceCommands;
import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.kicker.KickerCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public class BeedrillStateProfile implements ControlProfile {
    private static final int STATE_RUMBLE_MS = 200;

    private final Gamepad chassisOp;
    private final Gamepad subsystemsOp;
    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command shooterCommand;
    private final Command intakingSequence;
    private final Command outtakingSequence;
    private final Command kickInCommand;
    private final Command feedWhenReadyCommand;

    private MatchState matchState = MatchState.IDLE;
    private Command activeMechanismCommand;
    private boolean previousOptions;
    private boolean previousA;
    private boolean previousY;
    private boolean previousX;

    public BeedrillStateProfile(
            Gamepad gamepad1,
            Gamepad gamepad2,
            RobotContainer robot
    ) {
        chassisOp = gamepad1;
        subsystemsOp = gamepad2;

        driveCommand = DrivetrainCommands.driveFieldCentric(
                robot.drivetrain,
                this::getDriverForward,
                this::getDriverStrafe,
                this::getDriverTurn
        );
        resetYawCommand = instant(robot.drivetrain::resetYaw);
        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                LowAltitudeConstants.Shooter.TARGET_RPM
        );
        intakingSequence = IntakingSequenceCommands.intakingSequence(
                robot.feeder,
                robot.intake,
                robot.kicker
        );
        outtakingSequence = IntakingSequenceCommands.outtakingSequence(
                robot.feeder,
                robot.intake,
                robot.kicker
        );
        kickInCommand = KickerCommands.kickIn(robot.kicker);
        feedWhenReadyCommand = ShootingSequenceCommands.feedWhenReady(
                robot.feeder,
                robot.intake,
                robot.kicker,
                robot.shooter
        );
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

    public MatchState getMatchState() {
        return matchState;
    }

    @Override
    public void start() {
        matchState = MatchState.IDLE;
        driveCommand.schedule();
    }

    @Override
    public void update() {
        updateChassisOp();
        updateMatchState();
        updateShooter();
        updateMechanisms();
    }

    @Override
    public void stop() {
        cancelIfScheduled(activeMechanismCommand);
        activeMechanismCommand = null;
        cancelIfScheduled(shooterCommand);
        cancelIfScheduled(resetYawCommand);
        cancelIfScheduled(driveCommand);
    }

    private void updateChassisOp() {
        boolean currentOptions = chassisOp.options;

        if (currentOptions && !previousOptions) {
            cancelIfScheduled(resetYawCommand);
            resetYawCommand.schedule();
        }
        previousOptions = currentOptions;
    }

    private void updateMatchState() {
        boolean currentA = subsystemsOp.a;
        boolean currentY = subsystemsOp.y;
        boolean currentX = subsystemsOp.x;

        if (currentA && !previousA) {
            selectState(MatchState.INTAKING);
        } else if (currentY && !previousY) {
            selectState(MatchState.SHOOTING);
        } else if (currentX && !previousX) {
            selectState(MatchState.IDLE);
        }

        previousA = currentA;
        previousY = currentY;
        previousX = currentX;
    }

    private void selectState(MatchState newState) {
        matchState = newState;
        subsystemsOp.rumble(STATE_RUMBLE_MS);
    }

    private void updateShooter() {
        if (matchState == MatchState.SHOOTING) {
            scheduleIfNeeded(shooterCommand);
        } else {
            cancelIfScheduled(shooterCommand);
        }
    }

    private void updateMechanisms() {
        Command desiredCommand = getDesiredMechanismCommand();

        if (desiredCommand != activeMechanismCommand) {
            cancelIfScheduled(activeMechanismCommand);
            activeMechanismCommand = desiredCommand;
        }

        scheduleIfNeeded(activeMechanismCommand);
    }

    private Command getDesiredMechanismCommand() {
        if (subsystemsOp.left_bumper) {
            return outtakingSequence;
        }
        if (subsystemsOp.b) {
            return kickInCommand;
        }
        if (matchState == MatchState.INTAKING) {
            return intakingSequence;
        }
        if (matchState == MatchState.SHOOTING && subsystemsOp.right_trigger_pressed) {
            return feedWhenReadyCommand;
        }
        if (matchState == MatchState.IDLE && subsystemsOp.right_bumper) {
            return intakingSequence;
        }
        return null;
    }

    private void scheduleIfNeeded(Command command) {
        if (command != null && !command.isScheduled()) {
            command.schedule();
        }
    }

    private void cancelIfScheduled(Command command) {
        if (command != null && command.isScheduled()) {
            command.cancel();
        }
    }

    @Override
    public void configureButtonBinding(RobotContainer robot) {
    }
}
