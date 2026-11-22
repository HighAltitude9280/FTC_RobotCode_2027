package org.firstinspires.ftc.teamcode.hardware.intake;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class MotorIntakeIO implements IntakeIO {
    private final DcMotorEx intakeMotor;

    public MotorIntakeIO(HardwareMap hardwareMap, String intakeMotorName) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, intakeMotorName);

        intakeMotor.setDirection(LowAltitudeConstants.Intake.DIRECTION);
        intakeMotor.setZeroPowerBehavior(LowAltitudeConstants.Intake.ZERO_POWER_BEHAVIOR);
        intakeMotor.setMode(LowAltitudeConstants.Intake.RUN_MODE);
    }

    @Override
    public void setIntakeMotorPower(double intakePower) {
        intakeMotor.setPower(intakePower);
    }
}
