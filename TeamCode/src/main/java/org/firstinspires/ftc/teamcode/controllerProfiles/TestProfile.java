package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.kicker.KickerCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public class TestProfile implements ControlProfile {
    private static final double SHOOTER_RPM_STEP = 500.0;
    private static final double SHOOTER_MAX_RPM = 3000.0;

    private final Gamepad mainOp;
    private final Gamepad sideOp;

    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakeInCommand;
    private final Command intakeOutCommand;
    private final Command feedInCommand;
    private final Command feedOutCommand;
    private final Command kickerInCommand;
    private final Command kickerOutCommand;
    private final Command shooterCommand;

    private boolean previousOptions = false;
    private boolean previousDpadUp = false;
    private boolean previousDpadDown = false;
    private double selectedShooterRPM = 0.0;

    public TestProfile(
            Gamepad gamepad1,
            Gamepad gamepad2,
            RobotContainer robot
    ) {
        this.mainOp = gamepad1;
        this.sideOp = gamepad2;

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

        kickerInCommand = KickerCommands.kickIn(robot.kicker);
        kickerOutCommand = KickerCommands.kickOut(robot.kicker);

        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                () -> selectedShooterRPM
        );
    }

    @Override
    public double getDriverForward() {
        return -mainOp.left_stick_y;
    }

    @Override
    public double getDriverStrafe() {
        return mainOp.left_stick_x;
    }

    @Override
    public double getDriverTurn() {
        return mainOp.right_stick_x;
    }

    @Override
    public void start() {
        selectedShooterRPM = 0.0;
        previousDpadUp = mainOp.dpad_up;
        previousDpadDown = mainOp.dpad_down;
        driveCommand.schedule();
    }

    @Override
    public void update() {
        updateMainOp();
        updateSideOp();
    }

    @Override
    public void stop() {
        cancelIfScheduled(driveCommand);
        cancelIfScheduled(resetYawCommand);
        cancelIfScheduled(intakeInCommand);
        cancelIfScheduled(intakeOutCommand);
        cancelIfScheduled(feedInCommand);
        cancelIfScheduled(feedOutCommand);
        cancelIfScheduled(kickerInCommand);
        cancelIfScheduled(kickerOutCommand);
        cancelIfScheduled(shooterCommand);
    }

    private void updateMainOp() {
        boolean currentOptions = mainOp.options;

        if (currentOptions && !previousOptions) {
            resetYawCommand.schedule();
        }
        previousOptions = currentOptions;

        if (mainOp.right_bumper) {
            cancelIfScheduled(intakeInCommand);
            scheduleIfNeeded(intakeOutCommand);
        } else if (mainOp.left_bumper) {
            cancelIfScheduled(intakeOutCommand);
            scheduleIfNeeded(intakeInCommand);
        } else {
            cancelIfScheduled(intakeInCommand);
            cancelIfScheduled(intakeOutCommand);
        }

        if (mainOp.x) {
            cancelIfScheduled((feedOutCommand));
            scheduleIfNeeded(feedInCommand);
        } else if (mainOp.y) {
            cancelIfScheduled(feedInCommand);
            scheduleIfNeeded(feedOutCommand);
        } else {
            cancelIfScheduled(feedInCommand);
            cancelIfScheduled(feedOutCommand);
        }

        if (mainOp.b) {
            cancelIfScheduled(kickerOutCommand);
            scheduleIfNeeded(kickerInCommand);
        } else if (mainOp.a) {
            cancelIfScheduled(kickerInCommand);
            scheduleIfNeeded(kickerOutCommand);
        } else {
            cancelIfScheduled(kickerInCommand);
            cancelIfScheduled(kickerOutCommand);
        }

        updateShooterRPM();
    }

    private void updateShooterRPM() {
        boolean currentDpadUp = mainOp.dpad_up;
        boolean currentDpadDown = mainOp.dpad_down;

        // Ignore opposing inputs; holding either button never repeats a step.
        if (currentDpadUp && !previousDpadUp && !currentDpadDown) {
            selectedShooterRPM = selectedShooterRPM >= SHOOTER_MAX_RPM
                    ? 0.0 : selectedShooterRPM + SHOOTER_RPM_STEP;
        } else if (currentDpadDown && !previousDpadDown && !currentDpadUp) {
            selectedShooterRPM = selectedShooterRPM <= 0.0
                    ? SHOOTER_MAX_RPM : selectedShooterRPM - SHOOTER_RPM_STEP;
        }

        previousDpadUp = currentDpadUp;
        previousDpadDown = currentDpadDown;

        if (selectedShooterRPM > 0.0) {
            scheduleIfNeeded(shooterCommand);
        } else {
            cancelIfScheduled(shooterCommand);
        }
    }

    public double getSelectedShooterRPM() {
        return selectedShooterRPM;
    }

    private void updateSideOp() {

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
