package org.firstinspires.ftc.teamcode.constants;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.hardware.kicker.ServoKickerIO;

public final class LowAltitudeConstants {
    public static final class Drivetrain {

        public static final String FRONT_LEFT_NAME = "frontLeft";
        public static final String FRONT_RIGHT_NAME = "frontRight";
        public static final String BACK_LEFT_NAME = "backLeft";
        public static final String BACK_RIGHT_NAME = "backRight";
        public static final String IMU_NAME = "imu";

        public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode RUN_MODE = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        public static final DcMotorSimple.Direction LEFT_DIRECTION =
                DcMotorSimple.Direction.REVERSE;
        public static final DcMotorSimple.Direction RIGHT_DIRECTION =
                DcMotorSimple.Direction.FORWARD;

        public static final double CHASSIS_SPEED_MULTIPLIER = 1.0;

        private Drivetrain() {}
    }

    public static final class Intake {
        public static final String MOTOR_NAME = "intake";
        public static final double IN_POWER = 0.6;
        public static final double OUT_POWER = -0.6;
        public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode RUN_MODE = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        public static final DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;

        private Intake() {}
    }

    public static final class Feeder {
        public static final String MOTOR_NAME = "feeder";
        public static final double IN_POWER = 0.9;
        public static final double OUT_POWER = -0.9;
        public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode RUN_MODE = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
        public static final DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;

        private Feeder() {}
    }

    @Configurable
    public static final class Shooter {
        public static final String MOTOR_NAME = "shooter";
        public static final double TARGET_RPM = 4000.0;
        public static final DcMotor.ZeroPowerBehavior ZERO_POWER_BEHAVIOR =
                DcMotor.ZeroPowerBehavior.BRAKE;
        public static final DcMotor.RunMode RUN_MODE = DcMotor.RunMode.RUN_USING_ENCODER;
        public static final DcMotorSimple.Direction DIRECTION = DcMotorSimple.Direction.FORWARD;
        public static final double TICKS_PER_REV = 28.0;

        public static final double RPM_TOLERANCE = 75.0;

        public static double kP = 30.0;
        public static double kI = 0.0;
        public static double kD = 0.7;
        public static double kF = 12.0;

        private Shooter() {}
    }

    public static final class Kicker {
        public static final String SERVO_NAME = "kicker";
        public static final CRServo.Direction DIRECTION = CRServo.Direction.FORWARD;
        public static final double IN_POWER = 0.75;
        public static final double OUT_POWER = -0.75;

        private Kicker() {}
    }
}
