package frc.robot;

import frc.robot.commands.ArmManual;
import frc.robot.commands.Dleft;
import frc.robot.commands.Dright;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.Dup;
import frc.robot.commands.InitArm;
import frc.robot.commands.Ddown;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.DriveTrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Mode;
import frc.robot.subsystems.Mode.Type;
import frc.robot.subsystems.arm.RealArm;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import static frc.robot.Constants.*;

public class RobotContainer {

  // Creating Controlers
  @SuppressWarnings({ "unused" })
  private final CommandXboxController driveController = new CommandXboxController(Constants.DRIVE_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final CommandXboxController operatorController = new CommandXboxController(
      Constants.OPERATOR_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final CommandXboxController testController = new CommandXboxController(Constants.TEST_CONTROLLER_PORT);

  private DriveTrainSubsystem driveTrainSubsystem;
  private IntakeSubsystem intakeSubsystem;
  public RealArm armSubsystem;
  private Mode mode;

  private SequentialCommandGroup intakeCone;
  private SequentialCommandGroup intakeCube;

  private DriveCommand driveCommand;
  private ArmManual armManual;
  private InitArm initArm;

  public RobotContainer() {
    createSubsystems(); // Create our subsystems.
    createCommands(); // Create our commands
    configureButtonBindings(); // Configure the button bindings
  }

  private void createSubsystems() {
    mode = new Mode();

    driveTrainSubsystem = new DriveTrainSubsystem(LEFT_FRONT_MOTOR, LEFT_BACK_MOTOR, RIGHT_FRONT_MOTOR,
        RIGHT_BACK_MOTOR);
    intakeSubsystem = new IntakeSubsystem(INTAKE_LEFT_MOTOR, INTAKE_RIGHT_MOTOR, FRONT_BEAM_BRAKE, BACK_BEAM_BRAKE);

    armSubsystem = new RealArm(BASE_MOTOR1, BASE_MOTOR2, WRIST_MOTOR, BASE_LIMIT, WRIST_LIMIT);
  }

  private void createCommands() {

    armManual = new ArmManual(armSubsystem, () -> -operatorController.getLeftY(), () -> operatorController.getRightY());
    //armSubsystem.setDefaultCommand(armManual);

    initArm = new InitArm(armSubsystem);

    driveCommand = new DriveCommand(driveTrainSubsystem,
        () -> driveController.getLeftTriggerAxis() - driveController.getRightTriggerAxis(),
        () -> driveController.getLeftX());
    driveTrainSubsystem.setDefaultCommand(driveCommand);

    intakeCone = new SequentialCommandGroup();
    // Sets the mode to Cone
    intakeCone.addCommands(new InstantCommand(() -> mode.setMode(Type.CONE)));
    // intakeCone.addCommands(new InstantCommand(() -> arm.setPos(2)));
    intakeCone.addCommands(new IntakeCommand(intakeSubsystem, Type.CONE, () -> driveController.y().getAsBoolean()));
    // intakeCone.addCommands(new InstantCommand(() -> arm.setPos(1)));

    intakeCube = new SequentialCommandGroup();
    // Sets the Mode to Cubes
    intakeCube.addCommands(new InstantCommand(() -> mode.setMode(Type.CUBE)));
    // Sets The pos of the arm to intake
    // intakeCube.addCommands(new InstantCommand(() -> arm.setPos(12)));
    // scheduals an intake command
    intakeCube.addCommands(new IntakeCommand(intakeSubsystem, Type.CUBE, () -> driveController.y().getAsBoolean()));
    // Sets the arm to storage
    // intakeCube.addCommands(new InstantCommand(() -> arm.setPos(11)));

  }

  private void configureButtonBindings() {

    // Calls Dup to move Arm to high position
    driveController.povUp().onTrue(new Dup(mode, armSubsystem));

    // Calls Dright to move Arm to mid position
    driveController.povRight().onTrue(new Dright(mode, armSubsystem));

    // Calls Ddown to move Arm to low position
    driveController.povDown().onTrue(new Ddown(mode, armSubsystem));

    // Calls Dleft to move Arm to storage position
    driveController.povLeft().onTrue(new Dleft(mode, armSubsystem));

    //Update PID Arm values
    driveController.b().onTrue(new InstantCommand(() -> armSubsystem.updatePID(),armSubsystem));

    // Toggle Brake Mode with A
    driveController.a().onTrue(new InstantCommand(() -> driveTrainSubsystem.toggleMode(), driveTrainSubsystem));
    System.out.println("Hello");
    // Eject Item with X
    driveController.x().onTrue(new IntakeCommand(intakeSubsystem, Type.EJECT, () -> driveController.y().getAsBoolean()));
    
    // Update PID values


    // Intake Cone with Right Bumper
    driveController.rightBumper().onTrue(intakeCone);
    
    // Intake Cube wiht Left Bumper
    driveController.leftBumper().onTrue(intakeCube);
    
    // Init Arm
    driveController.start().onTrue(initArm);
    
    // reset base encoder
    operatorController.start().onTrue(new InstantCommand(() -> armSubsystem.resetBaseEncoder(), armSubsystem));
    
    // reset writs encoder
    operatorController.back().onTrue(new InstantCommand(() -> armSubsystem.resetWristEncoder(), armSubsystem));

  }

  public Command getAutonomousCommand() {
    return null;
  }
}
