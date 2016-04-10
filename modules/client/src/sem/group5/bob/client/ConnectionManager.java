package sem.group5.bob.client;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager implements Runnable {
    private boolean isConnected;
    String ip;
    DiscoveryListener d;
    Socket socket;
    Smartcar sm = new Smartcar(this.socket);


    public void run() {
        try {
            if (!isConnected) {
                d = new DiscoveryListener();
                this.ip = d.getIp();
                sm = new Smartcar(this.socket);
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
    public String getIP(){
        return this.ip;
    }
    public Smartcar getSmartCar(){
        return this.sm;
    }
}
