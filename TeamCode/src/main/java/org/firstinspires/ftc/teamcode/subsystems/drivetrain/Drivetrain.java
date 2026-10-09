package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import org.firstinspires.ftc.teamcode.hardware.drivetrain.DriveTrainIO;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.DriveTrainIOInputs;

public class Drivetrain {

    private final DriveTrainIO io;

    private final DriveTrainIOInputs inputs =
            new DriveTrainIOInputs();

    public Drivetrain(DriveTrainIO io) {
        this.io = io;
    }

    public void driveRobotCentric(
            double forward,
            double strafe,
            double rotation
    ) {

        double frontLeftPower = forward + strafe + rotation;

        double frontRightPower = forward - strafe - rotation;

        double backLeftPower = forward - strafe + rotation;

        double backRightPower = forward + strafe - rotation;


        double maxMagnitude =
                Math.max(
                        1.0,
                        Math.max(
                                Math.max(
                                        Math.abs(frontLeftPower),
                                        Math.abs(frontRightPower)
                                ),
                                Math.max(
                                        Math.abs(backLeftPower),
                                        Math.abs(backRightPower)
                                )
                        )
                );


        frontLeftPower /= maxMagnitude;
        frontRightPower /= maxMagnitude;
        backLeftPower /= maxMagnitude;
        backRightPower /= maxMagnitude;


        io.setMotorPowers(
                frontLeftPower,
                frontRightPower,
                backLeftPower,
                backRightPower
        );
    }

    public void stop() {
        io.stop();
    }

    public void periodic() {
        io.updateInputs(inputs);
    }

    public double getHeadingRadians() {
        return inputs.headingRadians;
    }
    public double getHeadingDegrees() { return inputs.headingDegrees; }
}