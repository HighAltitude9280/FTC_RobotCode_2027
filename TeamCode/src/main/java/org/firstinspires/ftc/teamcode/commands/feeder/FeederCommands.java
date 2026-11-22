package org.firstinspires.ftc.teamcode.commands.feeder;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.subsystems.feeder.Feeder;

public class FeederCommands {
    private FeederCommands() {}

    public static Command feedIn(Feeder feeder) {
        return Command.build()
                .setStart(feeder::feedIn)
                .setEnd(endCondition -> feeder.stop())
                .requiring(feeder);
    }

    public static Command feedOut(Feeder feeder) {
        return Command.build()
                .setStart(feeder::feedOut)
                .setEnd(endCondition -> feeder.stop())
                .requiring(feeder);
    }
}
