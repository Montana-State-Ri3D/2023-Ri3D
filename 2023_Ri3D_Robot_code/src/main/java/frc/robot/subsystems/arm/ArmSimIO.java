// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.math.VecBuilder;

public class ArmSimIO implements ArmIO {
  private static final double ARM_CG_MOMENT_OF_INERTIA = 0.5;
  private static final double ARM_LENGHT = 6.5;
  
  private static final double INTAKE_CG_MOMENT_OF_INERTIA = 0.2;
  private static final double INTAKE_MASS = 3;

  private final DCMotor shoulderMotor = DCMotor.getNEO(1);
  private static final double shoulderGearRadio = 360 * (1.0 / (70.0 * (26.0 / 15.0)));

  private SingleJointedArmSim shoulderSim;

  private double inputShoulderPower = 0;

  /** Creates a new Arm. */
  public ArmSimIO() {


    shoulderSim = new SingleJointedArmSim(
    shoulderMotor,
    shoulderGearRadio,
    ARM_CG_MOMENT_OF_INERTIA,
    ARM_LENGHT,
    Units.degreesToRadians(0),
    Units.degreesToRadians(120),
    false,
    VecBuilder.fill(2.0 * Math.PI / 4096));

  }



  @Override
  public void updateInputs(ArmIOInputs inputs) {
    inputs.shoulderAngleDeg = Units.radiansToDegrees(shoulderSim.getAngleRads());
    inputs.shoulderAngularVelDegPerSec = (shoulderSim.getVelocityRadPerSec()*360.0)/2.0*Math.PI;
    inputs.shoulderCurrentDrawAmps = shoulderSim.getCurrentDrawAmps();
    inputs.shoulderAppliedPower = inputShoulderPower;

    inputs.wristAngleDeg = 0;
    inputs.wristAngularVelDegPerSec = 0;
    inputs.wristCurrentDrawAmps = 0;
    inputs.wristAppliedPower = 0;

    inputs.shoulderLimit = shoulderSim.hasHitLowerLimit();
    inputs.wristLimit = false;
  }
  @Override
  public void setShoulderPower(double power) {
    inputShoulderPower = power;
    shoulderSim.setInputVoltage(power);
    shoulderSim.update(0.020);
  }
  @Override
  public void setWristPower(double power) {
    //armWrist.set(power);
  }
  @Override
  public void resetShoulderEncoder(){
  }
  @Override
  public void resetWristEncoder(){
  }

}
