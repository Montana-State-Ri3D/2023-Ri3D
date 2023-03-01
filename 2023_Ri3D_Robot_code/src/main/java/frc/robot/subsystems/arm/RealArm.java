// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import java.util.HashMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RealArm extends SubsystemBase {

  public boolean isIniting;

  private HashMap<Integer, Double> BasePos;
  private HashMap<Integer, Double> WristPos;
  private int pos;

  private CANSparkMax armBase1;
  private CANSparkMax armBase2;
  private SparkMaxPIDController armBase1PID;
  private RelativeEncoder armBase1Encoder;

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
  private GenericEntry baseGoalAngle;
  private GenericEntry wristGoalAngle;
  private GenericEntry baseOutput;
  private GenericEntry wristOutput;

  private GenericEntry kP;
  private GenericEntry kI;
  private GenericEntry kD;
  private GenericEntry kIz;
  private GenericEntry kFF;
  private GenericEntry kMaxOutput;
  private GenericEntry kMinOutput;

  private GenericEntry test;

  /** Creates a new Arm. */
  public RealArm(int armBase1ID, int armBase2ID, int armWristID, int baseLimitID, int wristLimitID) {
    isIniting = true;

    BaseLimit = new DigitalInput(baseLimitID);
    WristLimit = new DigitalInput(wristLimitID);

    intiTelem();
    initArmBase(armBase1ID, armBase2ID);
    initArmWrist(armWristID);
    makeMaps();
  }

  private void intiTelem() {
    tab = Shuffleboard.getTab("Arm");

    posTelem = tab.add("pos", 0)
        .withPosition(0, 0)
        .withSize(1, 1)
        .getEntry();

    wristLimitTelem = tab.add("Wrist Limit", false)
        .withPosition(2, 0)
        .withSize(1, 1)
        .getEntry();

    baseLimitTelem = tab.add("Base Limit", false)
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

    baseGoalAngle = tab.add("Base Goal Angle", 0)
        .withPosition(4, 1)
        .withSize(1, 1)
        .getEntry();

    wristGoalAngle = tab.add("Wrist Goal Angle", 0)
        .withPosition(5, 1)
        .withSize(1, 1)
        .getEntry();

    wristOutput = tab.add("Writs OP", 0)
        .withPosition(5, 2)
        .withSize(1, 1)
        .getEntry();

    baseOutput = tab.add("Base OP", 0)
        .withPosition(4, 2)
        .withSize(1, 1)
        .getEntry();

    int kConstantsRow = 4;

    kP = tab.add("kP", 0)
        .withPosition(0, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kI = tab.add("kI", 0)
        .withPosition(1, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kD = tab.add("kD", 0)
        .withPosition(2, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kIz = tab.add("kIz", 0)
        .withPosition(3, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kFF = tab.add("kF", 0)
        .withPosition(4, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kMaxOutput = tab.add("kMaxOutput", 0)
        .withPosition(5, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    kMinOutput = tab.add("kMinOutput", 0)
        .withPosition(6, kConstantsRow)
        .withSize(1, 1)
        .getEntry();

    test = tab.add("test", 0)
        .withPosition(4, 2)
        .withSize(10, 10)
        .getEntry();
  }

  private void initArmBase(int armBase1ID, int armBase2ID) {
    armBase1 = new CANSparkMax(armBase1ID, MotorType.kBrushless);
    armBase2 = new CANSparkMax(armBase2ID, MotorType.kBrushless);

    armBase2.setSmartCurrentLimit(45);
    armBase1.setSmartCurrentLimit(45);

    armBase1.restoreFactoryDefaults();
    armBase2.restoreFactoryDefaults();

    armBase2.setIdleMode(IdleMode.kBrake);
    armBase1.setIdleMode(IdleMode.kBrake);

    armBase2.follow(armBase1, true);

    armBase1.setInverted(true);

    armBase1Encoder = armBase1.getEncoder();

    armBase1Encoder.setPositionConversionFactor(360 * (1.0 / (70.0 * (26.0 / 15.0))));

    armBase1PID = armBase1.getPIDController();

    armBase1PID.setP(0.000001);
    armBase1PID.setI(0.000000);
    armBase1PID.setD(0.000000);
    armBase1PID.setIZone(0);
    armBase1PID.setFF(0);
    armBase1PID.setOutputRange(-.25, .25);
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
    armWristPID.setOutputRange(-.25, .25);
  }

  private void makeMaps() {
    BasePos = new HashMap<>();
    BasePos.put(0, 0.0);

    BasePos.put(1, 0.0);// Storage //Cones
    BasePos.put(2, 0.0);// Low Place
    BasePos.put(3, 0.0);// Mid Place
    BasePos.put(4, 60.0);// High Place

    BasePos.put(11, 0.0);// Storage //Cubes
    BasePos.put(12, 0.0);// Low Place
    BasePos.put(13, 0.0);// Mid Place
    BasePos.put(14, 60.0);// High Place

    WristPos = new HashMap<>();
    WristPos.put(0, 0.0);

    WristPos.put(1, 0.0);// Storage //Cones
    WristPos.put(2, 0.0);// Low Place
    WristPos.put(3, 0.0);// Mid Place
    WristPos.put(4, 0.0);// High Place

    WristPos.put(11, 0.0);// Storage //Cubes
    WristPos.put(12, 0.0);// Low Place
    WristPos.put(13, 0.0);// Mid Place
    WristPos.put(14, 0.0);// High Place
  }

  @Override
  public void periodic() {

    if (true) {
      armWristPID.setReference(WristPos.get(pos), CANSparkMax.ControlType.kPosition);
      armBase1PID.setReference(BasePos.get(pos), CANSparkMax.ControlType.kPosition);
    }
    baseOutput.setDouble(armBase1.getAppliedOutput());
    wristOutput.setDouble(armWrist.getAppliedOutput());
    posTelem.setInteger(pos);
    baseAngle.setDouble(armBase1Encoder.getPosition());
    wristAngle.setDouble(armWristEncoder.getPosition());
    baseGoalAngle.setDouble(BasePos.get(pos));
    wristGoalAngle.setDouble(WristPos.get(pos));
    baseLimitTelem.setBoolean(BaseLimit.get());
    wristLimitTelem.setBoolean(WristLimit.get());
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

  public void resetWristEncoder() {
    armWristEncoder.setPosition(0);
  }

  public void resetBaseEncoder() {
    armBase1Encoder.setPosition(0);
  }

  public boolean getWristLimit() {
    return !WristLimit.get();
  }

  public boolean getBaseLimit() {
    return !BaseLimit.get();
  }

  public void updatePID() {
    armBase1PID.setP(kP.getDouble(0));
    armBase1PID.setI(kI.getDouble(0));
    armBase1PID.setD(kD.getDouble(0));
    armBase1PID.setIZone(kIz.getDouble(0));
    armBase1PID.setFF(kFF.getDouble(0));
    armBase1PID.setOutputRange(kMinOutput.getDouble(0), kMaxOutput.getDouble(0));

  }

  public void dumpPIDCoefficients() {
    System.out.println("--------------------------------------------------------");
    System.out.printf("private static final double kPBase = %f;\n", armBase1PID.getP());
    System.out.printf("private static final double kIBase = %f;\n", armBase1PID.getI());
    System.out.printf("private static final double kDBase = %f;\n", armBase1PID.getD());
    System.out.printf("private static final double kIzBase = %f;\n", armBase1PID.getIZone());
    System.out.printf("private static final double kFFBase = %f;\n", armBase1PID.getFF());
    System.out.printf("private static final double kMaxOutputBase = %f;\n", armBase1PID.getOutputMax());
    System.out.printf("private static final double kMinOutputBase = %f;\n", armBase1PID.getOutputMin());
    System.out.println("--------------------------------------------------------");
}
}
