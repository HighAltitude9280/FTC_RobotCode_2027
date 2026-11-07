package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;
import org.firstinspires.ftc.teamcode.hardware.drivetrain.MecanumDrivetrainIO;
import org.firstinspires.ftc.teamcode.hardware.rollers.MotorRollerIO;
import org.firstinspires.ftc.teamcode.hardware.shooter.MotorShooterIO;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.intake.Intake;
import org.firstinspires.ftc.teamcode.subsystems.shooter.Shooter;

public class RobotContainer {

    public final Drivetrain drivetrain;
    public final Intake intake;
    public final Shooter shooter;

    public RobotContainer(HardwareMap hardwareMap) {
        MecanumDrivetrainIO drivetrainIO =
                new MecanumDrivetrainIO(
                        hardwareMap,
                        LowAltitudeConstants.Drivetrain.FRONT_LEFT_NAME,
                        LowAltitudeConstants.Drivetrain.FRONT_RIGHT_NAME,
                        LowAltitudeConstants.Drivetrain.BACK_LEFT_NAME,
                        LowAltitudeConstants.Drivetrain.BACK_RIGHT_NAME,
                        LowAltitudeConstants.Drivetrain.IMU_NAME);

        MotorRollerIO motorRollerIO =
                new MotorRollerIO(hardwareMap, LowAltitudeConstants.Intake.MOTOR_NAME);

        MotorShooterIO motorShooterIO =
                new MotorShooterIO(hardwareMap, LowAltitudeConstants.Shooter.MOTOR_NAME);

        drivetrain = new Drivetrain(drivetrainIO);
        intake = new Intake(motorRollerIO);
        shooter = new Shooter(motorShooterIO);
    }
}
