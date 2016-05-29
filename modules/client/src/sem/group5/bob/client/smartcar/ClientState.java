package sem.group5.bob.client.smartcar;

import javafx.application.Platform;
import sem.group5.bob.client.gui.ControllerGUI;
import sem.group5.bob.client.map.FileLogger;
import sem.group5.bob.client.streaming.*;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


/**
 * Class that will track and update the state of the client UI depending on the arguments passed.
 * @see java.util.Observer
 */
public class ClientState implements Observer
{
    private ControllerGUI gui;
    private Smartcar smartcar;
    private ConnectionManager connectionManager;
    private SmartcarController smartcarController;
    private VideoStreamHandler videoHandlerDepth;
    public boolean isConnected;

    /**
     * Constructor
     * @param gui Client GUI
     */
    public ClientState(ControllerGUI gui)
    {
        this.gui = gui;
        connectionManager = new ConnectionManager();
        connectionManager.addObserver(this);
        isConnected = false;
    }

    /**
     *  Initializes the connectionManager connection
     *  see @ ConnectionManager
     */
    private void startConnection()
    {
        Thread connectionThread = new Thread(()->{
            connectionManager.connect();
        });
        connectionThread.start();
    }

    /**
     * Returns if the client is connected to the BobCar
     * @return true if the client is connected, false if not.
     */
    public boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Calls disconnect() from the connectionManager
     * see @ ConnectionManager
     */
    private void disconnect()
    {
        connectionManager.disconnect();
    }

    /**
     * Starts mapping and change the state of the client and displays the position info on the GUI.
     * @see MultiPartsParse
     * @see VideoStreamHandler
     */
    public void startMap()
    {
        try
        {
            FileLogger fileLogger = new FileLogger();

            MultiPartsParse parseDepth = new MultiPartsParse(connectionManager.getDepthSocket().getInputStream());
            TelemetryProvider telemetryProvider = new TelemetryProvider();
            parseDepth.addObserver(telemetryProvider);
            telemetryProvider.addObserver(fileLogger);

            videoHandlerDepth = new VideoStreamHandler(gui.kinectViewDepth, parseDepth);
            videoHandlerDepth.startStreaming();

            new TextPoseHandler(gui.poseInfo, parseDepth);

            gui.replaceStatus("Stream connection successful.");
        }catch (Exception e)
        {
            gui.replaceStatus("Stream connection failed.\r\n" + "Reason: " + e.getMessage());
        }
    }


    /**
     * Stops mapping by stopping the incoming stream and closes the connection socket.
     */
    public void stopMap()
    {
        // TODO: 20/05/2016
        try
        {
            videoHandlerDepth.stopStreaming();
            connectionManager.DepthSocketClose();
        } catch (IOException e)
        {
            System.err.print("Could not close DepthSocket");
        }
    }

    /**
     * getter to return the smartCarController
     * @return this SmartCarController
     */
    public SmartcarController getSmartcarController()
    {
        return smartcarController;
    }

    /**
     * Starts video streaming and updates the GUI status accordingly.
     */

    private void startStream()
    {
        try
        {
            System.out.println("Starting Video Stream");
            MultiPartsParse parseVideo = new MultiPartsParse(connectionManager.getVideoSocket().getInputStream());
            parseVideo.addObserver(this);
            VideoStreamHandler videoHandlerVideo = new VideoStreamHandler(gui.kinectViewVideo, parseVideo);
            videoHandlerVideo.startStreaming();
            gui.replaceStatus("Stream connection successful.");
        }catch (Exception e)
        {
            gui.replaceStatus("Stream connection failed.\r\n" + "Reason: " + e.getMessage());
        }

    }

    /**
     * Updates methods applied when notified by failure in the methods called for the GUI.
     * @param observable observable object
     * @param o observable object
     */
    @Override
    public void update(Observable observable, Object o)
    {
        if (o.equals("Connect") && !isConnected)
        {
            startConnection();
        }
        else if (o.equals("Disconnect") && isConnected)
        {
            try
            {
                if (gui.isMapping) gui.map.fire();
                smartcar.close();
                disconnect();
            } catch (IOException e)
            {
                gui.replaceStatus("Couldn't disconnect, reason:" + e.getMessage());
            }
        }
        else if (o.equals("Connected"))
        {
            try
            {
                this.smartcar = connectionManager.getSmartCar();
                this.smartcar.addObserver(this);
                this.smartcarController = connectionManager.getSmartcarController();
                gui.mResetArduino.fire();
                gui.replaceStatus("Connected!");
                isConnected = true;
                connectionManager.checkConnectionHeartBeat();
                Platform.runLater(()-> gui.setState("Connected"));
                gui.loadImage.setVisible(false);
                gui.setConnectClicked(false);
                startStream();
            } catch (IOException e)
            {
                gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
            }
        }
        else if (o.equals("Disconnected"))
        {
            gui.replaceStatus("Disconnected!");
            isConnected = false;
            Platform.runLater(()-> gui.setState("Disconnected"));
            gui.loadImage.setVisible(false);
            gui.setConnectClicked(false);
        }
        else if (o.equals("Fail Connecting"))
        {
            gui.replaceStatus("Couldn't connect, reason:" + connectionManager.connectionException.getMessage());
            connectionManager.reconnect();
        }
        else if (o.equals("Socket Failed"))
        {
            gui.replaceStatus("Couldn't send data to BOBCar, reason:" + smartcar.getE().getMessage());
            connectionManager.reconnect();
        }
        else if (o.equals("Error Receiving Stream"))
        {
            connectionManager.reconnect();
        }
    }
}
