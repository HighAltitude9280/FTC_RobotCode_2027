package org.firstinspires.ftc.teamcode.commands;

import java.util.Objects;
import org.firstinspires.ftc.teamcode.drive.MecanumDrive;

/** Accion sencilla por ciclo, sin scheduler ni dependencias Android/FTCLib. */
public final class DriveMecanum {
    private final MecanumDrive drive;

    public DriveMecanum(MecanumDrive drive) { this.drive = Objects.requireNonNull(drive); }

    public void execute(double forward, double left, double counterClockwise, boolean enabled) {
        drive.drive(forward, left, counterClockwise, enabled);
    }

    public void end() { drive.stop(); }
}
