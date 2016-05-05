package sem.group5.bob.client;

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
    private void connect()
    {
        Thread connectionThread = new Thread(() -> {
           connectionManager.findServerIp();
        });
        connectionThread.start();
    }

    /**
     *  Method to return if the client is connected to the BobCar
     * @return if the client is connect
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
     * Method to stop the depth streaming
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
    public void update(Observable observable, Object o) {
        if (o instanceof ControllerGUI)
        {
           if (!isConnected)
           {
                connect();
           }
           else
           {
               try {
                   smartcar.close();
                   disconnect();
               } catch (IOException e) {
                   gui.replaceStatus("Couldn't disconnect, reason:" + e.getMessage());
               }
           }
        }

        else if(o instanceof ConnectionManager)
        {
            if (connectionManager.isConnected()) {
                gui.loadImage.setVisible(false);
                try {
                    this.smartcar = connectionManager.getSmartCar();
                    this.smartcar.addObserver(this);
                    this.smartcarController = connectionManager.getSmartcarController();
                    gui.replaceStatus("Connected!");
                    isConnected = true;
                    gui.stream();
                    connectionManager.checkConnectionHeartBeat();
                } catch (IOException e) {
                    gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
                }

            } else if (!connectionManager.isConnected() && connectionManager.connectionException != null){
                isConnected = false;
                gui.style.styleButton(gui.connect, "");
                gui.replaceStatus("Couldn't connect, reason:" + connectionManager.connectionException.getMessage());
                connectionManager.reconnect();

            } else if (!connectionManager.isConnected()){
                gui.loadImage.setVisible(false);
                gui.replaceStatus("Disconnected!");
                isConnected = false;
            }
        }

        else if (o instanceof Smartcar)
        {
            gui.replaceStatus("Couldn't send data to BOBCar, reason:" + smartcar.getE().getMessage());
            connectionManager.reconnect();
        }
    }
}
