package sem.group5.bob.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

class ConnectionManager extends Observable {
    private String carIp;
    private Socket controlSocket;
    private Socket depthSocket;
    private Smartcar smartcar;
    private SmartcarController smartcarController;
    private boolean isConnected;
    Exception connectionException;

    /**
     * Method to connect to bobCar
     * @throws IOException
     */
    void connect() throws IOException {
        try {
            DiscoveryListener d = new DiscoveryListener();
            d.listenIp();
            this.carIp = d.getIp();

            this.controlSocket = getControlSocket();
            System.out.println("Socket Established");

            this.depthSocket = getDepthSocket();
            System.out.println("Depth Socket Established");

            if (!(this.carIp == null)) smartcar = new Smartcar(this.controlSocket);
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

    /**
     * Method to disconnect from bobCar
     */
    void disconnect(){
        try{
            smartcarController = null;

            controlSocket.close();
            controlSocket = null;
            System.out.println("Control Socket Closed!");

            depthSocket.close();
            depthSocket = null;
            System.out.println("Depth Socket Closed!");

            isConnected = false;
            setChanged();
            notifyObservers(this);
        }catch(Exception e){
            connectionException = e;
        }

    }

    /**
     * Method to reconnect to bobCar
     */
    void reconnect()
    {
        try {
            disconnect();
            connect();
        } catch (IOException e) {
            connectionException = e;
        }
    }

    /**
     * Method to get smartcarController
     * @return smartcarController
     * @throws IOException
     */
    SmartcarController getSmartcarController() throws IOException
    {
        if(smartcarController == null){
            smartcarController = new SmartcarController(this);
        }

        return smartcarController;
    }

    /**
     * Method to get controlSocket
     * @return controlSocket
     * @throws IOException
     */
    private Socket getControlSocket() throws IOException{
        if(controlSocket == null){
            controlSocket = new Socket(this.carIp, 1234);
            controlSocket.setReuseAddress(true);
            controlSocket.setTcpNoDelay(true);
        }
        return controlSocket;
    }

    /**
     * Method to get depthSocket
     * @return depthSocket
     * @throws IOException
     */
    Socket getDepthSocket() throws IOException{
        if(depthSocket == null){
            depthSocket = new Socket(this.carIp, 50001);
            depthSocket.setReuseAddress(true);
            depthSocket.setTcpNoDelay(true);
            }
        return depthSocket;
    }

    /**
     * Method to get smartcar
     * @return smartcar
     * @throws IOException
     */
    Smartcar getSmartCar() throws IOException{
        if(smartcar == null || !smartcar.isConnected()) {
            smartcar = new Smartcar(this.getControlSocket());
        }
        return this.smartcar;
    }

    /**
     * Method to tell if it is connected to bobCar
     * @return isConnected
     */
    boolean isConnected() {
        return isConnected;
    }

    /**
     *
     */
    void checkConnectionHeartBeat(){
        Thread t = new Thread(()->{
            try {
                while (!getControlSocket().isClosed())
                {
                    InputStream in = getControlSocket().getInputStream();
                    String buffer = "";
                    while (in.available() > 0)
                    {
                        buffer += (char) in.read();
                        System.out.println(buffer);
                        if (!buffer.equals("Active"))
                        {
                            reconnect();
                        }
                        buffer = "";
                        System.out.println("Server Still Active!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }
}
