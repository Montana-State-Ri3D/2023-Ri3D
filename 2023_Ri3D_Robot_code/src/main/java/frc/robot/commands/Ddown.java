// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.Mode;

public class Ddown extends CommandBase {
  /** Creates a new Dup. */
  private Mode mode;
  private ArmSubsystem arm;
  private boolean done;

  public Ddown(Mode mode, ArmSubsystem arm) {
    this.mode = mode;
    this.arm = arm;
    done = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (mode.getMode() == 1) {// If Cone
      arm.setPos(2);
    } else if (mode.getMode() == 0) {// If Cube
      arm.setPos(12);
    }
    done = true;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}
