package frc.robot;

import frc.robot.commands.Dleft;
import frc.robot.commands.Dright;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.Dup;
import frc.robot.commands.Ddown;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.Mode;
import frc.robot.subsystems.Mode.Type;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.utility.RobotIdentity;
import frc.robot.utility.SubsystemFactory;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import static frc.robot.Constants.*;

public class RobotContainer {

  // Creating Controlers
  @SuppressWarnings({ "unused" })
  private final CommandXboxController driveController = new CommandXboxController(DRIVE_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final CommandXboxController operatorController = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
  @SuppressWarnings({ "unused" })
  private final CommandXboxController testController = new CommandXboxController(TEST_CONTROLLER_PORT);

  private DriveTrain driveTrainSubsystem;
  private IntakeSubsystem intakeSubsystem;
  public ArmSubsystem armSubsystem;
  private Mode mode;

  private SequentialCommandGroup intakeCone;
  private SequentialCommandGroup intakeCube;

  private DriveCommand driveCommand;

  public RobotContainer() {
    createSubsystems(); // Create our subsystems.
    createCommands(); // Create our commands
    configureButtonBindings(); // Configure the button bindings
  }

  private void createSubsystems() {
    RobotIdentity identity = RobotIdentity.getIdentity();

    armSubsystem = SubsystemFactory.createArm(identity);

    driveTrainSubsystem = SubsystemFactory.createDriveTrain(identity);

    intakeSubsystem = SubsystemFactory.createIntake(identity);

    mode = new Mode();
  }

  private void createCommands() {

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

    // Toggle Brake Mode with A
    driveController.a().onTrue(new InstantCommand(() -> driveTrainSubsystem.toggleMode(), driveTrainSubsystem));
    // Eject Item with X
    driveController.x()
        .onTrue(new IntakeCommand(intakeSubsystem, Type.EJECT, () -> driveController.y().getAsBoolean()));

    // Intake Cone with Right Bumper
    driveController.rightBumper().onTrue(intakeCone);

    // Intake Cube wiht Left Bumper
    driveController.leftBumper().onTrue(intakeCube);

    // Init Arm
    //driveController.start().onTrue(initArm);

    // reset base encoder
    operatorController.start().onTrue(new InstantCommand(() -> armSubsystem.resetShoulderEncoder(), armSubsystem));

    // reset writs encoder
    operatorController.back().onTrue(new InstantCommand(() -> armSubsystem.resetWristEncoder(), armSubsystem));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
