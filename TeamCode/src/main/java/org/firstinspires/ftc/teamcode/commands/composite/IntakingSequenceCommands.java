package org.firstinspires.ftc.teamcode.commands.composite;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.commands.kicker.KickerCommands;
import org.firstinspires.ftc.teamcode.subsystems.feeder.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.kicker.Kicker;

public class IntakingSequenceCommands {
    private IntakingSequenceCommands() {}

    public static Command intakingSequence(Feeder feeder, Intake intake, Kicker kicker) {
        return parallel(
                IntakeCommands.intakeIn(intake),
                FeederCommands.feedIn(feeder),
                KickerCommands.kickOut(kicker));
    }

    public static Command outtakingSequence(Feeder feeder, Intake intake, Kicker kicker) {
        return parallel(
                IntakeCommands.intakeOut(intake),
                FeederCommands.feedOut(feeder),
                KickerCommands.kickOut(kicker));
    }
}
