package bob.client;
import SmartCarInterface.SmartCar;
import udpSockets.DiscoveryListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager implements Runnable {
    private boolean isConnected;
    SmartCar sm;
    String ip;
    DiscoveryListener d;
    Socket socket;
    Writer out;

    public void run() {
        try {
            if (!isConnected) {
                d = new DiscoveryListener();
                this.ip = d.getIp();
                sm = new SmartCar(this.socket);
                isConnected = true;
            } else {
                sm.close();
                isConnected = false;
            }
        } catch (IOException ie) {
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
}
