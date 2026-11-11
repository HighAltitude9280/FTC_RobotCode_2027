package org.firstinspires.ftc.teamcode.commands.drivetrain;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;

import java.util.function.DoubleSupplier;

public final class DrivetrainCommands {
    private DrivetrainCommands() {}

    public static Command driveRobotCentric(
            Drivetrain drivetrain,
            DoubleSupplier forward,
            DoubleSupplier strafe,
            DoubleSupplier rotation) {
        return infinite(
                        () ->
                                drivetrain.driveRobotCentric(
                                        forward.getAsDouble(),
                                        strafe.getAsDouble(),
                                        rotation.getAsDouble()))
                .setEnd(endCondition -> drivetrain.stop())
                .requiring(drivetrain);
    }

    public static Command driveFieldCentric(
            Drivetrain drivetrain,
            DoubleSupplier forward,
            DoubleSupplier strafe,
            DoubleSupplier rotation) {
        return infinite(
                () ->
                        drivetrain.driveFieldCentric(
                                forward.getAsDouble(),
                                strafe.getAsDouble(),
                                rotation.getAsDouble()))
                .setEnd(endCondition -> drivetrain.stop())
                .requiring(drivetrain);
    }
}
