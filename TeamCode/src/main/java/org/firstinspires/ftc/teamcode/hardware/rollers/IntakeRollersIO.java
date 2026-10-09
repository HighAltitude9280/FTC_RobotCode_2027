package org.firstinspires.ftc.teamcode.hardware.rollers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class IntakeRollersIO implements RollersIO {
    private final DcMotorEx intakeRollerMotor;

    public IntakeRollersIO(HardwareMap hardwareMap, String intakeRollerMotorName) {
        intakeRollerMotor = hardwareMap.get(DcMotorEx.class, intakeRollerMotorName);

        intakeRollerMotor.setDirection(
                LowAltitudeConstants
                        .INTAKE
                        .INTAKE_DIRECTION);
        intakeRollerMotor.setZeroPowerBehavior(
                LowAltitudeConstants
                .INTAKE
                .INTAKE_ZERO_POWER_BEHAVIOR);
        intakeRollerMotor.setMode(
                LowAltitudeConstants
                        .INTAKE
                        .INTAKE_RUN_MODE);
    }

    @Override
    public void setRollerMotorPower(double rollerPower) {
        intakeRollerMotor.setPower(rollerPower);
    }
}