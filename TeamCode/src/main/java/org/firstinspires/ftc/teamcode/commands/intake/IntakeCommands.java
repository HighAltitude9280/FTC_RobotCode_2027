package org.firstinspires.ftc.teamcode.commands.intake;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;

public final class IntakeCommands {
    private IntakeCommands() {}

    public static Command intakeIn(Intake intake) {
        return Command.build()
                .setStart(intake::intakeIn)
                .setEnd(endCondition -> intake.stop())
                .requiring(intake);
    }

    public  static  Command intakeOut(Intake intake) {
        return Command.build()
                .setStart(intake::intakeOut)
                .setEnd(endCondition -> intake.stop())
                .requiring(intake);
    }
}