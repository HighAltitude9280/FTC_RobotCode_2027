package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.MecanumDrivetrainIO;
import org.firstinspires.ftc.teamcode.hardware.rollers.IntakeRollersIO;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;

public class RobotContainer {

    public final Drivetrain drivetrain;
    public final Intake intake;

    public RobotContainer(
            HardwareMap hardwareMap
    ) {
        MecanumDrivetrainIO drivetrainIO =
                new MecanumDrivetrainIO(
                        hardwareMap,
                        LowAltitudeConstants
                                .DRIVETRAIN
                                .FRONT_LEFT_NAME,
                        LowAltitudeConstants
                                .DRIVETRAIN
                                .FRONT_RIGHT_NAME,
                        LowAltitudeConstants
                                .DRIVETRAIN
                                .BACK_LEFT_NAME,
                        LowAltitudeConstants
                                .DRIVETRAIN
                                .BACK_RIGHT_NAME,
                        LowAltitudeConstants
                                .DRIVETRAIN
                                .IMU_NAME
                );
        IntakeRollersIO intakeRollersIO = new IntakeRollersIO(hardwareMap, LowAltitudeConstants.INTAKE.INTAKE_NAME);
        drivetrain =
                new Drivetrain(drivetrainIO);
        intake =
                new Intake(intakeRollersIO);
    }
}