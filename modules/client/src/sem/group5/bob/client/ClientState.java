package sem.group5.bob.client;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

/**
 * Created by jpp on 19/04/16.
 */
public class ClientState implements Observer {
    ControllerGUI gui;
    Smartcar smartcar;
    ConnectionManager connectionManager;
    SmartcarController smartcarController;
    VideoStreamHandler videoStreamHandler;
    boolean isConnected;

    /**
     * Constructor
     * @param gui
     */
    public ClientState(ControllerGUI gui) {
        this.gui = gui;
        connectionManager = new ConnectionManager();
        isConnected = false;
    }

    /**
     *
     */
    public void connect()
    {
        connectionManager.addObserver(this);
        Thread connectionThread = new Thread(() -> {
            try {
                connectionManager.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        connectionThread.start();
    }

    public boolean isConnected(){
        return isConnected;
    }

    public void disconnect()
    {
        connectionManager.disconnect();
    }

    public void startMap(){
        try{
            MultiPartsParse parse = new MultiPartsParse(connectionManager.getDepthSocket().getInputStream());
            videoStreamHandler = new VideoStreamHandler(gui.kinectView,  parse);
            gui.replaceStatus("Map connection successful.");
        }catch (Exception e){
            gui.replaceStatus("Map connection failed.\r\n" + "Reason: " + e.getMessage());
        }

    }

    public void stopMap(){

    }

    public SmartcarController getSmartcarController() {
        return smartcarController;
    }

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
                   gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
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
                } catch (IOException e) {
                    gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
                }
            } else if (!connectionManager.isConnected() && connectionManager.connectionException != null){
                isConnected = false;
                gui.style.styleButton(gui.connect, "");
                gui.replaceStatus("Couldn't connect, reason:" + connectionManager.connectionException.getMessage());
            } else if (!connectionManager.isConnected()){
                gui.loadImage.setVisible(false);
                gui.replaceStatus("Disconnected!");
                isConnected = false;
            }
        }
        else if (o instanceof Smartcar)
        {
            try {
                connectionManager.reconnect();
            } catch (IOException e) {
                gui.replaceStatus("Couldn't connect, reason:" + e.getMessage());
            }
        }
    }
}
