package sem.group5.bob.client;

import javafx.application.Platform;
import sem.group5.bob.client.bobSmartCar.Smartcar;
import sem.group5.bob.client.bobSmartCar.SmartcarController;
import sem.group5.bob.client.mappGenerator.LogToFile;
import sem.group5.bob.client.streamReceiver.MultiPartsParse;
import sem.group5.bob.client.streamReceiver.ScanLineGenerator;
import sem.group5.bob.client.streamReceiver.TextPoseHandler;
import sem.group5.bob.client.streamReceiver.VideoStreamHandler;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


/**
 * Class that will track and update the state of the client depending on the arguments passed.
 * @see java.util.Observer
 */
class ClientState implements Observer
{
    private ControllerGUI gui;
    private Smartcar smartcar;
    private ConnectionManager connectionManager;
    private SmartcarController smartcarController;
    private ScanLineGenerator scanLineGenerator;
    private VideoStreamHandler videoHandlerDepth;
    boolean isConnected;
    private TextPoseHandler poseHandler;

    /**
     * Constructor
     * @param gui Client GUI
     */
    ClientState(ControllerGUI gui)
    {
        this.gui = gui;
        connectionManager = new ConnectionManager();
        connectionManager.addObserver(this);
        isConnected = false;
    }

    /**
     *  Method that initializes the connectionManager connection
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
     *  Method to return if the client is connected to the BobCar
     * @return true if the client is connected
     */
    boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Method that call disconnect from the connectionManager
     * see @ ConnectionManager
     */
    private void disconnect()
    {
        connectionManager.disconnect();
    }

    /**
     * Method that starts mapping and change the state of the client to reflect the changes.
     * @see MultiPartsParse
     * @see VideoStreamHandler
     * @see ScanLineGenerator
     */
    void startMap()
    {
        try
        {
            MultiPartsParse parseDepth = new MultiPartsParse(connectionManager.getDepthSocket().getInputStream());
            scanLineGenerator = new ScanLineGenerator();
            parseDepth.addObserver(scanLineGenerator);
            videoHandlerDepth = new VideoStreamHandler(gui.kinectViewDepth, parseDepth);
            videoHandlerDepth.startStreaming();
            poseHandler = new TextPoseHandler(gui.poseInfo, parseDepth);
            LogToFile CarmenLog = new LogToFile();
            parseDepth.setLog(CarmenLog);
            scanLineGenerator.setLog(CarmenLog);
            gui.replaceStatus("Stream connection successful.");
        }catch (Exception e)
        {
            gui.replaceStatus("Stream connection failed.\r\n" + "Reason: " + e.getMessage());
        }
    }


    /**
     * Method to stop mapping from the client side
     */
    void stopMap()
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
     * Method to return the smartCarController
     * @return this SmartCarController
     */
    SmartcarController getSmartcarController()
    {
        return smartcarController;
    }

    /**
     * Method that starts the video streaming
     */

    void startStream()
    {
        try
        {
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
     *  Method to do the necessary updates when notified by the observable objects
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
                gui.replaceStatus("Connected!");
                isConnected = true;
                gui.stream();
                connectionManager.checkConnectionHeartBeat();
                Platform.runLater(()-> gui.setState("Connected"));
                gui.loadImage.setVisible(false);
                gui.setConnectClicked(false);
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
