package sem.group5.bob.car;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by GeoffreyC on 2016/5/1.
 * Updated by Geoffrey, Axel and Emanuel at 16-05-06
 */

public class Pose extends SerialConnect implements Observer {


    double dispTmp = 0, angle, disp;
    private double X = 0, Y = 0;

    /**
     * Round up the number the digits can be selected.
     *
     * @param a
     * @param r
     * @return
     */
    public static double rdNum(Double a, int r) {
        if (r < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, r);
        a = a * factor;
        long tmp = Math.round(a);
        return (double) tmp / factor;

    }

    /**
     * @param locationData
     */
    public void breakDown(String locationData) {
        this.angle = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1, locationData.indexOf("d")));
        this.disp = Double.parseDouble(locationData.substring(locationData.indexOf("d") + 1, locationData.indexOf("/")));
    }

    /**
     *
     */
    public void calculatePose() {

        double dispOld = 0, x, y;
        double dispTmp = disp - dispOld;

        if (Math.abs(angle) >= 360) {
            this.angle = angle % 360;
        }
        if (angle == 90) {
            Y += disp;
            dispOld += dispTmp;
        } else if (angle == 270) {
            Y -= disp;
            dispOld += dispTmp;
        } else if (angle == 0) {
            X += disp;
            dispOld += dispTmp;
        } else if (angle == 180) {
            X -= disp;
            dispOld += dispTmp;
        } else {

            x = dispTmp * Math.cos(rdNum((Math.toRadians(angle)), 5));
            y = dispTmp * Math.sin(rdNum((Math.toRadians(angle)), 5));

            this.X += rdNum(x, 3);
            this.Y += rdNum(y, 3);
            dispOld += dispTmp;
            System.out.println(X + "this is the X");
            System.out.println(Y + "this is the Y");
        }
    }

    public void update(Observable o, Object arg) {
        String locationData = (String) arg;
        breakDown(locationData);
    }

    /**
     * @return A string with X, Y and angle will be returned with the format of "X"+ X + "Y" + Y + "Ang" + angle.
     */
    public String getLatestPose() {
        calculatePose();
        System.out.print("This is the data sent from getLatestPose: " + "X" + X + "Y" + Y + "Ang" + angle);
        return "X" + X + "Y" + Y + "Ang" + angle;
    }
}
