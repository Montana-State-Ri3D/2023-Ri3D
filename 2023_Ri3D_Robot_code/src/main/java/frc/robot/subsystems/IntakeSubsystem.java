// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

  private CANSparkMax rightMotor;
  private CANSparkMax leftMotor;
  private DigitalInput frontBeam;
  private DigitalInput backBeam;

  private ShuffleboardTab tab;
  private GenericEntry frontBeamTelem;
  private GenericEntry backBeamTelem;
  private GenericEntry totalPowerDraw;

  public IntakeSubsystem(int leftMotorID, int rightMotorID, int frontBeamID, int backBeamID) {
    tab = Shuffleboard.getTab("Intake");

    frontBeamTelem = tab.add("Front Beam Brake Sensor", 0)
        .withPosition(0, 0)
        .withSize(2, 1)
        .getEntry();

    backBeamTelem = tab.add("Back Beam Brake Sensor", 0)
        .withPosition(2, 0)
        .withSize(2, 1)
        .getEntry();

    totalPowerDraw = tab.add("Total Power Draw", 0)
        .withPosition(0, 1)
        .withSize(2, 1)
        .getEntry();

    frontBeam = new DigitalInput(frontBeamID);
    backBeam = new DigitalInput(backBeamID);

    rightMotor = new CANSparkMax(rightMotorID, MotorType.kBrushless);
    leftMotor = new CANSparkMax(leftMotorID, MotorType.kBrushless);

    leftMotor.setIdleMode(IdleMode.kBrake);
    rightMotor.setIdleMode(IdleMode.kBrake);

    rightMotor.follow(leftMotor,true);

    int curentLimit = 40;
    rightMotor.setSmartCurrentLimit(curentLimit);
    leftMotor.setSmartCurrentLimit(curentLimit);

  }

  @Override
  public void periodic() {
    frontBeamTelem.setBoolean(frontBeam.get());
    backBeamTelem.setBoolean(backBeam.get());
    totalPowerDraw.setDouble(leftMotor.getOutputCurrent());
  }

  public void intakePower(double power) {
    leftMotor.set(power);
  }

  public boolean getFrontBeam() {
    return frontBeam.get();
  }

  public boolean getBackBeam() {
    return backBeam.get();
  }
}
