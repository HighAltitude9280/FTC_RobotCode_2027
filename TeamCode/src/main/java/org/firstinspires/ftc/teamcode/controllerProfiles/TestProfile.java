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
    private final Gamepad mainOP;
    private final Gamepad sideOP;

    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakeIn;
    private final Command intakeOut;
    private final Command feedIn;
    private final Command feedOut;
    private final Command shooterCommand;

    private boolean previousOptions = false;

    public TestProfile(
            Gamepad gamepad1,
            Gamepad gamepad2,
            RobotContainer robot
    ) {
        this.mainOP = gamepad1;
        this.sideOP = gamepad2;

        driveCommand = DrivetrainCommands.driveFieldCentric(
                robot.drivetrain,
                this::getDriverForward,
                this::getDriverStrafe,
                this::getDriverTurn
        );

        resetYawCommand = instant(robot.drivetrain::resetYaw);

        intakeIn = IntakeCommands.intakeIn(robot.intake);

        intakeOut = IntakeCommands.intakeOut(robot.intake);

        feedIn = FeederCommands.feedIn(robot.feeder);

        feedOut = FeederCommands.feedOut(robot.feeder);

        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                LowAltitudeConstants.Shooter.TARGET_RPM
        );
    }

    @Override
    public double getDriverForward() {
        return -mainOP.left_stick_y;
    }

    @Override
    public double getDriverStrafe() {
        return mainOP.left_stick_x;
    }

    @Override
    public double getDriverTurn() {
        return mainOP.right_stick_x;
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
        cancelIfScheduled(intakeIn);
        cancelIfScheduled(intakeOut);
        cancelIfScheduled(feedIn);
        cancelIfScheduled(feedOut);
        cancelIfScheduled(shooterCommand);
    }

    private void updateMainOp() {
        boolean currentOptions = mainOP.options;

        if(currentOptions && !previousOptions) {
            resetYawCommand.schedule();
        }
        previousOptions = currentOptions;

        if(mainOP.right_bumper) {
            cancelIfScheduled(intakeIn);
            scheduleIfNeeded(intakeOut);
        } else if (mainOP.left_bumper) {
            cancelIfScheduled(intakeOut);
            scheduleIfNeeded(intakeIn);
        } else {
            cancelIfScheduled(intakeIn);
            cancelIfScheduled(intakeOut);
        }

        if(mainOP.x) {
            cancelIfScheduled((feedOut));
            scheduleIfNeeded(feedIn);
        } else if(mainOP.b) {
            cancelIfScheduled(feedIn);
            scheduleIfNeeded(feedOut);
        } else {
            cancelIfScheduled(feedIn);
            cancelIfScheduled(feedOut);
        }

        if(mainOP.a) {
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
