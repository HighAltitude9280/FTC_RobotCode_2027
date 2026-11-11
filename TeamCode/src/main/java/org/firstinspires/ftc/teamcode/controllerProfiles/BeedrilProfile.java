package org.firstinspires.ftc.teamcode.controllerProfiles;

import static com.pedropathing.ivy.commands.Commands.instant;

import android.telephony.SmsManager;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.drivetrain.DrivetrainCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.robot.RobotContainer;
public class BeedrilProfile implements ControlProfile {
    private final Gamepad chassisOp;
    private final Gamepad subsystemsOp;

    private final Command driveCommand;
    private final Command resetYawCommand;
    private final Command intakeIn;
    private final Command intakeOut;
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
                this::getChassisDriverForward,
                this::getChassisDriverStrafe,
                this::getChassisDriverTurn
        );

        resetYawCommand = instant(robot.drivetrain::resetYaw);

        intakeIn = IntakeCommands.intakeIn(robot.intake);

        intakeOut = IntakeCommands.intakeOut(robot.intake);

        shooterCommand = ShooterCommands.runAtRPM(
                robot.shooter,
                LowAltitudeConstants.Shooter.TARGET_RPM
        );
    }

    @Override
    public double getChassisDriverForward() {
        return -chassisOp.left_stick_y;
    }

    @Override
    public double getChassisDriverStrafe() {
        return chassisOp.left_stick_x;
    }

    @Override
    public double getChassisDriverTurn() {
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
        cancelIfScheduled(intakeIn);
        cancelIfScheduled(intakeOut);
        cancelIfScheduled(shooterCommand);

    }

    private void updateChassisOp() {
        boolean currentOptions = chassisOp.options;

        if(currentOptions && !previousOptions) {
            resetYawCommand.schedule();
        }

        previousOptions = currentOptions;
    }

    private void updateSubsystemsOp() {
        if(subsystemsOp.right_bumper) {
            cancelIfScheduled(intakeIn);
            scheduleIfNeeded(intakeOut);
        } else if (subsystemsOp.left_bumper) {
            cancelIfScheduled(intakeOut);
            scheduleIfNeeded(intakeIn);
        } else {
            cancelIfScheduled(intakeIn);
            cancelIfScheduled(intakeOut);
        }

        if(subsystemsOp.a) {
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