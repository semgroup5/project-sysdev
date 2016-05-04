package sem.group5.bob.car;

import org.openkinect.freenect.*;
import sem.group5.bob.car.network.DiscoveryBroadcaster;
import sem.group5.bob.car.streaming.DepthJpegProvider;
import sem.group5.bob.car.streaming.MjpegStreamer;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;
/**
 * This class is notified when an observed object is changed and updates the object with specific methods.
 */

public class BobCarObserver implements Observer {
    private SmartCarComm scc;
    private SerialConnect serialC;
    private RemoteControlListener rcl;
    private DepthStreamSocket depthStreamSocket;


    @Override
    public void update(Observable o, Object arg) {

        // If an error encountered with the RemoteControlCListener
        if (arg instanceof RemoteControlListener) {

            //close the depth stream socket
            depthStreamSocket.closeSocketDepthStream();

            //restart method for this operation (further explained in )
            startFuntions();

            // If an error is encountered with the connection forwarding the client commands to the arduino
        } else if (arg instanceof SmartCarComm) {
            restartSerialConnection();

            // If an error is encountered with getting and sending jpeg frames
            // or within the depthstream connection
        } else if (arg instanceof MjpegStreamer || arg instanceof DepthStreamSocket) {

            //close remote control listener connection
            rcl.closeConnections();

            // close depth stream socket.
            depthStreamSocket.closeSocketDepthStream();

            //start connection operation (this function is specified in a method below)
            startFuntions();
        }
    }

    void observe() {
        startSerialConnection();

        startFuntions();
    }

    /**
     * The startFunction method used to update the observable and restart the connection
     */
    private void startFuntions()
    {
        startDiscoveryListener();

        startRemoteListener(scc);

        try {
            streamVideo();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could Not Start Video Streaming");
        }
    }

    /**
     * Method to initiate the broadcasting IP
     */
    private void startDiscoveryListener() {
        System.out.println("Starting IP Address Broadcast");
        DiscoveryBroadcaster d = new DiscoveryBroadcaster();
        Thread thread = new Thread(d);
        thread.run();
    }

    /**
     * Method to initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     */
    private void startRemoteListener(SmartCarComm scc)
    {
        System.out.println("Starting Remote Listener");
        try{
            rcl = new RemoteControlListener(1234, scc);
            rcl.addObserver(this);
            Thread t = new Thread(rcl);
            t.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to initiate the video streaming
     */
    private void streamVideo()
    {

        Thread streamThread = new Thread(() -> {

            depthStreamSocket = new DepthStreamSocket();

            Device d = null;
            try {
                System.out.println("Starting video streamer");
                Context context = Freenect.createContext();
                d = context.openDevice(0);
                d.setDepthFormat(DepthFormat.MM);
                d.setVideoFormat(VideoFormat.RGB);
            }catch (Exception e){
                rcl.closeConnections();
            }

            try {
                System.out.println( "Starting Video Stream" );

                DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
                //d.startVideo(depthJpegProvider::receiveVideo);
                if (d != null) {
                    d.startDepth(depthJpegProvider::receiveDepth);
                }
                Thread.sleep(1000);

                MjpegStreamer mjpegStreamer = new MjpegStreamer(depthStreamSocket.getSocket(), depthJpegProvider, this);
                Thread t = new Thread(mjpegStreamer);
                t.start();
            }
            catch(Exception e){
                System.out.println("Could Not Start Video Stream");
                rcl.closeConnections();
                e.printStackTrace();
            }
        });
        streamThread.start();

    }

    /**
     *
     */
    private void startSerialConnection(){
        serialC = new SerialConnect();
        serialC.initialize();
        BufferedReader in = serialC.getBufferReader();
        OutputStream out = serialC.getOutputStream();
        scc = new SmartCarComm(in, out);
        scc.addObserver(this);
    }

    /**
     *
     */
    private void restartSerialConnection() {
        serialC.close();
        scc = null;
        startSerialConnection();
    }

}
