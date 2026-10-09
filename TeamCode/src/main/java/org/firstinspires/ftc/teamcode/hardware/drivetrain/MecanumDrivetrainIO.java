package org.firstinspires.ftc.teamcode.hardware.drivetrain;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class MecanumDrivetrainIO implements DriveTrainIO {

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;

    private final IMU imu;

    public MecanumDrivetrainIO(
            HardwareMap hardwareMap,
            String frontLeftName,
            String frontRightName,
            String backLeftName,
            String backRightName,
            String imuName
    ) {
        frontLeft =
                hardwareMap.get(DcMotorEx.class, frontLeftName);

        frontRight =
                hardwareMap.get(DcMotorEx.class, frontRightName);

        backLeft =
                hardwareMap.get(DcMotorEx.class, backLeftName);

        backRight =
                hardwareMap.get(DcMotorEx.class, backRightName);

        imu =
                hardwareMap.get(IMU.class, imuName);


        // Configura la dirección de giro de cada motor según su orientación física en el drivetrain.
        frontLeft.setDirection(
                LowAltitudeConstants.DRIVETRAIN.LEFT_DIRECTION);
        frontRight.setDirection(
                LowAltitudeConstants.DRIVETRAIN.RIGHT_DIRECTION);
        backLeft.setDirection(
                LowAltitudeConstants.DRIVETRAIN.LEFT_DIRECTION);
        backRight.setDirection(
                LowAltitudeConstants.DRIVETRAIN.RIGHT_DIRECTION);

        // Define el comportamiento de los motores al recibir potencia cero: BRAKE
        // para frenado activo o FLOAT para giro libre.
        frontLeft.setZeroPowerBehavior(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_ZERO_POWER_BEHAVIOR);
        frontRight.setZeroPowerBehavior(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_ZERO_POWER_BEHAVIOR);
        backLeft.setZeroPowerBehavior(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_ZERO_POWER_BEHAVIOR);
        backRight.setZeroPowerBehavior(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_ZERO_POWER_BEHAVIOR);

        // Establece el modo de operación de los motores: RUN_WITHOUT_ENCODER para control
        // directo por potencia; puede sustituirse por otros modos según se requiera control
        // por velocidad, posición o reinicio de encoder.
        frontLeft.setMode(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_RUN_MODE);
        frontRight.setMode(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_RUN_MODE);
        backLeft.setMode(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_RUN_MODE);
        backRight.setMode(
                LowAltitudeConstants.DRIVETRAIN.DRIVETRAIN_RUN_MODE);

        IMU.Parameters parameters =
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot
                                        .LogoFacingDirection.UP,

                                RevHubOrientationOnRobot
                                        .UsbFacingDirection.LEFT
                        )
                );

        imu.initialize(parameters);
        imu.resetYaw();
    }

    @Override
    public void updateInputs(
            DriveTrainIOInputs inputs
    ) {
        inputs.headingRadians =
                imu.getRobotYawPitchRollAngles()
                        .getYaw(AngleUnit.RADIANS);
        inputs.headingDegrees =
                imu.getRobotYawPitchRollAngles()
                        .getYaw(AngleUnit.DEGREES);
    }


    @Override
    public void setMotorPowers(
            double frontLeftPower,
            double frontRightPower,
            double backLeftPower,
            double backRightPower
    ) {
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
}