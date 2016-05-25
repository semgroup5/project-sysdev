package sem.group5.bob.car.smartCarManager;

import sem.group5.bob.car.smartCar.SerialConnect;

import java.util.Observable;
import java.util.Observer;

/**
 * todo
 */
public class PoseManager extends SerialConnect implements Observer {
    private double angle;
    private double disp;
    private double dispOld = 0 , angOld =0;
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
     * todo
     */
    private void calculatePose() {
        double angTmp;
        double x;
        double y;
        double dispTmp = disp - dispOld;
        angTmp = angle-this.angOld;
        if (Math.abs(angle) >= 360) {
            this.angle = angle % 360;
        }
        if(angTmp !=0 || dispTmp !=0) {
            if (angle == 90) {
                X += disp;
                dispOld += dispTmp;
                System.out.println("this is the Y" + Y);
            } else if (angle == 270) {
                X -= disp;
                dispOld += dispTmp;
                System.out.println("this is the Y" + Y);
            } else if (angle == 0) {
                Y += disp;
                dispOld += dispTmp;
                System.out.println("this is the X" + X);
            } else if (angle == 180) {
                Y -= disp;
                dispOld += dispTmp;
                System.out.println("this is the X" + X);
            } else if(angle != 90 && angle != 180 && angle != 270 && angle != 0) {

                y = dispTmp * Math.cos(rdNum((Math.toRadians(angle)), 5));
                x = dispTmp * Math.sin(rdNum((Math.toRadians(angle)), 5));

                this.X += rdNum(x, 0);
                this.Y += rdNum(y, 0);
                dispOld += dispTmp;
                System.out.println(X + "this is the X");
                System.out.println(Y + "this is the Y");
            }
        }

    }

    /**
     * p
     * todo
     *
     * @param o   todo v
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
        System.out.print("This is the data sent from getLatestPose: " + "X" + X + "Y" + Y + "Ang" + angle);
        return "X" + X + "Y" + Y + "Ang" + angle;
    }
}
