// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import static frc.robot.Constants.*;

public class ArmRealIO implements ArmIO {

  private CANSparkMax shoulder1;
  private CANSparkMax shoulder2;
  private SparkMaxAbsoluteEncoder shoulder1Encoder;

  private CANSparkMax wristMotor;
  private RelativeEncoder wristEncoder;

  public ArmRealIO() {
    initArmBase(SHOULDER_MOTOR1, SHOULDER_MOTOR2);
    initArmWrist(WRIST_MOTOR);
  }

  private void initArmBase(int armShoulder1ID, int armShoulder2ID) {
    shoulder1 = new CANSparkMax(armShoulder1ID, MotorType.kBrushless);
    shoulder2 = new CANSparkMax(armShoulder2ID, MotorType.kBrushless);

    shoulder1.restoreFactoryDefaults();
    shoulder2.restoreFactoryDefaults();

    shoulder2.setSmartCurrentLimit(45);
    shoulder1.setSmartCurrentLimit(45);

    shoulder2.setIdleMode(IdleMode.kBrake);
    shoulder1.setIdleMode(IdleMode.kBrake);

    shoulder2.follow(shoulder1, true);

    shoulder1.setInverted(true);

    shoulder1Encoder = shoulder1.getAbsoluteEncoder(Type.kDutyCycle);

    shoulder1Encoder.setPositionConversionFactor(SHOULDER_RADIO);
    shoulder1Encoder.setZeroOffset(SHOULDER_OFFSET);
  }

  private void initArmWrist(int armWristID) {
    wristMotor = new CANSparkMax(armWristID, MotorType.kBrushless);
    wristMotor.restoreFactoryDefaults();
    wristMotor.setIdleMode(IdleMode.kBrake);
    wristEncoder = wristMotor.getEncoder();

    wristMotor.setSmartCurrentLimit(25);

    wristMotor.setInverted(true);

    wristEncoder.setPositionConversionFactor(WRIST_RADIO);
    shoulder1Encoder.setZeroOffset(WRIST_OFFSET);
  }

  @Override
  public void updateInputs(ArmIOInputs inputs) {
    inputs.shoulderAngleDeg = shoulder1Encoder.getPosition();
    inputs.shoulderAngularVelDegPerSec = shoulder1Encoder.getVelocity();
    inputs.shoulderCurrentDrawAmps = shoulder1.getOutputCurrent();
    inputs.shoulderAppliedPower = shoulder1.getAppliedOutput();

    inputs.wristAngleDeg = wristEncoder.getPosition();
    inputs.wristAngularVelDegPerSec = wristEncoder.getVelocity();
    inputs.wristCurrentDrawAmps = wristMotor.getOutputCurrent();
    inputs.wristAppliedPower = wristMotor.getAppliedOutput();
  }

  @Override
  public void setShoulderPower(double power) {
    shoulder1.set(power);
  }

  @Override
  public void setWristPower(double power) {
    wristMotor.set(power);
  }
}
