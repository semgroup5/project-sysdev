package sem.group5.bob.car;

import org.openkinect.freenect.*;
import sem.group5.bob.car.network.DiscoveryBroadcaster;
import sem.group5.bob.car.streaming.DepthJpegProvider;
import sem.group5.bob.car.streaming.MjpegStreamer;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class BobCarObserver implements Observer {
    private static ServerSocket serverSocket = null;
    private SmartCarComm scc;
    private SerialConnect serialC;
    private Socket socket;

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof RemoteControlListener) {
            startRemoteListener(scc);
            startDiscoveryListener();
        } else if (arg instanceof SmartCarComm) {
            restartSerialConnection();
        } else if (arg instanceof MjpegStreamer) {
            streamVideo();
        }
    }

    void observe() {
        startSerialConnection();

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
        DiscoveryBroadcaster d = new DiscoveryBroadcaster(this);
        Thread thread = new Thread(d);
        thread.run();
    }

    /**
     * Method to initiate the port listener that will be waiting for inputs from the client side to forward it then to the arduino.
     *
     */
    private void startRemoteListener(SmartCarComm scc)
    {
        System.out.println("Starting Remote Listener");
        try{
            RemoteControlListener rcl = new RemoteControlListener(1234, scc, this);
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
        int port = 50001;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            socket = serverSocket.accept();
            socket.setTcpNoDelay(true);
            socket.setReuseAddress(true);
            System.out.println("Stream socket established");
        }
        catch(Exception e) {
            System.out.println("Couldn't Create Socket");
            e.printStackTrace();
        }

        Thread streamThread = new Thread(() -> {
            System.out.println("Starting video streamer");
            Context context = Freenect.createContext();
            Device d = context.openDevice(0);
            d.setDepthFormat(DepthFormat.D11BIT);
            d.setVideoFormat(VideoFormat.RGB);


            try {
                System.out.println( "Starting Video Stream" );

                DepthJpegProvider depthJpegProvider = new DepthJpegProvider();
                //d.startVideo(depthJpegProvider::receiveVideo);
                d.startDepth(depthJpegProvider::receiveDepth);
                Thread.sleep(1000);

                MjpegStreamer mjpegStreamer = new MjpegStreamer(socket,serverSocket, depthJpegProvider, this);
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

    private void startSerialConnection(){
        serialC = new SerialConnect();
        serialC.initialize();
        BufferedReader in = serialC.getBufferReader();
        OutputStream out = serialC.getOutputStream();
        scc = new SmartCarComm(in, out, this);
        scc.addObserver(this);
    }

    private void restartSerialConnection() {
        serialC.close();
        scc = null;
        startSerialConnection();
    }
}
