package sem.group5.bob.client.streamReceiver;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;
import org.apache.commons.fileupload.MultipartStream;
import sem.group5.bob.client.LogToFile;

import javax.imageio.ImageIO;

/**
 * Class that will take the video stream from the kinect as a multipart MJPEG stream.
 * @see java.util.Observable
 * @see java.lang.Runnable
 */
public class MultiPartsParse extends Observable implements Runnable{
    private InputStream depthStream;
    private LogToFile CarmenLog;
    boolean nextPart;

    /**
     * Constructor
     * @param depthStream the video stream captured by the kinect.
     */
    public MultiPartsParse(InputStream depthStream) {
        this.depthStream = depthStream;
    }

    public void setLog(LogToFile CarmenLog){

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
                String pose = headers.substring(headers.lastIndexOf("X-Robot-Pose: ")+1);

                if(!(CarmenLog == null)){
                    CarmenLog.addToList(pose);
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                multipartStream.readBodyData(out);
                InputStream in = new ByteArrayInputStream(out.toByteArray());

                BufferedImage img = ImageIO.read(in);

                nextPart = multipartStream.readBoundary();
                setChanged();
                notifyObservers(img);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Caught Error Receiving Stream");
            setChanged();
            notifyObservers("Error Receiving Stream");
        }

    }

}

