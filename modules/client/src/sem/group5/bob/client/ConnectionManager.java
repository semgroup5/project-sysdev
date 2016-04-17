package sem.group5.bob.client;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager {
    String ip;
    DiscoveryListener d;
    Socket socket;
    Smartcar sm;

    public void init(ControllerGUI ctr) {
        try {
            d = new DiscoveryListener();
            d.run();

            this.ip = d.getIp();
            if (!this.ip.equals(null)) {
                socket();
                sm = new Smartcar(this.socket, ctr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void socket() {
        try {
            this.socket = new Socket(this.ip, 1234);
            System.out.println("Socket established");
        }catch(IOException e){
            System.out.println(e);
        }
    }
    public String getIP(){
        return this.ip;
    }

    public Smartcar getSmartCar(){
        return this.sm;
    }

    public DiscoveryListener getDiscoverListener(){ return this.d; }

}
