// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sensors.HallSensor;

public class ArmSubsystem extends SubsystemBase {

  private boolean isIniting;

  private HashMap<Integer, Double> BasePos;
  private HashMap<Integer, Double> WristPos;
  private int pos;

  private CANSparkMax armBase;
  private SparkMaxPIDController armBasePID;
  private RelativeEncoder armBaseEncoder;

  private CANSparkMax armWrist;
  private SparkMaxPIDController armWristPID;
  private RelativeEncoder armWristEncoder;

  private HallSensor BaseLimit;
  private DigitalInput WristLimit;

  /** Creates a new Arm. */
  public ArmSubsystem(int armBaseID, int armWristID) {
    isIniting = true;

    BaseLimit = new HallSensor(0);
    WristLimit = new DigitalInput(1);

    initArmBase(armBaseID);
    initArmWrist(armWristID);
    makeMaps();
  }

  private void initArmBase(int armBaseID) {
    armBase = new CANSparkMax(armBaseID, MotorType.kBrushless);
    armBase.restoreFactoryDefaults();
    armBase.setIdleMode(IdleMode.kBrake);
    armBasePID = armBase.getPIDController();
    armBaseEncoder = armBase.getEncoder();

    armBaseEncoder.setPositionConversionFactor(0.69);

    armBasePID.setP(0.000001);
    armBasePID.setI(0.000001);
    armBasePID.setD(0.000001);
    armBasePID.setIZone(0);
    armBasePID.setFF(0);
    armBasePID.setOutputRange(1, -1);
  }

  private void initArmWrist(int armWristID) {
    armWrist = new CANSparkMax(armWristID, MotorType.kBrushless);
    armWrist.restoreFactoryDefaults();
    armWrist.setIdleMode(IdleMode.kBrake);
    armWristPID = armWrist.getPIDController();
    armWristEncoder = armWrist.getEncoder();

    armWristEncoder.setPositionConversionFactor(0.69);

    armWristPID.setP(0.000001);
    armWristPID.setI(0.000001);
    armWristPID.setD(0.000001);
    armWristPID.setIZone(0);
    armWristPID.setFF(0);
    armWristPID.setOutputRange(1, -1);
  }

  private void makeMaps() {
    BasePos = new HashMap<>();
    WristPos.put(1, 180.8);
    WristPos.put(2, 0.0);
    WristPos.put(3, 0.0);
    WristPos.put(4, 0.0);

    WristPos = new HashMap<>();
    WristPos.put(1, 0.0);
    WristPos.put(2, 0.0);
    WristPos.put(3, 0.0);
    WristPos.put(4, 0.0);
  }

  @Override
  public void periodic() {

    if (!isIniting) {
      armWristPID.setReference(WristPos.get(pos), CANSparkMax.ControlType.kPosition);
      armBasePID.setReference(BasePos.get(pos), CANSparkMax.ControlType.kPosition);
    }

  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public void setWristPower(double wristPower) {
    armWrist.set(wristPower);
  }

  public void setBasePower(double basePower) {
    armBase.set(basePower);
  }

  public void resetWristEncoder() {
    armWristEncoder.setPosition(0);
  }

  public void resetBaseEncoder() {
    armBaseEncoder.setPosition(0);
  }

  public boolean getWristLimit() {
    return WristLimit.get();
  }

  public boolean getBaseLimit() {
    return BaseLimit.getBoolean();
  }
}
