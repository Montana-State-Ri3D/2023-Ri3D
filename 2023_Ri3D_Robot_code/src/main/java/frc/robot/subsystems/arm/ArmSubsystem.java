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

  private double ksP = 0.1;
  private double ksI = 0.00;
  private double ksD = 0.0;
  private double ksVel = 0.0;
  private double ksAcc = 0.0;
  private double kVertAngle = 60;
  private double kFrontP = 0.0000;// P Value whne the arm is forwards
  private double kBackP =  0.0000;// P vale for when the arm is backwards

  private double kwP = 0.017;
  private double kwI = 0.00001;
  private double kwD = 0.0;
  private double kwFF = 0.0;
  private double kwVel = 1500.0;
  private double kwAcc = 0.0;

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
    shoulderPos.put(3, 90.0);// Mid Place
    shoulderPos.put(4, 70.0);// High Place

    shoulderPos.put(11, 0.0);// Storage //Cubes
    shoulderPos.put(12, 0.0);// Low Place
    shoulderPos.put(13, 0.0);// Mid Place
    shoulderPos.put(14, 70.0);// High Place

    wristPos = new HashMap<>();
    wristPos.put(0, 0.0);

    wristPos.put(1, 0.0);// Storage //Cones
    wristPos.put(2, 90.0);// Low Place
    wristPos.put(3, 0.0);// Mid Place
    wristPos.put(4, 110.0);// High Place

    wristPos.put(11, 0.0);// Storage //Cubes
    wristPos.put(12, 90.0);// Low Place
    wristPos.put(13, 0.0);// Mid Place
    wristPos.put(14, 110.0);// High Place
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);

    Logger logger = Logger.getInstance();

    logger.processInputs("Arm/Inputs", inputs);

    logger.recordOutput("Arm/Pose", pose);

    double shoulderPIDOut = shoulderPID.calculate(inputs.shoulderAngleDeg, shoulderPos.get(pose));

    double shoulderPower = MathUtil.clamp(shoulderPIDOut + dynamicFFCalculate(), -0.25,0.25);

    double wristPower = MathUtil.clamp(wristPID.calculate(inputs.wristAngleDeg, wristPos.get(pose)), -0.5, 0.5)
        + kwFF;

    logger.recordOutput("Arm/ShoulderPIDOut", shoulderPIDOut);
    logger.recordOutput("Arm/ShoulderPower", shoulderPower);
    logger.recordOutput("Arm/WristPower", wristPower);

    logger.recordOutput("Arm/ShoulderGoalAngle", shoulderPos.get(pose));
    logger.recordOutput("Arm/WristGoalAngle", wristPos.get(pose));

    io.setShoulderPower(shoulderPower);
    io.setWristPower(wristPower);
  }

  public double dynamicFFCalculate() {
    double offCenter = inputs.shoulderAngleDeg - kVertAngle;
    Logger logger = Logger.getInstance();
    double ff = 0.0;
    if (offCenter > 0.0) {
      ff = -kFrontP * offCenter;
    } else {
      ff = -kBackP * offCenter;
    }

    logger.recordOutput("Arm/ShoulderFF", ff);
    return ff;
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

}