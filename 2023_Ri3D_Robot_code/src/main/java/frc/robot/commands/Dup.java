// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.Mode;

public class Dup extends CommandBase {
  /** Creates a new Dup. */
  private int type;
  private ArmSubsystem arm;

  public Dup(Mode mode, ArmSubsystem arm) {
    // Use addRequirements() here to declare subsystem dependencies.
    type = mode.getMode();
    this.arm = arm;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (type == 1) {// If Cone
      arm.setPos(4);
    } else if (type == 0) {// If Cube
      arm.setPos(14);
    }
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
    return true;
  }
}
