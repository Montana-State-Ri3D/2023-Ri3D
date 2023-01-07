// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {

  private CANSparkMax motor1;
  private CANSparkMax motor2;

  private int pos;

  public Elevator(int motor1ID, int motor2ID) {

    motor1 = new CANSparkMax(motor1ID, MotorType.kBrushless);
    motor2 = new CANSparkMax(motor2ID, MotorType.kBrushless);

    motor1.setIdleMode(IdleMode.kBrake);
    motor1.setIdleMode(IdleMode.kBrake);

    int MAX_CURRENT = 15;
    motor1.setSmartCurrentLimit(MAX_CURRENT);
    motor2.setSmartCurrentLimit(MAX_CURRENT);

    motor2.setInverted(true);

    motor2.follow(this.motor1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public int getPos() {
    return pos;
  }
}
