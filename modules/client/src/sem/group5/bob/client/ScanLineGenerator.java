package sem.group5.bob.client;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;


/**
 * TODO//Addheaders to all the classes.
 */
class ScanLineGenerator extends Observable implements Observer {
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

    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param observable observable object
     * @param o the argument passed to the notifyObservers method
     */@Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        setChanged();
        notifyObservers(generateLine(image));
    }
}
