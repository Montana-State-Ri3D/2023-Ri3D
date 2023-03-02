package frc.robot.utility;

import static frc.robot.Constants.*;

import frc.robot.subsystems.arm.ArmRealIO;
import frc.robot.subsystems.arm.ArmSimIO;
import frc.robot.subsystems.arm.ArmSubsystem;


public final class SubsystemFactory {

    public static ArmSubsystem createArm(RobotIdentity identity) {
        switch (identity) {
            case SIMULATION:
                return new ArmSubsystem(new ArmSimIO());
            default:
                return new ArmSubsystem(new ArmRealIO(SHOULDER_MOTOR1, SHOULDER_MOTOR2, WRIST_MOTOR, SHOULDER_LIMIT, WRIST_LIMIT));
        }
    }
}
