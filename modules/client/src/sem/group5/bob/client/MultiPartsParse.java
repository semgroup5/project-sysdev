package sem.group5.bob.client;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Observable;
import org.apache.commons.fileupload.MultipartStream;

import javax.imageio.ImageIO;

class MultiPartsParse extends Observable implements Runnable{

    private InputStream depthStream;

    MultiPartsParse(InputStream depthStream) {
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

        boolean nextPart;
        try{
            nextPart=multipartStream.skipPreamble();
            while(nextPart)
            {

                multipartStream.readHeaders();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                multipartStream.readBodyData(out);
                InputStream in = new ByteArrayInputStream(out.toByteArray());

                BufferedImage img = ImageIO.read(in);


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

