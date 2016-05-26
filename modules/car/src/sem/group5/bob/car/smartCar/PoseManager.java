package sem.group5.bob.car.smartcar;

import java.util.Observable;
import java.util.Observer;

/**
 * This class will read a set distance and carAngle values provided by the car's sensors and will
 * implement some mathematical calculations on it and will to return the current position of the car.
 */
public class PoseManager implements Observer {
    private double carAngle;
    private double carDistance;
    private double distOld = 0;
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
        this.carAngle = Double.parseDouble(locationData.substring(locationData.indexOf("a") + 1, locationData.indexOf("/")));
        calculatePose();
    }

    /**
     * Method that will calculate the position of the car and adds different arguments for 4 special cases depending on
     * the carAngle of the car(carAngle Zero, 90, 180, 270) to avoid getting a zero value on an axes traveled by the car.
     */
    private void calculatePose()
    {
        double angTmp;
        double xTmp;
        double yTmp;
        double dispTmp = carDistance - distOld;
        double angOld = 0;
        angTmp = carAngle - angOld;
        if (Math.abs(carAngle) >= 360) {
            this.carAngle = carAngle % 360;
        }
        if (angTmp != 0 || dispTmp != 0) {
            if (carAngle == 90) {
                coordinateX += carDistance;
                distOld += dispTmp;
            } else if (carAngle == 270) {
                coordinateX -= carDistance;
                distOld += dispTmp;
            } else if (carAngle == 0) {
                coordinateY += carDistance;
                distOld += dispTmp;
            } else if (carAngle == 180) {
                coordinateY -= carDistance;
                distOld += dispTmp;
            } else {

                yTmp = dispTmp * Math.cos(roundupNum((Math.toRadians(carAngle)), 5));
                xTmp = dispTmp * Math.sin(roundupNum((Math.toRadians(carAngle)), 5));

                this.coordinateX += roundupNum(xTmp, 0);
                this.coordinateY += roundupNum(yTmp, 0);
                distOld += dispTmp;
                System.out.println(coordinateX + "this is the coordinateX");
                System.out.println(coordinateY + "this is the coordinateY");

            }
        }

    }

    /**
     * Method that gets the serial read from the arduino
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
