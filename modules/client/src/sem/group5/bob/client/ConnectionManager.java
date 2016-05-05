package sem.group5.bob.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

class ConnectionManager extends Observable implements Observer{
    private String carIp;
    private Socket controlSocket;
    private Socket depthSocket;
    private Smartcar smartcar;
    private SmartcarController smartcarController;
    private boolean isConnected;
    private DiscoveryListener d;
    Exception connectionException;

    ConnectionManager() {
        d = new DiscoveryListener();
        addObserver(this);
    }

    /**
     *
     */
    void findServerIp() {
        d.listenIp();
    }

    /**
     * Method to connect to bobCar
     * @throws IOException
     */
    void connect() throws IOException {
        try {
            this.carIp = d.getIp();
            // Assigns a socket connection
            this.controlSocket = getControlSocket();
            System.out.println("Socket Established");
            this.depthSocket = getDepthSocket();
            System.out.println("Depth Socket Established");

            //If an IP from the car was registered, establish a connection with the IP.
            if (!(this.carIp == null))
                smartcar = new Smartcar(this.controlSocket);
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

            if (controlSocket != null) controlSocket.close();
            controlSocket = null;
            System.out.println("Control Socket Closed!");

            if (depthSocket != null) depthSocket.close();
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

    void DepthSocketCloser() throws IOException{
        depthSocket.close();
        depthSocket = null;
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
                        if (!buffer.equals("A"))
                        {
                            reconnect();
                        }
                        buffer = "";
                    }
                    //Thread.sleep(1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
           // } catch (InterruptedException e){
                //
            }
        });
        t.start();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg.equals("IP Found")) {
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (arg.equals("Time Out")) {
            disconnect();
        }
    }
}
