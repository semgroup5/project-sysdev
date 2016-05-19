package sem.group5.bob.client;

import sun.rmi.runtime.Log;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


/**
 * Class responsible for generating data to be used on mapping.
 */
class ScanLineGenerator extends Observable implements Observer {
    LogToFile log;
    /**
     * Method that will mimic a laser rangefinder data by passing a line of pixels captured from an image .
     * @param image frame captured.
     * @return distanceArray
     */
    private static int[] generateLine(BufferedImage image){
        int[] distanceArray = new int[640];
        for(int i = 0; i < 640; i++){
            distanceArray[i]=image.getRGB(240, i);
        }
        return distanceArray;
    }
    private void scanLineToLog(int[] array){
        log.addToList(Arrays.toString(array));
    }
    public void setLog(LogToFile log) {
        this.log = log;
    }

    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param observable observable object
     * @param o the argument passed to the notifyObservers method
     */@Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        setChanged();
        int [] pixelLine = generateLine(image);
        notifyObservers(pixelLine);
        scanLineToLog(pixelLine);
    }


}
