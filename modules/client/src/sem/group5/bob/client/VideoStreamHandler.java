package sem.group5.bob.client;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Observable;
import java.util.Observer;

/**
 * Class responsible to return a video stream to the caller
 */
class VideoStreamHandler implements Observer{

    private MultiPartsParse provider;
    private ImageView container;
    private Thread t;

    /**
     *
      * @param container the image view in the gui
     * @param provider
     */
    VideoStreamHandler(ImageView container, MultiPartsParse provider) {

        //constructs a new container
        this.container = container;

        // Constructs a provider and adds an observer to it
        this.provider = provider;
        provider.addObserver(this);

    }
    void startStreaming()
    {
       t = new Thread(provider);
       t.start();
    }

    void stopStreaming()
    {
        if (t.isAlive()) {
            t.stop();
        }

    }

/**
 * The update() method updates an observed object.
 * This is called by the notifyObservers() from Observable
 * @param observable observable object
 * @param o the argument passed to the notifyObservers method
 */
@Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        container.setImage(fxImage);
    }
}
