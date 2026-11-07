package org.firstinspires.ftc.teamcode.commands.shooter;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class ShooterCommands {
    private ShooterCommands() {}

    public static Command runAtRPM(Shooter shooter, double targetRPM) {
        return Command.build()
                .setStart(() -> shooter.setTargetRPM(targetRPM))
                .setEnd(endCondition -> shooter.stop())
                .requiring(shooter);
    }

    public static Command tuneVelocity(Shooter shooter) {
        return Command.build()
                .setExecute(() -> shooter.setTargetRPM(LowAltitudeConstants.Shooter.TARGET_RPM))
                .setEnd(endCondition -> shooter.stop())
                .requiring(shooter);
    }
}
