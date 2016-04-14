package sem.group5.bob.car;

import org.openkinect.freenect.*;
import sem.group5.bob.car.network.DiscoveryBroadcaster;
import sem.group5.bob.car.streaming.DepthJpegProvider;
import sem.group5.bob.car.streaming.MjpegStreamer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class to run application in the raspberry side
 * Created by jpp on 30/03/16.
 */
public class BobCar {
    static BufferedReader in;
    static OutputStream out;
    static Boolean waiting = false;
    static ServerSocket server = null;
    static DiscoveryBroadcaster d;

    public static void main(String[] args)
    {
        System.out.println("Starting IP address broadcast");
        startDiscoveryListener();

        SerialConnect serialC = new SerialConnect();
        serialC.initialize();
        in = serialC.getBufferReader();
        out = serialC.getOutputStream();

        System.out.println("Starting remote listener");
        startRemoteListener(in, out);

//        System.out.println("Starting video streamer");
//        try {
//            streamVideo();
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Could not start video streaming");
//        }
    }

    /**
     * Method to initiate the broadcasting IP
     */
    public static void startDiscoveryListener() {
        d = new DiscoveryBroadcaster();
        d.startBroadcast();
    }

    /**
     * Method to initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     * @param in
     * @param out
     */
    public static void startRemoteListener(BufferedReader in, OutputStream out)
    {
        try{
            RemoteControlListener rcl = new RemoteControlListener(1234, new SmartCarComm(in, out));
            Thread t = new Thread(rcl);
            t.run();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to initiate the video streaming
     */
    public static void streamVideo()
    {
        Context context = Freenect.createContext();
        Device d = context.openDevice(0);
        Socket socket = null;
        d.setDepthFormat(DepthFormat.D11BIT);
        d.setVideoFormat(VideoFormat.RGB);
        int port = 50001;

        try {
            server = new ServerSocket(port);
        }
        catch(Exception e) {
            System.out.println("Couldn't create socket");
            System.exit(1);
        }

        try {
            System.out.println("Listening on port " + port);
            socket = server.accept();
        }
        catch(IOException e) {
            System.err.println("Could not listen on port:" + port);
            System.exit(1);
        }

        try {
            System.out.println( "Starting Video Stream, " );

            DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
            //d.startVideo(depthJpegProvider::receiveVideo);
            d.startDepth(depthJpegProvider::receiveDepth);
            Thread.sleep(1000);

            MjpegStreamer mjpegStreamer = new MjpegStreamer(socket, depthJpegProvider);
            Thread t = new Thread(mjpegStreamer);
            t.run();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
