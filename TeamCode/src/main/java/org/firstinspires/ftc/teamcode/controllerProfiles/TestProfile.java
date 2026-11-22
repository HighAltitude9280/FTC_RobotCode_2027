package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import android.telephony.SmsManager;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public class TestProfile implements ControlProfile {
    private final Gamepad mainOp;
    private final Gamepad sideOp;

    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakeInCommand;
    private final Command intakeOutCommand;
    private final Command feedInCommand;
    private final Command feedOutCommand;
    private final Command shooterCommand;

    private boolean previousOptions = false;

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

        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                LowAltitudeConstants.Shooter.TARGET_RPM
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
        } else if (mainOp.b) {
            cancelIfScheduled(feedInCommand);
            scheduleIfNeeded(feedOutCommand);
        } else {
            cancelIfScheduled(feedInCommand);
            cancelIfScheduled(feedOutCommand);
        }

        if (mainOp.a) {
            scheduleIfNeeded(shooterCommand);
        } else {
            cancelIfScheduled(shooterCommand);
        }
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
