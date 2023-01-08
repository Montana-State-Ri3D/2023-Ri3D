// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends CommandBase {

  private int type;
  private boolean done;
  private IntakeSubsystem subsystem;

  private BooleanSupplier cancle;

  /** Creates a new Intake. */
  public IntakeCommand(IntakeSubsystem subsystem, int type, BooleanSupplier cancle) {
    this.type = type;
    this.subsystem = subsystem;
    this.cancle = cancle;

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    addRequirements(subsystem);
    if (checkDone()) {
      done = true;
    } else {
      subsystem.intakePower(0.5);
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    done = checkDone();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsystem.intakePower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }

  private boolean checkDone() {
    if(cancle.getAsBoolean())
    {
      return true;
    }
    
    if (type == 1) {// If Cone
      if (subsystem.getBackBeam() == false) {
        return true;
      }
    } else if (type == 0) {// If Cube
      if (subsystem.getFrontBeam() == false) {
        return true;
      }
    }
    return false;
  }
}
