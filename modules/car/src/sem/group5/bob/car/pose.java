package sem.group5.bob.car;

/**
 * Created by GeoffreyC on 2016/5/1.
 * Updated by Geoffrey, Axel and Emanuel at 16-05-06
 */

class pose extends SerialConnect implements poseProvider {


    private double X = 0, Y = 0;
    double dispTmp = 0,angle,disp;

    /**
     *
     * @param locationData
     */
    public void breakDown(String locationData){

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
            int turn = (int) angle / 360;
            this.angle = angle - (360 * turn);
        }
        if (angle == 90 || angle == 270) {
            X += disp;
            dispOld += dispTmp;
        } else if (angle == 0 || angle == 180) {
            Y += disp;
            dispOld += dispTmp;
        } else if (angle != 0 && angle != 90) {

            x = disp * Math.cos(rdNum((Math.toRadians(angle)), 5));
            y = disp * Math.sin(rdNum((Math.toRadians(angle)), 5));

            this.X += rdNum(x, 3);
            this.Y += rdNum(y, 3);
            dispOld += dispTmp;
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
     * @return
     */
    public String getLatestpose() {
        calculatePose();
        String pose;
        pose = X + " " + Y + " " + angle + " ";
        return pose;
    }
}