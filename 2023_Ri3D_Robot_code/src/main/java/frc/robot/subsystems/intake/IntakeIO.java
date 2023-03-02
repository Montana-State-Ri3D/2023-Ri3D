// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

/** Add your docs here. */
public interface IntakeIO {
    @AutoLog
    class IntakeIOInputs{
        public double totalPowerDraw;
        public double AppliedPower;

        public boolean cubeBeam;
        public boolean coneBeam;
    }
    default void updateInputs(IntakeIOInputs inputs){}
    default void intakePower(double power){}
    default boolean getCubeBeam(){return false;}
    default boolean getConeBeam(){return false;}
}
