package org.firstinspires.ftc.teamcode.hardware.rollers;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class MotorRollerIO implements RollerIO {
    private final DcMotorEx intakeRollerMotor;

    public MotorRollerIO(HardwareMap hardwareMap, String intakeRollerMotorName) {
        intakeRollerMotor = hardwareMap.get(DcMotorEx.class, intakeRollerMotorName);

        intakeRollerMotor.setDirection(LowAltitudeConstants.Intake.DIRECTION);
        intakeRollerMotor.setZeroPowerBehavior(LowAltitudeConstants.Intake.ZERO_POWER_BEHAVIOR);
        intakeRollerMotor.setMode(LowAltitudeConstants.Intake.RUN_MODE);
    }

    @Override
    public void setRollerMotorPower(double rollerPower) {
        intakeRollerMotor.setPower(rollerPower);
    }
}
