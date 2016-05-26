package sem.group5.bob.car.streaming;

import sem.group5.bob.car.smartcar.BobCarConnectionManager;
import sem.group5.bob.car.smartcar.PoseManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
//TODO
/**
 * Class responsible for sending depth stream to the client over a socket connection.
 *@see java.lang.Runnable
 * @see java.util.Observable
 */
public class DepthStreamer extends Observable implements Runnable
{
    private DepthJpegProvider cjp;
    private PoseManager poseManagerManagerProvider;
    private Socket socket;
    private boolean streaming;

    /**
     * Constructor
     * @param s socket used for communication
     * @param cjp responsible for selecting which frames will be send to the client.
     */
    public DepthStreamer(Socket s, DepthJpegProvider cjp, PoseManager PoseManagerManagerProvider)
    {
        this.socket = s;
        this.cjp = cjp;
        this.poseManagerManagerProvider = PoseManagerManagerProvider;
        this.streaming = true;
    }

    /**
     * Function that sends hte latest frames to client with boundaries so the client will be able to organize them. In case of
     * any error is found while streaming it will notify the class BobCarConnectionManager.
     * @see DepthJpegProvider
     * @see BobCarConnectionManager
     */
    private void stream()
    {
        try
        {
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
            while (streaming)
            {
                if (Thread.interrupted()) throw new InterruptedException();
                data = cjp.getLatestJpeg();
                //PoseManager poseProvider =  new PoseManager();     getlatestpose   needs to be implemented on the pose class
                poseManagerManagerProvider.getLatestPose();
                String pose = poseManagerManagerProvider.getLatestPose();
                out.write(("--BoundaryString\r\n" +
                        "Content-type: image/jpeg\r\n" +
                        "Content-Length: " + data.length + "\r\n" +
                        "X-Robot-Pose: " + pose + "\r\n\r\n").getBytes());
                out.write(data);

                System.out.print(pose);
                if (Thread.interrupted()) throw new InterruptedException();
                out.write("\r\n\r\n".getBytes());
                out.flush();
                if (Thread.interrupted()) throw new InterruptedException();
            }
        } catch (Exception e)
        {
            System.out.println("Streaming Stopped Unexpectedly");
//            setChanged();
//            notifyObservers("Error Streaming");
        }

    }

    /**
     * Used by the thread to run the stream.
     */
    public void run()
    {
        this.stream();
    }

    /**
     * True if depth is streaming and false when not.
     * @param b b
     * @throws IOException
     */
    public void setStreaming(boolean b) throws IOException
    {
        this.streaming = b;
    }
}
