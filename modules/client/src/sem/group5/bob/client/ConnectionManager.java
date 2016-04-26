package sem.group5.bob.client;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

/**
 * Created by Emanuel on 4/7/2016.
 */
public class ConnectionManager extends Observable{
    String carIp;
    DiscoveryListener d;
    Socket controlSocket;
    Socket depthSocket;
    Smartcar smartcar;
    SmartcarController smartcarController;
    boolean isConnected;
    Exception connectionException;

    public void connect() throws IOException {
        try {
            d = new DiscoveryListener();
            d.listenIp();

            this.carIp = d.getIp();
            this.controlSocket = getControlSocket();
            System.out.println("Socket established");

//            this.depthSocket = getDepthSocket();
//            System.out.println("Depth socket established");

            if (!this.carIp.equals(null)) smartcar = new Smartcar(this.controlSocket);
            isConnected = true;

            setChanged();
            notifyObservers(this);
        } catch (IOException e) {
            connectionException = e;
            isConnected = false;
            setChanged();
            notifyObservers(this);
        }
    }

    public void disconnect(){
        try{
            d.close();

            controlSocket.close();
            System.out.println("Control socket closed!");

//            depthSocket.close();
//            System.out.println("Depth socket closed!");

            isConnected = false;
            setChanged();
            notifyObservers(this);
            System.out.println("notified");
        }catch(Exception e){
            connectionException = e;
        }

    }

    public void reconnect() throws IOException
    {
        disconnect();
        connect();
    }

    public SmartcarController getSmartcarController() throws IOException
    {
        if(smartcarController == null){
            smartcarController = new SmartcarController(this);
        }

        return smartcarController;
    }

    public Socket getControlSocket() throws IOException{
        if(controlSocket == null){
            controlSocket = new Socket(this.carIp, 1234);
            controlSocket.setReuseAddress(true);
            controlSocket.setTcpNoDelay(true);
        }

        return controlSocket;
    }

    public Socket getDepthSocket() throws IOException{
        if(depthSocket == null){
            depthSocket = new Socket(this.carIp, 50001);
            depthSocket.setReuseAddress(true);
            depthSocket.setTcpNoDelay(true);
            }
        return depthSocket;
    }

    public String getCarIp(){
        return this.carIp;
    }

    public Smartcar getSmartCar() throws IOException{
        if(smartcar == null || !smartcar.isConnected()) {
            smartcar = new Smartcar(this.getControlSocket());
        }
        return this.smartcar;
    }

    public DiscoveryListener getDiscoverListener(){
        return this.d;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
