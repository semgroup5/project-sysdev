package sem.group5.bob.client;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;
import org.apache.commons.fileupload.MultipartStream;

import javax.imageio.ImageIO;

/**
 * Created by Raphael on 09/04/2016 for project-sysdev.
 */
public class MultiPartsParse extends Observable {

    public void readImage(InputStream depthStream, BufferedImage img) {
        byte[] boundary = "--BoundaryString".getBytes();

        MultipartStream multipartStream = new MultipartStream(depthStream, boundary);

        boolean nextPart;
        try {
            nextPart = multipartStream.skipPreamble();
            while (true) {
                while (nextPart) {
                    String header = multipartStream.readHeaders();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    multipartStream.readBodyData(out);
                    InputStream in = new ByteArrayInputStream(out.toByteArray());
                    img = ImageIO.read(in);

                    nextPart = multipartStream.readBoundary();
                    setChanged();
                    notifyObservers(img);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

