// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Mode extends SubsystemBase {

  private ShuffleboardTab tab;
  private GenericEntry modeTelem;

  private Type mode;

  public enum Type {
    CONE,
    CUBE,
    EJECT
  }

  /** Creates a new Mode. */
  public Mode() {
    tab = Shuffleboard.getTab("Arm");
    mode = Type.CONE;

    modeTelem = tab.add("Mode", "CONE")
        .withPosition(1, 0)
        .withSize(1, 1)
        .getEntry();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    modeTelem.setString(mode.toString());
  }

  public Type getMode() {
    return mode;
  }

  public void setMode(Type mode) {
    this.mode = mode;
  }
}
