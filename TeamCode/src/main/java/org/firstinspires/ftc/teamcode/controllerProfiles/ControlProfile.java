package org.firstinspires.ftc.teamcode.controllerProfiles;

import org.firstinspires.ftc.teamcode.robot.RobotContainer;

public interface ControlProfile {
    double getDriverStrafe();
    double getDriverForward();
    double getDriverTurn();
    void start();
    void update();
    void stop();
    void configureButtonBinding(RobotContainer robot);
}
