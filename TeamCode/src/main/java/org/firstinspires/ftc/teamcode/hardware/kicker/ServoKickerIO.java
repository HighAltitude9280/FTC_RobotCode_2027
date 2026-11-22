package org.firstinspires.ftc.teamcode.hardware.kicker;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.constants.LowAltitudeConstants;

public class ServoKickerIO implements KickerIO {
    private final CRServo kickerServo;

    public ServoKickerIO(HardwareMap hardwareMap, String kickerServoName) {
        kickerServo = hardwareMap.get(CRServo.class, kickerServoName);

        kickerServo.setDirection(LowAltitudeConstants.Kicker.DIRECTION);
    }
    @Override
    public void setKickerServoPower(double kickerPower) { kickerServo.setPower(kickerPower); }
}
