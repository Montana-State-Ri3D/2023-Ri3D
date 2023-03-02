// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {

  private IntakeIO io;

  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);

    Logger logger = Logger.getInstance();

    logger.processInputs("Intake/Inputs", inputs);
    
    if (this.getCurrentCommand() != null) {

      logger.recordOutput("Intake/CurentCommand", this.getCurrentCommand().getName());
    } else {
      logger.recordOutput("Intake/CurentCommand", "none");
    }

  }

  public void intakePower(double power) {
    io.intakePower(power);
  }

  public boolean getCubeBeam() {
    return io.getCubeBeam();
  }

  public boolean getConeBeam() {
    return io.getConeBeam();
  }
}
