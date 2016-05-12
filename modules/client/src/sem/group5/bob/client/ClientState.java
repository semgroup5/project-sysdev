package sem.group5.bob.client;

import javafx.application.Platform;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


class ClientState implements Observer {
    private ControllerGUI gui;
    private Smartcar smartcar;
    private ConnectionManager connectionManager;
    private SmartcarController smartcarController;
    private MultiPartsParse parse;
    private VideoStreamHandler videoHandler;
    private ScanLineGenerator scanLineGenerator;
    Thread parseThread;
    boolean isConnected;

    /**
     * Constructor
     * @param gui Client GUI
     */
    ClientState(ControllerGUI gui) {
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
    boolean isConnected(){
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
     * Method that starts mapping
     */
    //TODO
    void startMap(){
        try{
            parse = new MultiPartsParse(connectionManager.getDepthSocket().getInputStream());
            videoHandler = new VideoStreamHandler(gui.kinectView, parse);
            scanLineGenerator = new ScanLineGenerator();
            parse.addObserver(scanLineGenerator);
            parseThread = new Thread(parse);
            parseThread.start();
            gui.replaceStatus("Map connection successful.");
        }catch (Exception e){
            gui.replaceStatus("Map connection failed.\r\n" + "Reason: " + e.getMessage());
        }
    }

    /**
     * Method to stop mapping
     */
    //TODO
    void stopMap(){
        try {
            connectionManager.DepthSocketCloser();
            parseThread.interrupt();
            videoHandler = null;
            parse = null;
        } catch (IOException e) {
            System.err.print("Could not close DepthSocket");
        }
    }

    /**
     * Method to return the smartCarController
     * @return this SmartCarController
     */
    SmartcarController getSmartcarController() {
        return smartcarController;
    }

    /**
     * Method that starts the depth streaming
     */

    void startStream(){
        try{
            parse = new MultiPartsParse(connectionManager.getDepthSocket().getInputStream());
            videoHandler = new VideoStreamHandler(gui.kinectView, parse);
            videoHandler.startStreaming();
            gui.replaceStatus("Stream connection successful.");
        }catch (Exception e){
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
            try {
                smartcar.close();
                disconnect();
            } catch (IOException e) {
                gui.replaceStatus("Couldn't disconnect, reason:" + e.getMessage());
            }
        }
        else if (o.equals("Connected"))
        {
            try {
                this.smartcar = connectionManager.getSmartCar();
                this.smartcar.addObserver(this);
                this.smartcarController = connectionManager.getSmartcarController();
                gui.replaceStatus("Connected!");
                isConnected = true;
                Platform.runLater(() -> gui.setState("Connected"));
                gui.stream();
                connectionManager.checkConnectionHeartBeat();
                gui.loadImage.setVisible(false);
                gui.setConnectClicked(false);
            } catch (IOException e) {
                gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
            }
        }
        else if (o.equals("Disconnected"))
        {
            gui.replaceStatus("Disconnected!");
            isConnected = false;
            Platform.runLater(() -> gui.setState("Disconnected"));
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
    }
}
