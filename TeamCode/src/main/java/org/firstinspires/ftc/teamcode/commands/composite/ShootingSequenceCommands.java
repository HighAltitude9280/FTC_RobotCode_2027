package org.firstinspires.ftc.teamcode.commands.composite;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.kicker.KickerCommands;
import org.firstinspires.ftc.teamcode.commands.shooter.ShooterCommands;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.subsystems.feeder.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.kicker.Kicker;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShootingSequenceCommands {
    private ShootingSequenceCommands() {}

    public static Command feedWhenReady(
            Feeder feeder,
            Intake intake,
            Kicker kicker,
            Shooter shooter
    ) {
        return Command.build()
                .setExecute(() -> {
                    boolean shooterReady = shooter.getTargetRPM() > 0.0
                            && shooter.isAtTargetRPM();

                    intake.intakeIn();
                    feeder.feedIn();

                    if (shooterReady) {
                        kicker.kickIn();
                    } else {
                        kicker.stop();
                    }
                })
                .setEnd(endCondition -> {
                    intake.stop();
                    feeder.stop();
                    kicker.stop();
                })
                .requiring(intake, feeder, kicker);
    }

    public static Command shootingSequence(
            Feeder feeder,
            Intake intake,
            Shooter shooter,
            Kicker kicker
    ) {
        return parallel(
                IntakeCommands.intakeIn(intake),
                FeederCommands.feedIn(feeder),
                ShooterCommands.runAtRPM(
                        shooter,
                        () -> LowAltitudeConstants.Shooter.TARGET_RPM
                ),
                KickerCommands.kickInWhenAtTargetRPM(kicker, shooter));

    }
}
