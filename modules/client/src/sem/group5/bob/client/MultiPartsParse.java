package sem.group5.bob.client;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;
import org.apache.commons.fileupload.MultipartStream;

import javax.imageio.ImageIO;

/**
 * Created by Raphael on 09/04/2016 for project-sysdev.
 */
public class MultiPartsParse extends Observable implements Runnable{

    InputStream depthStream;
    BufferedImage img;

    public MultiPartsParse(InputStream depthStream) {
        this.depthStream = depthStream;
    }

    public void run() {
        byte[] boundary = "BoundaryString".getBytes();
        MultipartStream multipartStream = new MultipartStream(depthStream, boundary);

        try
        {
            multipartStream.setBoundary(boundary);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        boolean nextPart =true;
        try{
            nextPart=multipartStream.skipPreamble();
            while(nextPart)
            {
                String header = multipartStream.readHeaders();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                multipartStream.readBodyData(out);
                InputStream in = new ByteArrayInputStream(out.toByteArray());

                img = ImageIO.read(in);

                nextPart = multipartStream.readBoundary();
                setChanged();
                notifyObservers(img);

            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("caught error receiving depth");
        }


    }

}

