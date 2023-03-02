// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

import static frc.robot.Constants.*;

/** Add your docs here. */
public class IntakeSimIO implements IntakeIO {

    private double power;
    private final DCMotor intakeMotors = DCMotor.getNEO(2);
    private final DCMotorSim intakeMotorsSim = new DCMotorSim(intakeMotors,INTAKE_RADIO, 0.05);

    public IntakeSimIO() {}

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.AppliedPower = power;
        inputs.totalPowerDraw = intakeMotorsSim.getCurrentDrawAmps();
        inputs.coneBeam = true;
        inputs.cubeBeam = true;
    }

    @Override
    public void intakePower(double power) {
        intakeMotorsSim.setInput(power);
        this.power = power;
    }

    @Override
    public boolean getCubeBeam() {
        return true;
    }

    @Override
    public boolean getConeBeam() {
        return true;
    }
}
