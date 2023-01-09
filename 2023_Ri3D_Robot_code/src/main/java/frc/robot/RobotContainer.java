package frc.robot;

import frc.robot.commands.Ddown;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.InitArm;
import frc.robot.commands.Ddown;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Mode;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static frc.robot.Constants.*;

public class RobotContainer {

  // Creating Controlers
  @SuppressWarnings({ "unused" })
  private final XboxController driveController = new XboxController(Constants.DRIVE_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final XboxController operatorController = new XboxController(Constants.OPERATOR_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final XboxController testController = new XboxController(Constants.TEST_CONTROLLER_PORT);

  private DriveTrainSubsystem driveTrainSubsystem;
  private IntakeSubsystem intakeSubsystem;
  private ArmSubsystem arm;
  private Mode mode;

  private SequentialCommandGroup ejectItem;

  private SequentialCommandGroup intakeCone;
  private SequentialCommandGroup intakeCube;

  private DriveCommand driveCommand;
  private InitArm initArm;

  public RobotContainer() {
    createSubsystems(); // Create our subsystems.
    createCommands(); // Create our commands
    configureButtonBindings(); // Configure the button bindings
  }

  private void createSubsystems() { 
    mode = new Mode();

    driveTrainSubsystem = new DriveTrainSubsystem(LEFT_FRONT_MOTOR,
        LEFT_BACK_MOTOR, RIGHT_FRONT_MOTOR, RIGHT_BACK_MOTOR);
    intakeSubsystem = new IntakeSubsystem(INTAKE_LEFT_MOTOR, INTAKE_RIGHT_MOTOR,
        FRONT_BEAM_BRAKE, BACK_BEAM_BRAKE);

    arm = new ArmSubsystem(BASE_MOTOR1, BASE_MOTOR2, WRIST_MOTOR, BASE_LIMIT, WRIST_LIMIT);
  }

  private void createCommands() {
    initArm = new InitArm(arm);

    driveCommand = new DriveCommand(driveTrainSubsystem,
        () -> driveController.getLeftTriggerAxis() - driveController.getRightTriggerAxis(),
        () -> driveController.getLeftX());
    driveTrainSubsystem.setDefaultCommand(driveCommand);

    ejectItem = new SequentialCommandGroup();
    ejectItem.addCommands(new InstantCommand(() -> intakeSubsystem.intakePower(-1)));
    ejectItem.addCommands(new WaitCommand(0.5));
    ejectItem.addCommands(new InstantCommand(() -> intakeSubsystem.intakePower(0)));

    intakeCone = new SequentialCommandGroup();
    intakeCone.addCommands(new InstantCommand(() -> mode.setMode(1)));
    intakeCone.addCommands(new IntakeCommand(intakeSubsystem, 1, () -> driveController.getYButton()));

    intakeCube = new SequentialCommandGroup();
    intakeCube.addCommands(new InstantCommand(() -> mode.setMode(0)));
    intakeCube.addCommands(new IntakeCommand(intakeSubsystem, 0, () -> driveController.getYButton()));
  }

  private void configureButtonBindings() {

    // Calls Dup to move Arm Up
    new POVButton(driveController, 0).whenPressed(new Ddown(mode, arm));

    // Calls Ddown to move Arm Down
    new POVButton(driveController, 180).whenPressed(new Ddown(mode, arm));

    // Toggle Brake Mode with A
    new JoystickButton(driveController, Button.kA.value)
        .whenPressed(new InstantCommand(() -> driveTrainSubsystem.toggleMode(), driveTrainSubsystem));

    // Eject Item with X
    new JoystickButton(driveController, Button.kX.value).whenPressed(ejectItem);

    // Intake Cone
    new JoystickButton(driveController, Button.kRightBumper.value).whenPressed(intakeCone);

    new JoystickButton(driveController, Button.kLeftBumper.value).whenPressed(intakeCube);

    new JoystickButton(driveController, Button.kStart.value).whenPressed(initArm);

  }

  public Command getAutonomousCommand() {
    return null;
  }
}
