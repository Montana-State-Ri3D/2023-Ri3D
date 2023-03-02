// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Mode;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class IntakeCommand extends CommandBase {

  public Mode.Type type;
  private boolean broken;
  private IntakeSubsystem subsystem;
  private long startTime;

  private BooleanSupplier cancel;

  /** Creates a new Intake. */
  public IntakeCommand(IntakeSubsystem subsystem, Mode.Type type, BooleanSupplier cancel) {
    this.type = type;
    this.subsystem = subsystem;
    this.cancel = cancel;
    broken = false;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (type == Mode.Type.EJECT){
      subsystem.intakePower(-1);
      startTime = System.currentTimeMillis();
      return;
    }
    else{
      subsystem.intakePower(0.80);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    broken = checkDone();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsystem.intakePower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return broken;
  }

  private boolean checkDone() {
    if(cancel.getAsBoolean())
    {
      return true;
    }
    
    if (type == Mode.Type.CONE) {// If Cone
      if (subsystem.getConeBeam() == false) {
        return true;
      }
    } else if (type == Mode.Type.CUBE) {// If Cube
      if (subsystem.getCubeBeam() == false) {
        return true;
      }
    } else if (type == Mode.Type.EJECT){
      if(System.currentTimeMillis() - startTime >= 500){
        return true;
      }
    }
    return false;
  }
}
