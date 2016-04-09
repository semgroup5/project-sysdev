package bob.car;

import org.openkinect.freenect.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jpp on 30/03/16.
 */
public class BobCar {
    static InputStream in;
    static OutputStream out;
    static Boolean waiting = false;
    static ServerSocket server = null;

    public void streamVideo()
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
