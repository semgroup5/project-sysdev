package sem.group5.bob.car.streaming;

import sem.group5.bob.car.BobCarConnectionManager;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;

/**
 * Class responsible for sending video or depth stream to the client.
 */
public class MjpegStreamer extends Observable implements Runnable{
    private DepthJpegProvider cjp;
    private Socket socket;

    /**
     *  Constructor
     * @param s socket used for communication
     * @param cjp responsible for selecting which frames will be send to the client.
     */
    public MjpegStreamer(Socket s, DepthJpegProvider cjp) {
        this.socket = s;
        this.cjp = cjp;
    }

    /**
     * Function that sends hte latest frames to client with boundaries so the client will be able to organize them. In case of
     * any error is found while streaming it will notify the class BobCarConnectionManager.
     * @see DepthJpegProvider
     * @see BobCarConnectionManager
     */
    private void stream()
    {
        try {
        System.out.println("Streaming");
        OutputStream out = socket.getOutputStream();

        out.write( ( "HTTP/1.0 200 OK\r\n" +
                         "Server: YourServerName\r\n" +
                         "Connection: close\r\n" +
                         "Max-Age: 0\r\n" +
                         "Expires: 0\r\n" +
                         "Cache-Control: no-cache, private\r\n" +
                         "Pragma: no-cache\r\n" +
                         "Content-Type: multipart/x-motion-jpeg; " +
                         "boundary=BoundaryString\r\n\r\n" ).getBytes() );
        byte[] data;
        while (!socket.isClosed()) {
            System.out.println("Sending frame");
            data = cjp.getLatestJpeg();
            out.write(("--BoundaryString\r\n" +
                    "Content-type: image/jpeg\r\n" +
                    "Content-Length: " + data.length + "\r\n\r\n").getBytes());
            out.write(data);
            out.write("\r\n\r\n".getBytes());
            out.flush();
        }
        } catch (Exception e) {
            e.printStackTrace();
//            setChanged();
//            notifyObservers();
        }

    }

    /**
     * Function used by the thread to run the stream.
     */
    public void run()
    {
            this.stream();
    }
}
