package org.firstinspires.ftc.teamcode.constants;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.bylazar.configurables.annotations.Configurable;

public final class LowAltitudeConstants {
    public static final class DRIVETRAIN {

        // Nombres de motor e IMU
        public static final String FRONT_LEFT_NAME = "frontLeft";
        public static final String FRONT_RIGHT_NAME = "frontRight";
        public static final String BACK_LEFT_NAME = "backLeft";
        public static final String BACK_RIGHT_NAME = "backRight";
        public static final String IMU_NAME = "imu";

        // Configuración de motores
        public static final DcMotor.ZeroPowerBehavior DRIVETRAIN_ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode DRIVETRAIN_RUN_MODE =
                DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        public static final DcMotorSimple.Direction LEFT_DIRECTION =
                DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction RIGHT_DIRECTION =
                DcMotorSimple.Direction.FORWARD;



        private DRIVETRAIN() {}
    }

    public static final class INTAKE {
        public static final String INTAKE_NAME = "intake";
        public static final double INTAKE_IN_POWER = 0.5;
        public static final double INTAKE_OUT_POWER = -0.5;
        public static final DcMotor.ZeroPowerBehavior INTAKE_ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode INTAKE_RUN_MODE =
                DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        public static final DcMotorSimple.Direction INTAKE_DIRECTION =
                DcMotorSimple.Direction.FORWARD;

        private INTAKE() {}
    }

    @Configurable
    public static final class SHOOTER {
        public static final String SHOOTER_NAME = "shooter";
        public static final double SHOOT_RPM = 4000.0;
        public static final DcMotor.ZeroPowerBehavior SHOOTER_ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode SHOOTER_RUN_MODE =
                DcMotor.RunMode.RUN_USING_ENCODER;
        public static final DcMotorSimple.Direction SHOOTER_DIRECTION =
                DcMotorSimple.Direction.FORWARD;
        public static final double SHOOTER_TICKS_PER_REV = 28.0;

        public static final double RPM_TOLERANCE = 75.0;

        public static double SHOOTER_P = 5.0;
        public static double SHOOTER_I = 0.0;
        public static double SHOOTER_D = 0.0;
        public static double SHOOTER_F = 10.0;

        private SHOOTER() {}
    }
}