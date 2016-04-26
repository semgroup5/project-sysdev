package sem.group5.bob.client;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager {
    String carIp;
    DiscoveryListener d;
    Socket controlSocket;
    Socket depthSocket;
    Smartcar sm;

    public void init(ControllerGUI ctr) {
        try {
            d = new DiscoveryListener();
            d.run();

            this.carIp = d.getIp();
            this.controlSocket = initControlSocket();
            System.out.println("Socket established");

            //this.depthSocket = initDepthSocket();
            System.out.println("Depth socket established");

            //if (!this.carIp.equals(null)) sm = new Smartcar(this.controlSocket, ctr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket initControlSocket() throws IOException{
        return new Socket(this.carIp, 1234);
    }

    public Socket initDepthSocket() throws IOException{
        return new Socket(this.carIp, 50001);
    }

    public String getCarIp(){
        return this.carIp;
    }

    public Smartcar getSmartCar(){
        return this.sm;
    }

    public DiscoveryListener getDiscoverListener(){ return this.d; }

}
