package sem.group5.bob.client.streamReceiver;

import org.apache.commons.fileupload.MultipartStream;
import sem.group5.bob.client.mappGenerator.LogToFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;

/**
 * Class that will take the video stream from the kinect as a multipart MJPEG stream.
 * @see java.util.Observable
 * @see java.lang.Runnable
 */
public class MultiPartsParse extends Observable implements Runnable
{
    private InputStream depthStream;
    private LogToFile CarmenLog;
    boolean nextPart;
    private String pose;

    /**
     * Constructor
     * @param depthStream the video stream captured by the kinect.
     */
    public MultiPartsParse(InputStream depthStream)
    {
        this.depthStream = depthStream;
    }

    /**
     * todo
     * @param CarmenLog log
     */
    public void setLog(LogToFile CarmenLog)
    {
        this.CarmenLog = CarmenLog;
    }

    /**
     * Class that receive the JPEGs sent by the car as a multipart MJPEG stream
     * and notify any observers once an image has been received.
     */
    public void run() {
        byte[] boundary = "BoundaryString".getBytes();
        @SuppressWarnings("deprecation") MultipartStream multipartStream = new MultipartStream(depthStream, boundary);

        try
        {
            multipartStream.setBoundary(boundary);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        try{
            nextPart = multipartStream.skipPreamble();
            while(nextPart)
            {
                String headers = multipartStream.readHeaders();
                this.pose = headers.substring(headers.lastIndexOf("X-Robot-Pose: ")+1);

                if(!(CarmenLog == null)){
                    double x = Double.parseDouble(pose.substring(pose.indexOf('X', pose.indexOf('Y'))));
                    pose = pose.substring(pose.indexOf('Y' +1));
                    double y = Double.parseDouble(pose.substring(0, pose.indexOf('A')));
                    pose = pose.substring(pose.indexOf('g'+1));
                    double theta = Double.parseDouble(pose);
                    CarmenLog.logOdometryFormatter(x, y, theta);
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                multipartStream.readBodyData(out);
                InputStream in = new ByteArrayInputStream(out.toByteArray());

                BufferedImage img;
                try
                {
                    img = ImageIO.read(in);
                    nextPart = multipartStream.readBoundary();
                    setChanged();
                    notifyObservers(img);
                } catch (IOException ignore) {}
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Caught Error Receiving Stream");
//            setChanged();
//            notifyObservers("Error Receiving Stream");
        }

    }
String getPose(){
    return this.pose;
}
}

