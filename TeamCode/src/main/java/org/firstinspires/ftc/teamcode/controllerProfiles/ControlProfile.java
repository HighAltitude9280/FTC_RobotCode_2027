package org.firstinspires.ftc.teamcode.controllerProfiles;

import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public interface ControlProfile {
    double getChassisDriverStrafe();
    double getChassisDriverForward();
    double getChassisDriverTurn();
    void start();
    void update();
    void stop();
    void configureButtonBinding(RobotContainer robot);
}
