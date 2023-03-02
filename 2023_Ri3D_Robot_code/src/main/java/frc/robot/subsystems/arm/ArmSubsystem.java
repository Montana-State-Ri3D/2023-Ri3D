// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import java.util.HashMap;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {
  /** Creates a new ArmSubsystem. */
  public boolean isIniting;

  private final ArmIO io;

  private final ArmIOInputsAutoLogged inputs = new ArmIOInputsAutoLogged();

  private HashMap<Integer, Double> shoulderPos;
  private HashMap<Integer, Double> wristPos;

  private int pose;

  private ProfiledPIDController shoulderPID;

  private ProfiledPIDController wristPID;

  double ksP = 0.05;
  double ksI = 0.00;
  double ksD = 0.05;
  double ksFF = 0.0;
  double ksVel = 0.0;
  double ksAcc = 0.0;

  double kwP = 0.0;
  double kwI = 0.0;
  double kwD = 0.0;
  double kwFF = 0.0;
  double kwVel = 0.0;
  double kwAcc = 0.0;

  /** Creates a new Arm. */
  public ArmSubsystem(ArmIO io) {
    this.io = io;

    shoulderPID = new ProfiledPIDController(ksP, ksI, ksD, new TrapezoidProfile.Constraints(ksVel, ksAcc));
    wristPID = new ProfiledPIDController(kwP, kwI, kwD, new TrapezoidProfile.Constraints(kwVel, kwAcc));

    makeMaps();
  }

  private void makeMaps() {
    shoulderPos = new HashMap<>();
    shoulderPos.put(0, 0.0);

    shoulderPos.put(1, 0.0);// Storage //Cones
    shoulderPos.put(2, 0.0);// Low Place
    shoulderPos.put(3, 0.0);// Mid Place
    shoulderPos.put(4, 60.0);// High Place

    shoulderPos.put(11, 0.0);// Storage //Cubes
    shoulderPos.put(12, 0.0);// Low Place
    shoulderPos.put(13, 0.0);// Mid Place
    shoulderPos.put(14, 60.0);// High Place

    wristPos = new HashMap<>();
    wristPos.put(0, 0.0);

    wristPos.put(1, 0.0);// Storage //Cones
    wristPos.put(2, 0.0);// Low Place
    wristPos.put(3, 0.0);// Mid Place
    wristPos.put(4, 0.0);// High Place

    wristPos.put(11, 0.0);// Storage //Cubes
    wristPos.put(12, 0.0);// Low Place
    wristPos.put(13, 0.0);// Mid Place
    wristPos.put(14, 0.0);// High Place
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.getInstance().processInputs("Arm/Inputs", inputs);

    Logger.getInstance().recordOutput("Arm/Pose", pose);

    double shoulderPower = MathUtil.clamp(shoulderPID.calculate(inputs.shoulderAngleDeg, shoulderPos.get(pose)), -0.25,
        0.25) + ksFF;
    double wristPower = MathUtil.clamp(wristPID.calculate(inputs.wristAngleDeg, wristPos.get(pose)), -0.25, 0.25) + kwFF;

    Logger.getInstance().recordOutput("Arm/ShoulderPower", shoulderPower);
    Logger.getInstance().recordOutput("Arm/WristPower", wristPower);

    Logger.getInstance().recordOutput("Arm/ShoulderGoalAngle", shoulderPos.get(pose));
    Logger.getInstance().recordOutput("Arm/WristGoalAngle", wristPos.get(pose));

    io.setShoulderPower(shoulderPower);
    io.setWristPower(wristPower);
  }

  public void setPose(int pos) {
    this.pose = pos;
  }

  public void setWristPower(double wristPower) {
    io.setWristPower(wristPower);
  }

  public void setShoulderPower(double basePower) {
    io.setShoulderPower(basePower);
  }

  public void resetWristEncoder() {
    io.resetWristEncoder();
  }

  public void resetShoulderEncoder() {
    io.resetShoulderEncoder();
  }

  public boolean getWristLimit() {
    return !inputs.wristLimit;
  }

  public boolean getShoulderLimit() {
    return !inputs.shoulderLimit;
  }

  public void updatePID() {

  }

}