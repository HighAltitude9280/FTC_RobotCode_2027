package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Encoder Test", group = "Test")
public class EncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        String[] names = {"lf", "rf", "lr", "rr", "feeder"};
        DcMotorEx[] motors = new DcMotorEx[names.length];
        for (int i = 0; i < names.length; i++) {
            motors[i] = hardwareMap.get(DcMotorEx.class, names[i]);
        }

        waitForStart();
        while (opModeIsActive()) {
            for (int i = 0; i < names.length; i++) {
                telemetry.addData(names[i], motors[i].getCurrentPosition());
            }
            telemetry.update();
        }
    }
}