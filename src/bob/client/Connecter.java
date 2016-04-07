package bob.client;
import SmartCarInterface.SmartCar;
import udpSockets.DiscoveryListener;

import java.io.IOException;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class Connecter {
    boolean isConnected;
    DiscoveryListener dl = new DiscoveryListener();
    SmartCar sm;
    String ip;
    /**
     * Method to set connection with the smartcar.
     */
    public void connect(String ip){
        try {
            if (!isConnected) {
                this.ip = ip;
                sm = new SmartCar(ip, 1234);
                isConnected = true;
            } else {
                sm.close();
                isConnected = false;
            }
        } catch (IOException ie){

        }

    }
}
