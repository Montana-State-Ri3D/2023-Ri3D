package frc.robot;
public class Joystick {
        public static double JoystickInput(double value, double power, double deadband, boolean clamp) {
            if (clamp) {
                value = Math.max(-1.0, Math.min(value, 1.0));
            }
            if (power != 0) {
                value = Math.copySign(Math.pow(value, power), value);
            }
            if (deadband != 0) {
                if (Math.abs(value) > deadband) {
                    if (value > 0) {
                        value = (value - deadband) / (1.0 - deadband);
                    } else {
                        value = (value + deadband) / (1.0 - deadband);
                    }
                } else {
                    value = 0;
                }
            }
            return value;
        }

        public static double JoystickInput(double value) {
            return JoystickInput(value, 2, 0.02, true);
        }
        public static double JoystickInput(double value,double deadband) {
            return JoystickInput(value, 2, deadband, true);
        }
}
