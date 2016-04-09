package udpSockets;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.fileupload.MultipartStream;

import javax.imageio.ImageIO;

/**
 * Created by Raphael on 09/04/2016 for project-sysdev.
 */
public class MultiPartsParse extends java.util.Observable {

    public void readImage(InputStream depthStream, BufferedImage img) {
        byte[] boundary = "--BoundaryString".getBytes();

        @SuppressWarnings("deprecation")
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

