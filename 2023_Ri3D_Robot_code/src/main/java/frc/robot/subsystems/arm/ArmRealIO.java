// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

import static frc.robot.Constants.*;

public class ArmRealIO implements ArmIO {
  private DigitalInput shoulderLimit;
  private DigitalInput wristLimit;

  private CANSparkMax shoulder1;
  private CANSparkMax shoulder2;
  private RelativeEncoder shoulder1Encoder;

  private CANSparkMax armWrist;
  private RelativeEncoder armWristEncoder;

  /** Creates a new Arm. */
  public ArmRealIO(int armShoulder1ID, int armShoulder2ID, int armWristID, int shoulderLimitID, int wristLimitID) {

    shoulderLimit = new DigitalInput(shoulderLimitID);
    wristLimit = new DigitalInput(wristLimitID);

    initArmBase(armShoulder1ID, armShoulder2ID);
    initArmWrist(armWristID);

  }

  private void initArmBase(int armShoulder1ID, int armShoulder2ID) {
    shoulder1 = new CANSparkMax(armShoulder1ID, MotorType.kBrushless);
    shoulder2 = new CANSparkMax(armShoulder2ID, MotorType.kBrushless);

    shoulder2.setSmartCurrentLimit(45);
    shoulder1.setSmartCurrentLimit(45);

    shoulder1.restoreFactoryDefaults();
    shoulder2.restoreFactoryDefaults();

    shoulder2.setIdleMode(IdleMode.kBrake);
    shoulder1.setIdleMode(IdleMode.kBrake);

    shoulder2.follow(shoulder1, true);

    shoulder1.setInverted(true);

    shoulder1Encoder = shoulder1.getEncoder();

    shoulder1Encoder.setPositionConversionFactor(SHOULDER_RADIO);
  }

  private void initArmWrist(int armWristID) {
    armWrist = new CANSparkMax(armWristID, MotorType.kBrushless);
    armWrist.restoreFactoryDefaults();
    armWrist.setIdleMode(IdleMode.kBrake);
    armWristEncoder = armWrist.getEncoder();

    armWristEncoder.setPositionConversionFactor(WRIST_RADIO);
  }
  @Override
  public void updateInputs(ArmIOInputs inputs) {
    inputs.shoulderAngleDeg = shoulder1Encoder.getPosition();
    inputs.shoulderAngularVelDegPerSec = shoulder1Encoder.getVelocity();
    inputs.shoulderCurrentDrawAmps = shoulder1.getOutputCurrent();
    inputs.shoulderAppliedPower = shoulder1.getAppliedOutput();

    inputs.wristAngleDeg = armWristEncoder.getPosition();
    inputs.wristAngularVelDegPerSec = armWristEncoder.getVelocity();
    inputs.wristCurrentDrawAmps = shoulder1.getOutputCurrent();
    inputs.wristAppliedPower = shoulder1.getAppliedOutput();

    inputs.shoulderLimit = shoulderLimit.get();
    inputs.wristLimit = wristLimit.get();
  }
  @Override
  public void setShoulderPower(double power) {
    shoulder1.set(power);
  }
  @Override
  public void setWristPower(double power) {
    armWrist.set(power);
  }
  @Override
  public void resetShoulderEncoder(){
    shoulder1Encoder.setPosition(0);
  }
  @Override
  public void resetWristEncoder(){
    armWristEncoder.setPosition(0);
  }

}
