package sem.group5.bob.car.smartcar;

import java.util.Observable;
import java.util.Observer;

/**
 * Class that reads a set data of distance and angle values provided by the car's sensors and
 * return the current position of the car as a result.
 */
public class PoseManager implements Observer {

    private double oldDistance = 0;
    private double trueDistance = 0;
    private double carAngle;
    private double carDistance;
    private double coordinateX = 0, coordinateY = 0;


    /**
     * Round up the number the digits can be selected.
     *
     * @param a the car angle
     * @param r how many decimals we use
     * @return returns the roundup number
     */

    private static double roundupNum(Double a, int r) {
        if (r < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, r);
        a = a * factor;
        long tmp = Math.round(a);
        return (double) tmp / factor;


    }

    /**
     * Breaks down the raw data from the arduino to values
     *
     * @param locationData String that holds the raw data from arduino serial
     */
    private void breakDown(String locationData) {

        this.carDistance = Double.parseDouble(locationData.substring(locationData.indexOf("d") + 1, locationData.indexOf("a")));
        this.carAngle = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1));
        calculatePose();
        trueDistance = carDistance - oldDistance;
        oldDistance = carDistance;
    }

    /**
     * Method that will calculate the position of the car and adds different arguments for 4 special cases depending on
     * the carAngle of the car(carAngle Zero, 90, 180, 270) to avoid getting a zero value on an axes traveled by the car.
     */
    private void calculatePose()
    {
        double xTmp;
        double yTmp;


        if (Math.abs(carAngle) >= 360) {
            this.carAngle = carAngle % 360;
        }
        yTmp = trueDistance * Math.cos(Math.toRadians(carAngle));
        xTmp = trueDistance * Math.sin(Math.toRadians(carAngle));
        this.coordinateX += xTmp;
        this.coordinateY += yTmp;
        System.out.println(coordinateX + "  this is the coordinateX");
        System.out.println(coordinateY + "  this is the coordinateY");
    }



    /**
     * Method that gets the serial read from the arduino
     *
     * @param o   Unused variable
     * @param arg Holds the data passed from the observable
     */
    public void update(Observable o, Object arg) {
        String locationData = (String) arg;
        breakDown(locationData);
        System.out.println(locationData);
    }

    /**
     * @return A string with X, Y and carAngle will be returned with the format of "X"+ coordinateX + "Y" + coordinateY + "Ang" + carAngle.
     */
    public String getLatestPose() {
        return "X" + coordinateX + "Y" + coordinateY + "Ang" + carAngle;
    }
}
