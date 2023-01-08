// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Mode extends SubsystemBase {

  private ShuffleboardTab tab;
  private NetworkTableEntry modeTelem;

  private int mode;
  /** Creates a new Mode. */
  public Mode() {
    tab = Shuffleboard.getTab("Arm");
    mode = 1;

    modeTelem = tab.add("Mode", 1)
    .withPosition(1, 0)
    .withSize(1, 1)
    .getEntry();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    modeTelem.setDefaultDouble(mode);
  }
  public int getMode() {
      return mode;
  }
  public void setMode(int mode){
    this.mode = mode;
  }
}
