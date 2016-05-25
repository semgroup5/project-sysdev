package sem.group5.bob.car.smartCarManager;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will read a set distance and angle values provided by the car's sensors and will
 * implement some mathematical calculations on it and will to return the current position of the car.
 */
public class PoseManager implements Observer {
    private double angle;
    private double disp;
    private double dispOld = 0;
    private double X = 0, Y = 0;


    /**
     * Round up the number the digits can be selected.
     *
     * @param a todo
     * @param r todo
     * @return todo
     */

    private static double rdNum(Double a, int r) {
        if (r < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, r);
        a = a * factor;
        long tmp = Math.round(a);
        return (double) tmp / factor;
    }

    /**
     * Breaks down the raw data from the arduino to values
     *
     * @param locationData String that holds the raw data
     */
    private void breakDown(String locationData) {
        this.angle = Double.parseDouble(locationData.substring(locationData.indexOf("d") + 1, locationData.indexOf("a")));
        this.disp = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1, locationData.indexOf("/")));
        calculatePose();
    }

    /**
     * Method that will calculate the position of the car and adds different arguments for 4 special cases depending on
     * the angle of the car(angle Zero, 90, 180, 270) to avoid getting a zero value on an axes traveled by the car.
     */
    private void calculatePose()
    {
        double angTmp;
        double x;
        double y;
        double dispTmp = disp - dispOld;
        double angOld = 0;
        angTmp = angle- angOld;
        if (Math.abs(angle) >= 360) {
            angle = angle % 360;
        }
        if(angTmp !=0 || dispTmp !=0) {
            if (angle == 90) {
                X += disp;
                dispOld += dispTmp;
            } else if (angle == 270) {
                X -= disp;
                dispOld += dispTmp;
            } else if (angle == 0) {
                Y += disp;
                dispOld += dispTmp;
            } else if (angle == 180) {
                Y -= disp;
                dispOld += dispTmp;
            } else {
                y = dispTmp * Math.cos(rdNum((Math.toRadians(angle)), 5));
                x = dispTmp * Math.sin(rdNum((Math.toRadians(angle)), 5));

                this.X += rdNum(x, 0);
                this.Y += rdNum(y, 0);
                dispOld += dispTmp;
            }
        }

    }

    /**
     * p
     * todo
     *
     * @param o   todo
     * @param arg todo
     */
    public void update(Observable o, Object arg) {
        String locationData = (String) arg;
        breakDown(locationData);
        System.out.println(locationData);
    }

    /**
     * @return A string with X, Y and angle will be returned with the format of "X"+ X + "Y" + Y + "Ang" + angle.
     */
    public String getLatestPose() {
        return "X" + X + "Y" + Y + "Ang" + angle;
    }
}
