package sem.group5.bob.client;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager {
    private boolean isConnected;
    String ip;
    DiscoveryListener d;
    Socket socket;
    SmartCar sm;

    public void init() {
        try {
            if (!isConnected) {
                d = new DiscoveryListener();
                Thread t = new Thread(d);
                t.start();

                this.ip = d.getIp();
                socket();
                if (!this.ip.equals(null)) sm = new SmartCar(this.socket);
                isConnected = true;
            } else {
                sm.close();
                isConnected = false;
            }
        } catch (IOException ie) {
            ie.printStackTrace();
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

    public SmartCar getSmartCar(){
        return this.sm;
    }
}
