package org.firstinspires.ftc.teamcode.commands.kicker;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.subsystems.kicker.Kicker;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class KickerCommands {
    private KickerCommands() {}

    public static Command kickIn(Kicker kicker) {
        return Command.build()
                .setStart(kicker::kickIn)
                .setEnd(endCondition -> kicker.stop())
                .requiring(kicker);
    }

    public static Command kickOut(Kicker kicker) {
        return Command.build()
                .setStart(kicker::kickOut)
                .setEnd(endCondition -> kicker.stop())
                .requiring(kicker);
    }

    public static Command kickInWhenAtTargetRPM(Kicker kicker, Shooter shooter) {
        return Command.build()
                .setExecute(() -> {
                    if (shooter.getTargetRPM() > 0.0 && shooter.isAtTargetRPM()) {
                        kicker.kickIn();
                    } else {
                        kicker.stop();
                    }
                })
                .setEnd(endCondition -> kicker.stop())
                .requiring(kicker);
    }
}
