package sem.group5.bob.client.streamReceiver;

import sem.group5.bob.client.LogToFile;

import java.awt.*;
import sem.group5.bob.client.mappGenerator.LogToFile;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;


/**
 * Class responsible for generating data to be used on mapping.
 */
public class ScanLineGenerator extends Observable implements Observer
{
    private LogToFile log;

    /**
     * Method that will mimic a laser rangefinder data by passing a line of pixels captured from an image .
     * @param image frame captured.
     * @return distanceArray
     */
    private static int[] generateLine(BufferedImage image)
    {
        int[] distanceArray = new int[640];
        for(int i = 0; i < 640; i++)
        {
            distanceArray[i]=(new Color(image.getRGB(i+1, 240)).getRed())*16;
        }
        return distanceArray;
    }

    /**
     * todo
     * @param array array
     */
    private void scanLineToLog(int[] array)
    {
        log.addToList(Arrays.toString(array));
    }

    /**
     * todo
     * @param log log
     */
    public void setLog(LogToFile log)
    {
        this.log = log;
    }


    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param observable observable object
     * @param o the argument passed to the notifyObservers method
     */@Override
    public void update(Observable observable, Object o) {
        if(o instanceof BufferedImage ) {
            BufferedImage image = (BufferedImage) o;
            setChanged();
            int[] pixelLine = generateLine(image);
            notifyObservers(pixelLine);
            scanLineToLog(pixelLine);
        } else {
            System.out.println("Did not receive a valid image at Scan line");
        }
    }
}
