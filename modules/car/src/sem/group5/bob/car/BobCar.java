package sem.group5.bob.car;

import org.openkinect.freenect.*;
import sem.group5.bob.car.network.DiscoveryBroadcaster;
import sem.group5.bob.car.streaming.DepthJpegProvider;
import sem.group5.bob.car.streaming.MjpegStreamer;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.ServerSocket;

/**
 * Main class to run application in the raspberry side
 * Created by jpp on 30/03/16.
 */
public class BobCar {
    private static ServerSocket serverSocket = null;

    public static void main(String[] args)
    {

        SerialConnect serialC = new SerialConnect();
        serialC.initialize();
        BufferedReader in = serialC.getBufferReader();
        OutputStream out = serialC.getOutputStream();
        SmartCarComm scc = new SmartCarComm(in, out);

        System.out.println("Starting remote listener");
        startRemoteListener(scc);

        System.out.println("Starting IP address broadcast");
        startDiscoveryListener();

        System.out.println("Starting video streamer");
        try {
            streamVideo();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not start video streaming");
        }


    }

    /**
     * Method to initiate the broadcasting IP
     */
    static void startDiscoveryListener() {
        DiscoveryBroadcaster d = new DiscoveryBroadcaster();
        d.startIPBroadcast();
    }

    /**
     * Method to initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     *
     */
    private static void startRemoteListener(SmartCarComm scc)
    {
        try{
            RemoteControlListener rcl = new RemoteControlListener(1234, scc);
            Thread t = new Thread(rcl);
            t.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to initiate the video streaming
     */
    private static void streamVideo()
    {
        Context context = Freenect.createContext();
        Device d = context.openDevice(0);
        d.setDepthFormat(DepthFormat.D11BIT);
        d.setVideoFormat(VideoFormat.RGB);
        int port = 50001;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        }
        catch(Exception e) {
            System.out.println("Couldn't create socket");
            System.exit(1);
        }

        try {
            System.out.println( "Starting Video Stream, " );

            DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
            //d.startVideo(depthJpegProvider::receiveVideo);
            d.startDepth(depthJpegProvider::receiveDepth);
            Thread.sleep(1000);

            MjpegStreamer mjpegStreamer = new MjpegStreamer(serverSocket, depthJpegProvider);
            Thread t = new Thread(mjpegStreamer);
            t.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
