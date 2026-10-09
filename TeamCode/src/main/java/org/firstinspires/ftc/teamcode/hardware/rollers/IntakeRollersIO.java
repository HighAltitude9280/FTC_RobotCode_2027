package org.firstinspires.ftc.teamcode.hardware.rollers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeRollersIO implements RollersIO {
    private final DcMotorEx intakeRollerMotor;

    public IntakeRollersIO(HardwareMap hardwareMap, String intakeRollerMotorName) {
        intakeRollerMotor = hardwareMap.get(DcMotorEx.class, intakeRollerMotorName);

        intakeRollerMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeRollerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void setRollerMotorPower(double rollerPower) {
        intakeRollerMotor.setPower(rollerPower);
    }
}