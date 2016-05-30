package sem.group5.bob.client.streaming;

import org.apache.commons.fileupload.MultipartStream;
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
                this.pose = headers.substring(headers.indexOf("X-Robot-Pose") + 12);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                multipartStream.readBodyData(out);
                InputStream in = new ByteArrayInputStream(out.toByteArray());

                BufferedImage img;
                try
                {
                    img = ImageIO.read(in);
                    nextPart = multipartStream.readBoundary();
                    setChanged();
                    if (img != null)notifyObservers(img);
                } catch (IOException ignore) {}
            }
        }catch(Exception e){
            System.out.println("Caught Error Receiving Stream");
        }

    }
String getPose(){
    return this.pose;
}
}

