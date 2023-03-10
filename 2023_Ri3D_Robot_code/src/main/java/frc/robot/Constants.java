package frc.robot;

public final class Constants {
    // Xbox Controllers Port Indexes
    public static final int DRIVE_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;
    public static final int TEST_CONTROLLER_PORT = 2;

    public static final int PDP_CAN_ID = 0;

    // Drive Motors
    public static final int LEFT_FRONT_MOTOR = 1;
    public static final int LEFT_BACK_MOTOR = 2;
    public static final int RIGHT_FRONT_MOTOR = 3;
    public static final int RIGHT_BACK_MOTOR = 4;

    // Intake Motore
    public static final int INTAKE_LEFT_MOTOR = 8;
    public static final int INTAKE_RIGHT_MOTOR = 7;
    // Intake DIO
    public static final int FRONT_BEAM_BRAKE = 0;
    public static final int BACK_BEAM_BRAKE = 1;

    //Arm Motors
    public static final int SHOULDER_MOTOR1 = 10;
    public static final int SHOULDER_MOTOR2 = 11;
    public static final int WRIST_MOTOR = 12;
    // Arm DIO
    public static final int SHOULDER_LIMIT = 2;
    public static final int WRIST_LIMIT = 3;

    // Gear Radios
    public static final double SHOULDER_RADIO = 360.0 * (1.0 / ((70.0) * (26.0 / 15.0)));
    public static final double WRIST_RADIO = 360.0*(1.0/40.0);
    public static final double INTAKE_RADIO = 360.0*(1.0/7.0);
    public static final double DRIVE_RADIO = 360.0*(1.0/8.0);
}
