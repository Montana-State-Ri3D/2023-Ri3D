package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveCommand;
import frc.robot.subsystems.DriveTrainSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

import static frc.robot.Constants.*;

public class RobotContainer {

  // Creating Controlers
  private final XboxController driveController = new XboxController(Constants.DRIVE_CONTROLLER_PORT);
  private final XboxController operatorController = new XboxController(Constants.OPERATOR_CONTROLLER_PORT);
  private final XboxController testController = new XboxController(Constants.TEST_CONTROLLER_PORT);

  private DriveTrainSubsystem driveTrainSubsystem;

  private DriveCommand driveCommand;

  public RobotContainer() {
    createSubsystems(); // Create our subsystems.
    createCommands(); // Create our commands
    configureButtonBindings(); // Configure the button bindings
  }

  private void createSubsystems() {
    driveTrainSubsystem = new DriveTrainSubsystem(LEFT_FRONT_MOTOR, LEFT_BACK_MOTOR, RIGHT_FRONT_MOTOR,
        RIGHT_BACK_MOTOR);
  }

  private void createCommands() {
    driveCommand = new DriveCommand(driveTrainSubsystem, () -> driveController.getLeftY(),
        () -> driveController.getRightX());
    driveTrainSubsystem.setDefaultCommand(driveCommand);
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
