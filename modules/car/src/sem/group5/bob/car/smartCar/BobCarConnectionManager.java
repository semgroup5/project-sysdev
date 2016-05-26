package sem.group5.bob.car.smartcar;

import org.openkinect.freenect.*;
import sem.group5.bob.car.network.DiscoveryBroadcaster;
import sem.group5.bob.car.streaming.*;
import java.io.OutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages the connections on the car components.
 * @see java.util.Observer
 * @see java.util.Observable
 */
public class BobCarConnectionManager extends Observable implements Observer
{

    private SmartCarComm scc;
    private SerialConnect serialC;
    private RemoteControlListener rcl;
    private DepthVideoStreamSocket depthSocket;
    private DepthVideoStreamSocket videoSocket;
    public static  Device device;
    private Context context;
    private DepthStreamer depthStreamer;
    private VideoStreamer videoStreamer;
    private Thread depthThread;
    private Thread videoThread;


    /**
     * Updates an observed connection status.
     * This is called by the notifyObservers() from Observable.
     * @param o observable object
     * @param arg the argument
     * @see BobCarConnectionManager
     * @see BobCarConnectionManager#restartSerialConnection()
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals("Connection Closed"))
        {
//            try
//            {
//                videoStreamer.setStreaming(false);
//                depthStreamer.setStreaming(false);
//                if (depthThread.isAlive())depthThread.interrupt();
//                if (videoThread.isAlive())videoThread.interrupt();
//            } catch (IOException e)
//            {
//                System.out.println("Could Not Stop Stream");
//            }
//
//            System.out.println("Closing Stream");
//            depthSocket.closeSocketStream();
//            videoSocket.closeSocketStream();
//
//            try
//            {
//                System.out.println("Shutting Down Device");
//                if (context != null)
//                {
//                    if (device != null)
//                    {
//                        device.setLed(LedStatus.BLINK_GREEN);
//                        device.setTiltAngle(0);
//                        device.stopDepth();
//                        device.stopVideo();
//                        device.close();
//                    }
//                    context.shutdown();
//                }
//            } catch (Exception ignore) {}
            startFunctions();
        }
        else if (arg.equals("Serial Port Failed"))
        {
            restartSerialConnection();
        }
        else if (arg.equals("Error Streaming"))
        {
            rcl.closeConnections();
        }
        else if (arg.equals("Kinect Ready"))
        {
            device.setLed(LedStatus.BLINK_RED_YELLOW);
            streamVideo();
        }
    }

    /**
     * initialize() is called after update() is called on an observed object.
     * @see BobCarConnectionManager#startFunctions()
     * @see BobCarConnectionManager#startSerialConnection()
     */
    public void initialize()
    {
        addObserver(this);

        startSerialConnection();

        startFunctions();
    }

    /**
     * The startFunction() method initialize a set of methods, its called in initialize() when the observable object is updated.
     * @see BobCarConnectionManager#startRemoteListener(SmartCarComm)
     * @see BobCarConnectionManager#setDepthStreamSocket()
     * @see BobCarConnectionManager#startDiscoveryListener()
     * @see BobCarConnectionManager#kinectSetting()
     */
    private void startFunctions()
    {
        startRemoteListener(this.scc);

        setDepthStreamSocket();

        setVideoStreamSocket();

        startDiscoveryListener();

//        kinectSetting();

    }

    /**
     * Start a Depth Stream Socket
     * @see DepthVideoStreamSocket
     */
    private void setDepthStreamSocket()
    {
        Thread t = new Thread(()->{
            depthSocket = new DepthVideoStreamSocket(50001);
        });
        t.start();
    }

    /**
     * Start a Video Stream Socket
     * @see DepthVideoStreamSocket
     */
    private void setVideoStreamSocket()
    {
        Thread t = new Thread(()->{
            videoSocket = new DepthVideoStreamSocket(50002);
        });
        t.start();
    }

    /**
     * Initiate broadcasting BobCar's IP and adds an observer.
     * @see DiscoveryBroadcaster
     * @see java.util.Observer
     */
    private void startDiscoveryListener()
    {
        System.out.println("Starting IP Address Broadcast");
        DiscoveryBroadcaster d = new DiscoveryBroadcaster();
        rcl.addObserver(d);
        Thread thread = new Thread(d);
        thread.run();
    }

    /**
     * Initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     * @param scc smartCarComm
     * @see SmartCarComm
     * @see RemoteControlListener#RemoteControlListener(int, SmartCarComm)
     */
    private void startRemoteListener(SmartCarComm scc)
    {
        System.out.println("Starting Remote Listener");
        try
        {
            rcl = new RemoteControlListener(1234, scc);
            rcl.addObserver(this);
            Thread t = new Thread(rcl);
            t.start();
        } catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Sets the settings to the Kinect.
     * @see org.openkinect.freenect
     */
    private void kinectSetting()
    {
        device = null;
        while (device == null)
        {
            try
            {
                System.out.println("Setting Up Kinect");
                rcl.timer.reset();
                context = Freenect.createContext();
                System.out.println("Opening Device");
                rcl.timer.reset();
                device = context.openDevice(0);
                System.out.println("Setting Depth Format");
                rcl.timer.reset();
                device.setDepthFormat(DepthFormat.MM, Resolution.MEDIUM);
                System.out.println("Setting Video Format");
                rcl.timer.reset();
                device.setVideoFormat(VideoFormat.IR_10BIT, Resolution.MEDIUM);
                setChanged();
                notifyObservers("Kinect Ready");

            }catch (Exception e)
            {
                try
                {
                    System.out.println("Kinect Resetting");
                    System.out.println("Shutting Down Device");
                    if (context != null) {
                        if (device != null){
                            device.close();
                        }
                        context.shutdown();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    /**
     * Threaded method to start the video streaming.
     * @see DepthStreamer
     */
    private void streamVideo()
    {
        try
        {
            System.out.println( "Starting Video Stream" );

            DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
            VideoProvider videoProvider = new VideoProvider();
            PoseManager poseManager = new PoseManager();
            serialC.addObserver(poseManager);

            if (device != null)
            {
                device.startDepth(depthJpegProvider::receiveDepth);
                device.startVideo(videoProvider::receiveVideo);
            }

            depthStreamer = new DepthStreamer(depthSocket.getSocket(), depthJpegProvider, poseManager);
            depthStreamer.addObserver(this);
            videoStreamer = new VideoStreamer(videoSocket.getSocket(), videoProvider);
            videoStreamer.addObserver(this);
            depthThread = new Thread(depthStreamer);
            depthThread.start();
            videoThread = new Thread(videoStreamer);
            videoThread.start();
        }
        catch(Exception e)
        {
            System.out.println("Could Not Start Stream");
        }
    }

    /**
     * Initiates serial connection with the arduino
     * @see SerialConnect
     */
    private void startSerialConnection()
    {
        serialC = new SerialConnect();
        serialC.initialize();
        OutputStream out = serialC.getOutputStream();
        scc = new SmartCarComm(out);
        scc.addObserver(this);
    }

    /**
     * Restart serial connection if the first attempt fails.
     * @see SerialConnect
     */
    private void restartSerialConnection()
    {
        serialC.close();
        scc = null;
        startSerialConnection();
    }

}
