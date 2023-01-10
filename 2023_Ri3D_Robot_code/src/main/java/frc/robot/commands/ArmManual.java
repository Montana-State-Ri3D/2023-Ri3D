// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ArmSubsystem;

public class ArmManual extends CommandBase {
  /** Creates a new ArmManual. */
  private ArmSubsystem arm;
  private DoubleSupplier basePower;
  private DoubleSupplier wristPower;

  public ArmManual(ArmSubsystem arm, DoubleSupplier basePower,DoubleSupplier wristPower){ 
    this.arm = arm;
    this.basePower = basePower;
    this.wristPower = wristPower;
    addRequirements(arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    arm.setBasePower(basePower.getAsDouble()/4);
    arm.setWristPower(wristPower.getAsDouble()/2);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    arm.setBasePower(0);
    arm.setWristPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
