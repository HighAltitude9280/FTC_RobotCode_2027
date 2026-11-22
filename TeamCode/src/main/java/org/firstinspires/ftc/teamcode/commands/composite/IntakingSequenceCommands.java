package org.firstinspires.ftc.teamcode.commands.composite;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.commands.feeder.FeederCommands;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeCommands;
import org.firstinspires.ftc.teamcode.subsystems.feeder.Feeder;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;

public class IntakingSequenceCommands {
    private IntakingSequenceCommands() {}

    public static Command intakingSequence(Feeder feeder, Intake intake) {
        return parallel(
                IntakeCommands.intakeIn(intake),
                FeederCommands.feedIn(feeder));
    }

    public static Command outtakingSequence(Feeder feeder, Intake intake) {
        return parallel(
                IntakeCommands.intakeOut(intake),
                FeederCommands.feedOut(feeder));
    }
}
