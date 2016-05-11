package sem.group5.bob.car;

import java.util.Observable;
import java.util.Observer;

public class Pose {

    private double X = 0, Y = 0;
    double distTmp = 0,angle,dist;

    public Pose() {
        calculatePose();
    }



    /**
     *
     * @param locationData
     */
    public void breakDown(String locationData){

        this.angle = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1, locationData.indexOf("d")));
        this.dist = Double.parseDouble(locationData.substring(locationData.indexOf("d") + 1, locationData.indexOf("/")));
    }

    /**
     *
     */
    public void calculatePose() {
        double distOld = 0, x, y;
        double distTmp = dist - distOld;

        if (Math.abs(angle) >= 360) {
            int turn = (int) angle / 360;
            this.angle = angle - (360 * turn);
        }
        if (angle == 90 || angle == 270) {
            X += dist;
            distOld += distTmp;
        } else if (angle == 0 || angle == 180) {
            Y += dist;
            distOld += distTmp;
        } else if (angle != 0 && angle != 90) {

            x = dist * Math.cos(rdNum((Math.toRadians(angle)), 5));
            y = dist * Math.sin(rdNum((Math.toRadians(angle)), 5));

            this.X += rdNum(x, 3);
            this.Y += rdNum(y, 3);
            distOld += distTmp;
            System.out.println(X + "this is the X");
            System.out.println(Y + "this is the Y");
        }
    }

    /**
     * Round up the number to two significant digits.
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
     *
     * @param X
     * @param Y
     * @param angle
     * @return
     */
    public String getLatestPose(){
        X = this.X;
        Y = this.Y;
        angle = this.angle;

        return X + " " + Y + " " + angle;
    }


}

//read the web
//http://stackoverflow.com/questions/15996345/java-arduino-read-data-from-the-serial-port
//https://codeshare.io/TcybC