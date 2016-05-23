package sem.group5.bob.client.streamReceiver;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sem.group5.bob.client.streamReceiver.MultiPartsParse;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

/**
 * Class responsible to return a video stream to the caller
 */
public class VideoStreamHandler implements Observer
{
    private MultiPartsParse provider;
    private ImageView container;
    private Thread thread;

    /**
     *
     * @param container the image view in the gui
     * @param provider image parser
     */
    public VideoStreamHandler(ImageView container, MultiPartsParse provider)
    {
        //constructs a new container
        this.container = container;

        // Constructs a provider and adds an observer to it
        this.provider = provider;
        provider.addObserver(this);

    }
    public void startStreaming()
    {
        thread = new Thread(provider);
        thread.start();
    }

    public void stopStreaming()
    {
        provider.nextPart = false;
        thread.interrupt();


    }

    /**
     * The update() method updates an observed object.
     * This is called by the notifyObservers() from Observable
     * @param observable observable object
     * @param o the argument passed to the notifyObservers method
     */
    @Override
    public void update(Observable observable, Object o)
    {
        if (o instanceof BufferedImage)
        {
            BufferedImage image = (BufferedImage) o;
            Image fxImage = SwingFXUtils.toFXImage(image, null);
            container.setImage(fxImage);
        }
    }
}
