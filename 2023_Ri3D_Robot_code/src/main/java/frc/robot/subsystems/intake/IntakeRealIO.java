// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;

/** Add your docs here. */
public class IntakeRealIO implements IntakeIO {

    private CANSparkMax rightMotor;
    private CANSparkMax leftMotor;
    private DigitalInput cubeBeam;
    private DigitalInput coneBeam;

    public IntakeRealIO(int leftMotorID, int rightMotorID, int frontBeamID, int backBeamID) {
        cubeBeam = new DigitalInput(frontBeamID);
        coneBeam = new DigitalInput(backBeamID);

        rightMotor = new CANSparkMax(rightMotorID, MotorType.kBrushless);
        leftMotor = new CANSparkMax(leftMotorID, MotorType.kBrushless);

        leftMotor.setIdleMode(IdleMode.kBrake);
        rightMotor.setIdleMode(IdleMode.kBrake);

        rightMotor.follow(leftMotor, true);

        int curentLimit = 40;
        rightMotor.setSmartCurrentLimit(curentLimit);
        leftMotor.setSmartCurrentLimit(curentLimit);
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.AppliedPower = leftMotor.getAppliedOutput();
        inputs.totalPowerDraw = leftMotor.getOutputCurrent() + rightMotor.getOutputCurrent();
        inputs.coneBeam = coneBeam.get();
        inputs.cubeBeam = cubeBeam.get();
    }

    @Override
    public void intakePower(double power) {
        leftMotor.set(power);
    }

    @Override
    public boolean getCubeBeam() {
        return cubeBeam.get();
    }

    @Override
    public boolean getConeBeam() {
        return coneBeam.get();
    }
}
