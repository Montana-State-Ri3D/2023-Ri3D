package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import static frc.robot.Constants.*;

public class RobotContainer {

  // Creating Controlers
  @SuppressWarnings({ "unused" })
  private final XboxController driveController = new XboxController(Constants.DRIVE_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final XboxController operatorController = new XboxController(Constants.OPERATOR_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final XboxController testController = new XboxController(Constants.TEST_CONTROLLER_PORT);

  // private DriveTrainSubsystem driveTrainSubsystem;

  // private DriveCommand driveCommand;

  private IntakeSubsystem intakeSubsystem;

  public RobotContainer() {
    createSubsystems(); // Create our subsystems.
    createCommands(); // Create our commands
    configureButtonBindings(); // Configure the button bindings
  }

  private void createSubsystems() {
    // driveTrainSubsystem = new DriveTrainSubsystem(LEFT_FRONT_MOTOR,
    // LEFT_BACK_MOTOR, RIGHT_FRONT_MOTOR,RIGHT_BACK_MOTOR);
    intakeSubsystem = new IntakeSubsystem(INTAKE_LEFT_MOTOR, INTAKE_RIGHT_MOTOR, FRONT_BEAM_BRAKE, BACK_BEAM_BRAKE);
  }

  private void createCommands() {
    // driveCommand = new DriveCommand(driveTrainSubsystem, () ->
    // driveController.getLeftY(),() -> driveController.getRightX());
    // driveTrainSubsystem.setDefaultCommand(driveCommand);
  }

  private void configureButtonBindings() {

    // Map Toggle To A
    // new JoystickButton(driveController, Button.kA.value).whenPressed(new
    // InstantCommand(() -> driveTrainSubsystem.toggleMode(), driveTrainSubsystem));
    // Map Brake Mode to B
    // new JoystickButton(driveController, Button.kB.value).whenPressed(new
    // InstantCommand(() -> driveTrainSubsystem.setBrake(), driveTrainSubsystem));
    // Map Coast Mode to X
    // new JoystickButton(driveController, Button.kX.value).whenPressed(new
    // InstantCommand(() -> driveTrainSubsystem.setCoast(), driveTrainSubsystem));
    //
    new JoystickButton(driveController, Button.kLeftBumper.value).whenPressed(new IntakeCommand(intakeSubsystem, 1));

    new JoystickButton(driveController, Button.kRightBumper.value).whenPressed(new IntakeCommand(intakeSubsystem, 0));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
