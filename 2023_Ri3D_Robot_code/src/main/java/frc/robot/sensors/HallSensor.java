package frc.robot.sensors;
import edu.wpi.first.wpilibj.AnalogInput;

public class HallSensor {
    private double threshold;
    private AnalogInput sensor;

    public HallSensor(int pinID, double threshold) {
        this.threshold = threshold;
        sensor = new AnalogInput(pinID);
    }

    public HallSensor(int pinID) {
        this.threshold = 0.2;
        sensor = new AnalogInput(pinID);
    }

    public boolean getBoolean() {
        if (sensor.getVoltage() < threshold) {
            return true;
        } else {
            return false;
        }
    }

    public double getVoltage() {
        return sensor.getVoltage();
    }
}
