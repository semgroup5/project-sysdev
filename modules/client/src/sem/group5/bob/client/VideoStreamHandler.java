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
 * Created by jpp on 18/04/16.
 */
public class VideoStreamHandler implements Observer{

    MultiPartsParse provider;
    ImageView container;
    public VideoStreamHandler(ImageView container, MultiPartsParse provider) {
        this.container = container;
        this.provider = provider;
        Thread t = new Thread(provider);
        provider.addObserver(this);
        t.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        container.setImage(fxImage);
    }
}
