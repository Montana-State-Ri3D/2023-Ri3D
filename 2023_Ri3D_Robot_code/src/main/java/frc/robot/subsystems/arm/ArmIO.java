// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import org.littletonrobotics.junction.AutoLog;

/** Add your docs here. */
public interface ArmIO {
    @AutoLog
    class ArmIOInputs{
        public double shoulderAngleDeg;
        public double shoulderAngularVelDegPerSec;
        public double shoulderCurrentDrawAmps;
        public double shoulderAppliedPower;

        public double wristAngleDeg;
        public double wristAngularVelDegPerSec;
        public double wristCurrentDrawAmps;
        public double wristAppliedPower;

        public boolean shoulderLimit;
        public boolean wristLimit;
    }

    default void resetShoulderEncoder(){
    }
    default void resetWristEncoder(){
    }
    default void updateInputs(ArmIOInputs inputs){
    }
    default void setShoulderPower(double power) {
    }
    default void setWristPower(double power) {
    }
}
