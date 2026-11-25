package org.firstinspires.ftc.teamcode.commands.composite;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.subsystems.feeder.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShootingSequenceCommands {
    private ShootingSequenceCommands() {}

    public static Command shootingSequence(Feeder feeder, Intake intake, Shooter shooter) {
        return parallel(
                IntakeCommands.intakeIn(intake),
                FeederCommands.feedIn(feeder),
                ShooterCommands.runAtRPM(
                        shooter,
                        () -> LowAltitudeConstants.Shooter.TARGET_RPM
                ));

    }
}
