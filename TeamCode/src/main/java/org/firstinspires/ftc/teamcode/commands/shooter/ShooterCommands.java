package org.firstinspires.ftc.teamcode.commands.shooter;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

import java.util.function.DoubleSupplier;

public class ShooterCommands {
    private ShooterCommands() {}

    // Mantiene una RPM fija hasta que se cancele o interrumpa el comando.
    public static Command runAtRPM(Shooter shooter, double targetRPM) {
        return Command.build()
                .setStart(() -> shooter.setTargetRPM(targetRPM))
                .setEnd(endCondition -> shooter.stop())
                .requiring(shooter);
    }

    // Lee la RPM objetivo en cada ciclo; se detiene al cancelar o interrumpir.
    public static Command runAtRPM(Shooter shooter, DoubleSupplier targetRPM) {
        return Command.build()
                .setExecute(() -> shooter.setTargetRPM(targetRPM.getAsDouble()))
                .setEnd(endCondition -> shooter.stop())
                .requiring(shooter);
    }
}
