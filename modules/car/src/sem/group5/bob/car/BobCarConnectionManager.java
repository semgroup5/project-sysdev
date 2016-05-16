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
 * @see java.util.Observer
 */

public class BobCarConnectionManager implements Observer {
    private SmartCarComm scc;
    private SerialConnect serialC;
    private RemoteControlListener rcl;
    private DepthStreamSocket depthStreamSocket;
    private DiscoveryBroadcaster d;
    private  Device device;

    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param o observable object
     * @param arg the argument
     * @see BobCarConnectionManager#restartFunctions()
     * @see BobCarConnectionManager#restartSerialConnection()
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals("Connection Closed"))
        {
            depthStreamSocket.closeSocketDepthStream();

            restartFunctions();
        }
        else if (arg.equals("Serial Port Failed"))
        {
            restartSerialConnection();
        }
        else if (arg instanceof MjpegStreamer || arg instanceof DepthStreamSocket)
        {
            //rcl.closeConnections();
        }
    }

    /**
     * initialize() is called after update() is called on an observed object.
     * @see BobCarConnectionManager#startFunctions()
     * @see BobCarConnectionManager#startSerialConnection()
     */
    void initialize() {
        startSerialConnection();

        startFunctions();
    }

    /**
     * The startFunction() method initialize a set of methods, its called in initialize() method when the observable object is updated.
     * @see BobCarConnectionManager#startRemoteListener(SmartCarComm)
     * @see BobCarConnectionManager#setDepthStreamSocket()
     * @see BobCarConnectionManager#startDiscoveryListener()
     * @see BobCarConnectionManager#kinectSetting()
     */
    private void startFunctions()
    {
        startRemoteListener(this.scc);

        setDepthStreamSocket();

        startDiscoveryListener();

        kinectSetting();

        try {
            streamVideo();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could Not Start Video Streaming");
        }
    }

    /**
     * restart functions if an error was encountered in
     * @see BobCarConnectionManager#startFunctions()
     */
    private void restartFunctions()
    {
        startRemoteListener(scc);

        setDepthStreamSocket();

        startDiscoveryListener();

        try {
            streamVideo();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could Not Start Video Streaming");
        }
    }

    /**
     * Constructor
     * Start a Depth Stream Socket
     * @see DepthStreamSocket#DepthStreamSocket()
      */
    private void setDepthStreamSocket() {
        Thread t = new Thread(()->{
            depthStreamSocket = new DepthStreamSocket();
        });
        t.start();
    }

    /**
     * Method to initiate broadcasting BobCar's IP and adds an observer.
     * @see DiscoveryBroadcaster
     * @see java.util.Observer
     */
    private void startDiscoveryListener() {
        System.out.println("Starting IP Address Broadcast");
        d = new DiscoveryBroadcaster();
        rcl.addObserver(d);
        Thread thread = new Thread(d);
        thread.run();
    }

    /**
     * Method to initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     * @param scc smartCarComm
     * @see SmartCarComm
     * @see RemoteControlListener#RemoteControlListener(int, SmartCarComm)
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
     * Sets the settings to the Kinect.
     * @see org.openkinect.freenect
     */
    private void kinectSetting()
    {
            try {
                System.out.println("Starting video streamer");
                Context context = Freenect.createContext();
                System.out.println(1);
                device = context.openDevice(0);
                System.out.println(2);
                device.setDepthFormat(DepthFormat.MM);
                System.out.println(3);
                device.setVideoFormat(VideoFormat.RGB);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     * Threaded method to start the video streaming.
     * Catch and logs errors
     * @see MjpegStreamer
     */
    private void streamVideo()
    {

        Thread streamThread = new Thread(() -> {

            try {
                System.out.println( "Starting Video Stream" );

                DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
                Pose poseProvider = new Pose();
                //d.startVideo(depthJpegProvider::receiveVideo);
                if (device != null) {
                    device.startDepth(depthJpegProvider::receiveDepth);
                }
                Thread.sleep(1000);

                MjpegStreamer mjpegStreamer = new MjpegStreamer(depthStreamSocket.getSocket(), depthJpegProvider, poseProvider );
                mjpegStreamer.addObserver(this);
                Thread t = new Thread(mjpegStreamer);
                t.start();
            }
            catch(Exception e){
                System.out.println("Could Not Start Video Stream");
                e.printStackTrace();
            }
        });
        streamThread.start();

    }

    /**
     * Initiates serial connection with the arduino
     * @see SerialConnect#
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
     * Restart serial connection if the first attempt fails.
     * @see BobCarConnectionManager#startSerialConnection()
     */
    private void restartSerialConnection() {
        serialC.close();
        scc = null;
        startSerialConnection();
    }

}
