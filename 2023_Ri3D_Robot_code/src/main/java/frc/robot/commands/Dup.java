// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.Mode;

public class Dup extends CommandBase {
  /** Creates a new Dup. */
  private Mode mode;
  private ArmSubsystem arm;
  private boolean done;

  public Dup(Mode mode, ArmSubsystem arm) {
    this.mode = mode;
    this.arm = arm;
    done = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (mode.getMode() == Mode.Type.CONE) {// If Cone
      arm.setPos(4);
    } else if (mode.getMode() == Mode.Type.CUBE) {// If Cube
      arm.setPos(14);
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
