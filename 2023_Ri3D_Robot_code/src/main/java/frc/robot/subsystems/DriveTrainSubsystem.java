package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

//Shuffleboard
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.networktables.NetworkTableEntry;

public class DriveTrainSubsystem extends SubsystemBase {
  /** Creates a new DriveTrainSubsystem. */

  // Creating the Tab in Shuffleboard
  private final ShuffleboardTab tab = Shuffleboard.getTab("DriveBase");

  // Declaring the PowerDraw NetworkTable Entry
  private final NetworkTableEntry PowerDraw;

  // Declaring the motor controler Member variable
  private CANSparkMax leftMotor_1;
  private CANSparkMax leftMotor_2;
  private CANSparkMax rightMotor_1;
  private CANSparkMax rightMotor_2;

  public DriveTrainSubsystem(int IDleftMotor_1, int IDleftMotor_2, int IDrightMotor_1, int IDrightMotor_2) {
    leftMotor_1 = new CANSparkMax(IDleftMotor_1, MotorType.kBrushless);
    leftMotor_2 = new CANSparkMax(IDleftMotor_2, MotorType.kBrushless);
    rightMotor_1 = new CANSparkMax(IDrightMotor_1, MotorType.kBrushless);
    rightMotor_2 = new CANSparkMax(IDrightMotor_2, MotorType.kBrushless);

    leftMotor_1.restoreFactoryDefaults();
    leftMotor_2.restoreFactoryDefaults();
    rightMotor_1.restoreFactoryDefaults();
    rightMotor_2.restoreFactoryDefaults();

    int MAX_CURRENT = 15;
    leftMotor_1.setSmartCurrentLimit(MAX_CURRENT);
    leftMotor_2.setSmartCurrentLimit(MAX_CURRENT);
    rightMotor_1.setSmartCurrentLimit(MAX_CURRENT);
    rightMotor_2.setSmartCurrentLimit(MAX_CURRENT);

    leftMotor_1.setIdleMode(IdleMode.kCoast);
    leftMotor_2.setIdleMode(IdleMode.kCoast);
    rightMotor_1.setIdleMode(IdleMode.kCoast);
    rightMotor_2.setIdleMode(IdleMode.kCoast);

    leftMotor_1.setInverted(true);
    leftMotor_2.setInverted(true);

    leftMotor_2.follow(leftMotor_1);
    rightMotor_2.follow(rightMotor_1);

    // Shuffleboard setup
    PowerDraw = tab.add("Curent Power Draw of Full Drive Base", 0)
        .withPosition(0, 0)
        .withSize(3, 2)
        .withProperties(Map.of("min", 0, "max", 100))
        .withWidget(BuiltInWidgets.kGraph)
        .getEntry();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    PowerDraw.setNumber(leftMotor_1.getOutputCurrent() + leftMotor_2.getOutputCurrent()
        + rightMotor_1.getOutputCurrent() + rightMotor_2.getOutputCurrent());
  }

  public void drive(double leftPower, double rightPower) {
    leftMotor_1.set(leftPower);
    rightMotor_1.set(rightPower);
  }
}
