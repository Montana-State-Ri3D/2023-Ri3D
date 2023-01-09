// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;

public class InitArm extends CommandBase {
  private boolean baseDone;
  private boolean writsDone;
  private ArmSubsystem arm;

  public InitArm(ArmSubsystem arm) {
    this.arm = arm;
    addRequirements(arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    arm.setWristPower(-0.1);
    arm.setBasePower(-0.1);
    baseDone = false;
    writsDone = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!baseDone) {
      if (arm.getBaseLimit()) {
        arm.setBasePower(0);
        arm.resetBaseEncoder();
        baseDone = true;
      }
    }
    if (!writsDone) {
      if (arm.getWristLimit()) {
        arm.setWristPower(0);
        arm.resetWristEncoder();
        writsDone = true;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (baseDone && writsDone) {
      return true;
    } else {
      return false;
    }
  }
}
