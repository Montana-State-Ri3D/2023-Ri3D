package frc.robot.utility;

import static frc.robot.Constants.*;

import frc.robot.subsystems.arm.ArmRealIO;
import frc.robot.subsystems.arm.ArmSimIO;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.drivetrain.DriveTrainRealIO;
import frc.robot.subsystems.drivetrain.DriveTrainSimIO;


public final class SubsystemFactory {

    public static ArmSubsystem createArm(RobotIdentity identity) {
        switch (identity) {
            case SIMULATION:
                return new ArmSubsystem(new ArmSimIO());
            default:
                return new ArmSubsystem(new ArmRealIO(SHOULDER_MOTOR1, SHOULDER_MOTOR2, WRIST_MOTOR, SHOULDER_LIMIT, WRIST_LIMIT));
        }
    }
    public static DriveTrain createDriveTrain(RobotIdentity identity) {
        switch (identity) {
            case SIMULATION:
                return new DriveTrain(new DriveTrainSimIO());
            default:
                return new DriveTrain(new DriveTrainRealIO(LEFT_FRONT_MOTOR, LEFT_BACK_MOTOR, RIGHT_FRONT_MOTOR,RIGHT_BACK_MOTOR));
        }
    }
}
