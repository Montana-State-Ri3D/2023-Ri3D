// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.HashMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {

  public boolean isIniting;

  private HashMap<Integer, Double> BasePos;
  private HashMap<Integer, Double> WristPos;
  private int pos;

  private CANSparkMax armBase1;
  private CANSparkMax armBase2;
  private SparkMaxPIDController armBase1PID;
  private RelativeEncoder armBase1Encoder;
  private Solenoid helper;

  private CANSparkMax armWrist;
  private SparkMaxPIDController armWristPID;
  private RelativeEncoder armWristEncoder;

  private DigitalInput BaseLimit;
  private DigitalInput WristLimit;

  private ShuffleboardTab tab;
  
  private GenericEntry posTelem;
  private GenericEntry wristLimitTelem;
  private GenericEntry baseLimitTelem;
  private GenericEntry baseAngle;
  private GenericEntry wristAngle;

  /** Creates a new Arm. */
  public ArmSubsystem(int armBase1ID,int armBase2ID, int armWristID, int baseLimitID, int wristLimitID) {
    isIniting = true;

    tab = Shuffleboard.getTab("Arm");

    posTelem = tab.add("pos", 0)
    .withPosition(0, 0)
    .withSize(1, 1)
    .getEntry();

    wristLimitTelem = tab.add("Wrist Limit", 0)
    .withPosition(2, 0)
    .withSize(1, 1)
    .getEntry();

    baseLimitTelem = tab.add("Base Limit", 0)
    .withPosition(3, 0)
    .withSize(1, 1)
    .getEntry();

    baseAngle = tab.add("Base Angle", 0)
    .withPosition(4, 0)
    .withSize(1, 1)
    .getEntry();

    wristAngle = tab.add("Wrist Angle", 0)
    .withPosition(5, 0)
    .withSize(1, 1)
    .getEntry();



    BaseLimit = new DigitalInput(baseLimitID);
    WristLimit = new DigitalInput(wristLimitID);

    initArmBase(armBase1ID, armBase2ID);
    initArmWrist(armWristID);
    makeMaps();
  }

  private void initArmBase(int armBase1ID,int armBase2ID) {
    armBase1 = new CANSparkMax(armBase1ID, MotorType.kBrushless);
    armBase2 = new CANSparkMax(armBase2ID, MotorType.kBrushless);

    helper = new Solenoid(PneumaticsModuleType.CTREPCM, 0);

    helper.set(false);

    armBase2.restoreFactoryDefaults();
    armBase2.setIdleMode(IdleMode.kBrake);

    armBase2.follow(armBase1,true);

    armBase1.restoreFactoryDefaults();
    armBase1.setIdleMode(IdleMode.kBrake);
    
    armBase1PID = armBase1.getPIDController();
    armBase1Encoder = armBase1.getEncoder();

    armBase1Encoder.setPositionConversionFactor(0.69);

    armBase1PID.setP(0.000001);
    armBase1PID.setI(0.000001);
    armBase1PID.setD(0.000001);
    armBase1PID.setIZone(0);
    armBase1PID.setFF(0);
    armBase1PID.setOutputRange(1, -1);
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
    BasePos.put(0, 0.0);

    BasePos.put(1, 0.0);//Storage //Cones
    BasePos.put(2, 0.0);//Low Place
    BasePos.put(3, 0.0);//Mid Place
    BasePos.put(4, 0.0);//High Place

    BasePos.put(11, 0.0);//Storage //Cubes
    BasePos.put(12, 0.0);//Low Place
    BasePos.put(13, 0.0);//Mid Place
    BasePos.put(14, 0.0);//High Place

    WristPos = new HashMap<>();
    WristPos.put(0, 0.0);

    WristPos.put(1, 0.0);//Storage //Cones
    WristPos.put(2, 0.0);//Low Place
    WristPos.put(3, 0.0);//Mid Place
    WristPos.put(4, 0.0);//High Place

    WristPos.put(11, 0.0);//Storage //Cubes
    WristPos.put(12, 0.0);//Low Place
    WristPos.put(13, 0.0);//Mid Place
    WristPos.put(14, 0.0);//High Place
  }

  @Override
  public void periodic() {
    /*
    if (!isIniting) {
      armWristPID.setReference(WristPos.get(pos), CANSparkMax.ControlType.kPosition);
      armBase1PID.setReference(BasePos.get(pos), CANSparkMax.ControlType.kPosition);
    }
    */

    posTelem.setDouble(pos);
    baseAngle.setDouble(armBase1Encoder.getPosition());
    wristAngle.setDouble(armWristEncoder.getPosition());
    baseLimitTelem.setBoolean(!BaseLimit.get());
    wristLimitTelem.setBoolean(!WristLimit.get());
  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public void setWristPower(double wristPower) {
    armWrist.set(wristPower);
  }

  public void setBasePower(double basePower) {
    armBase1.set(basePower);
  }
/*
  public void resetWristEncoder() {
    armWristEncoder.setPosition(0);
  }

  public void resetBaseEncoder() {
    armBase1Encoder.setPosition(0);
  }
*/
  public boolean getWristLimit() {
    return !WristLimit.get();
  }

  public boolean getBaseLimit() {
    return !BaseLimit.get();
  }

  public void toggleHelper(){
    helper.toggle();
  }
  
}
