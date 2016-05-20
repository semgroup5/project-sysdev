package sem.group5.bob.car;

import java.util.Observable;
import java.util.Observer;

public class Pose extends SerialConnect implements Observer {


    private double dispTmp = 0, angle, disp;
    private double X = 0, Y = 0;

    /**
     * Round up the number the digits can be selected.
     * @param a a
     * @param r d
     * @return d
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
     * @param locationData String that holds the raw data
     */
    private void breakDown(String locationData){
        this.angle = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1, locationData.indexOf("d")));
        this.disp = Double.parseDouble(locationData.substring(locationData.indexOf("d") + 1, locationData.indexOf("/")));
    }

    /**
     *
     */
    private void calculatePose() {

        double dispOld = 0, x, y;
        double dispTmp = disp - dispOld;

        if (Math.abs(angle) >= 360) {
            int turn = (int) angle / 360;
            this.angle = angle - (360 * turn);
        }
        if (angle == 90 || angle == 270) {
            X += disp;
            dispOld += dispTmp;
        } else if (angle == 0 || angle == 180) {
            Y += disp;
            dispOld += dispTmp;
        } else {

            x = disp * Math.cos(rdNum((Math.toRadians(angle)), 5));
            y = disp * Math.sin(rdNum((Math.toRadians(angle)), 5));

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
        String pose;
        pose = "X" + X + "Y" + Y + "Ang" + angle;
        return pose;
    }
}
