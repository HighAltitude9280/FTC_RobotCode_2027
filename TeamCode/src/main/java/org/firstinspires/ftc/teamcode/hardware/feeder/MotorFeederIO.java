package org.firstinspires.ftc.teamcode.hardware.feeder;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class MotorFeederIO implements FeederIO {
    private final DcMotorEx feederMotor;

    public MotorFeederIO(HardwareMap hardwareMap, String feederMotorName) {
        feederMotor = hardwareMap.get(DcMotorEx.class, feederMotorName);

        feederMotor.setDirection(LowAltitudeConstants.Feeder.DIRECTION);
        feederMotor.setZeroPowerBehavior(LowAltitudeConstants.Feeder.ZERO_POWER_BEHAVIOR);
        feederMotor.setMode(LowAltitudeConstants.Feeder.RUN_MODE);
    }

    @Override
    public void setFeederMotorPower(double feederPower) {
        feederMotor.setPower(feederPower);
    }
}
