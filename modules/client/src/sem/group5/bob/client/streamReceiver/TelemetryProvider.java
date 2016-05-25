package sem.group5.bob.client.streamReceiver;

import sem.group5.bob.client.Pose;
import sem.group5.bob.client.ScanLine;
import sem.group5.bob.client.Telemetry;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

public class TelemetryProvider extends Observable implements Observer{

    /**
     * Method that will mimic a laser rangefinder data by passing a line of pixels captured from an image .
     * @param image frame captured.
     * @return distanceArray
     */
    private static int[] generateLine(BufferedImage image){
        int[] distanceArray = new int[640];
        for(int i = 0; i < 640; i++){
            distanceArray[i]=(new Color(image.getRGB(i, 240)).getRed())*16;
        }
        return distanceArray;
    }

    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param observable observable object
     * @param o the argument passed to the notifyObservers method
     */@Override
    public void update(Observable observable, Object o) {
        MultiPartsParse mpp = (MultiPartsParse) observable;

        if(!(o instanceof BufferedImage)){ return; }

        Pose p = new Pose(mpp.getPose());
        ScanLine scl = new ScanLine(generateLine((BufferedImage) o));
        Telemetry t = new Telemetry(p, scl);

        setChanged();
        notifyObservers(t);
    }
}
