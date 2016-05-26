package sem.group5.bob.client.smartCar;

import sem.group5.bob.client.network.DiscoveryListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * Class responsible for the connections between the client and BobCar
 * @see java.util.Observable
 */
class ConnectionManager extends Observable
{
    private String carIp;
    private Socket controlSocket;
    private Socket depthSocket;
    private Socket videoSocket;
    private Smartcar smartcar;
    private SmartcarController smartcarController;
    private boolean isConnected;
    Exception connectionException;

    /**
     * Establishes the connection to BobCar.
     */
    void connect()
    {
        try
        {
            DiscoveryListener d = new DiscoveryListener();
            d.listenIp();
            this.carIp = d.getIp();

            // socket constructor
            this.controlSocket = getControlSocket();
            System.out.println("Socket Established");

            this.depthSocket = getDepthSocket();
            System.out.println("Depth Socket Established");

            this.videoSocket = getVideoSocket();
            System.out.println("Video Socket Established");

            //If an IP from the car was registered, establish a connection with the IP.
            if (!(this.carIp == null)) smartcar = new Smartcar(this.controlSocket);
            isConnected = true;

            setChanged();
            notifyObservers("Connected");
        } catch (IOException e)
        {
            connectionException = e;
            isConnected = false;
            setChanged();
            notifyObservers("Fail Connecting");
        }
    }

    /**
     * Disconnects from bobCar.
     */
    void disconnect()
    {
        try
        {
            smartcarController = null;

            if (controlSocket != null) controlSocket.close();
            controlSocket = null;
            System.out.println("Control Socket Closed!");

            depthSocket.shutdownInput();
            if (depthSocket != null) depthSocket.close();
            depthSocket = null;
            System.out.println("Depth Socket Closed!");

            videoSocket.shutdownInput();
            if (videoSocket != null) videoSocket.close();
            videoSocket = null;
            System.out.println("Depth Socket Closed!");

            isConnected = false;
            setChanged();
            notifyObservers("Disconnected");
        }catch(Exception e)
        {
            connectionException = e;
        }

    }

    /**
     * Reconnect to bobCar, called if a connection exception is caught in connect().
     * @see ConnectionManager#connect()
     * @see ConnectionManager#disconnect()
     */
    void reconnect()
    {
        disconnect();
        connect();
    }

    /**
     * Getter for smartcarController
     * @return smartcarController
     * @throws IOException
     */
    SmartcarController getSmartcarController() throws IOException
    {
        if(smartcarController == null)
        {
            smartcarController = new SmartcarController(this);
        }

        return smartcarController;
    }

    /**
     * Getter for controlSocket
     * @return controlSocket
     * @throws IOException
     */
    private Socket getControlSocket() throws IOException
    {
        if(controlSocket == null)
        {
            controlSocket = new Socket(this.carIp, 1234);
            controlSocket.setReuseAddress(true);
            controlSocket.setTcpNoDelay(true);
        }
        return controlSocket;
    }

    /**
     * Getter for the depthSocket
     * @return depthSocket
     * @throws IOException
     */
    Socket getDepthSocket() throws IOException
    {
        if(depthSocket == null)
        {
            depthSocket = new Socket(this.carIp, 50001);
            depthSocket.setReuseAddress(true);
            depthSocket.setTcpNoDelay(true);
        }
        return depthSocket;
    }

    /**
     * Getter for videoSocket.
     * @return videoSocket
     * @throws IOException
     */
    Socket getVideoSocket() throws IOException
    {
        if(videoSocket == null)
        {
            videoSocket = new Socket(this.carIp, 50002);
            videoSocket.setReuseAddress(true);
            videoSocket.setTcpNoDelay(true);
        }
        return videoSocket;
    }

    /**
     * Closes the depth socket.
     * @throws IOException
     */
    void DepthSocketClose() throws IOException
    {
        depthSocket.close();
        depthSocket = null;
    }

    /**
     * Getter for smartCar.
     * @return smartcar
     * @throws IOException
     */
    Smartcar getSmartCar() throws IOException
    {
        if(smartcar == null || !smartcar.isConnected())
        {
            smartcar = new Smartcar(this.getControlSocket());
        }
        return this.smartcar;
    }

    /**
     * Boolean checks if the car is connected
     * @return true if connected and return false if not.
     */
    private boolean isConnected()
    {
        return isConnected;
    }

    /**
     * "HeartBeat" method, checks the status of the connection by reading a string from the buffer sent from the car.
     */
    void checkConnectionHeartBeat()
    {
        Thread t = new Thread(()->{
            try
            {
                if (!getControlSocket().isClosed())
                {
                    InputStream in = getControlSocket().getInputStream();
                    String buffer = "";
                    while (isConnected())
                    {
                        buffer += (char) in.read();
                        if (!buffer.equals("A"))
                        {
                            System.out.println("Socket Marked As Dead");
                            reconnect();
                        }
                        buffer = "";
                        Thread.sleep(9*1000);
                    }
                }
            } catch (IOException | InterruptedException ignore) {}
        });
        t.start();
    }
}
