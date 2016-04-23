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
    boolean isConnected = false;

    public ClientState(ControllerGUI gui) {
        this.gui = gui;
        connectionManager = new ConnectionManager();
    }

    public void connect()
    {
        connectionManager.addObserver(this);
        Thread connectionThread = new Thread(new Runnable(){
            @Override
            public void run() {
                connectionManager.connect();
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
            VideoStreamHandler vsh = new VideoStreamHandler(gui.kinectView,  parse);
            gui.replaceStatus("Map connection successful.");
        }catch (Exception e){
            gui.replaceStatus("Map connection failed.\r\n" + "Reason: " + e.getMessage());
        }

    }

    public void stopMap(){

    }

    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof ConnectionManager)
        {
            gui.loadImage.setVisible(false);
            if(!connectionManager.isConnected){
                gui.replaceStatus("Connected");
            }else{
                if(connectionManager.connectionException != null){
                    gui.replaceStatus("Couldn't connect, reason:" + connectionManager.connectionException.getMessage());
                }

            }
        }
    }
}
