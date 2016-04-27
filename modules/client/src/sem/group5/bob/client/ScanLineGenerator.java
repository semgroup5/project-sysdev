package sem.group5.bob.client;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Emanuel on 4/27/2016.
 */
public class ScanLineGenerator extends Observable implements Observer {
    private static int[] GenerateLine(BufferedImage image){
        int[] distanceArray = new int[640];
        for(int i = 0; i < 640; i++){
            distanceArray[i]=image.getRGB(240, i);
        }
        return  distanceArray;
    }

    @Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        this.notifyObservers(this.GenerateLine(image));
    }
}
