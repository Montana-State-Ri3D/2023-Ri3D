// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivetrain;

/** Add your docs here. */
public class DriveTrainSimIO implements DriveTrainIO  {

    private boolean isBrake;

    public DriveTrainSimIO(){
        isBrake = false;
    }
    public void updateInputs(DriveTrainIOInputs inputs){
        inputs.isBrake = isBrake;
        inputs.leftCurent = 0;
        inputs.rightCurent = 0;
    }
    public void drive(double leftPower, double rightPower){}
    public void toggleMode(){
        isBrake = !isBrake;
    }
}
