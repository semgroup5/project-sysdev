package sem.group5.bob.car.streaming;

import sem.group5.bob.car.smartcar.BobCarConnectionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;


/**
 * Class responsible for sending video stream to the client over a socket connection.
 * @see java.util.Observable
 * @see java.lang.Runnable
 */
public class VideoStreamer extends Observable implements Runnable{
    private VideoProvider videoProvider;
    private Socket socket;
    private boolean streaming;

    /**
     * Constructor
     * @param s socket used for communication
     * @param videoProvider responsible for selecting which frames will be send to the client.
     */
    public VideoStreamer(Socket s, VideoProvider videoProvider) {
        this.socket = s;
        this.videoProvider = videoProvider;
        this.streaming = true;
    }

    /**
     * Sends the latest frames to client with boundaries so the client will be able to organize them. In case of
     * any error is found while streaming it will notify the class BobCarConnectionManager.
     * @see DepthJpegProvider
     * @see BobCarConnectionManager
     */
    private void stream()
    {
        try {
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
            while (streaming) {
                data = videoProvider.getLatestJpeg();
                out.write(("--BoundaryString\r\n" +
                        "Content-type: image/jpeg\r\n" +
                        "Content-Length: " + data.length + "\r\n\r\n").getBytes());
                if (Thread.interrupted()) throw new InterruptedException();
                out.write(data);
                out.write("\r\n\r\n".getBytes());
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("Streaming Stopped Unexpectedly");
//            setChanged();
//            notifyObservers("Error Streaming");
        }

    }

    /**
     * Used by the thread to run the stream..
     */
    public void run()
    {
        this.stream();
    }

    /**
     * Boolean to start streaming if true and set the stream off if false.
     * @param b b
     * @throws IOException
     */
    public void setStreaming(boolean b) throws IOException
    {
        this.streaming = b;
    }
}