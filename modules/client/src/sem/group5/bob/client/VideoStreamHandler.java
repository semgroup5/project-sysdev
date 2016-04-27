package sem.group5.bob.client;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Observable;
import java.util.Observer;

class VideoStreamHandler implements Observer{

    private MultiPartsParse provider;
    private ImageView container;
    private Thread t;
    VideoStreamHandler(ImageView container, MultiPartsParse provider) {
        this.container = container;
        this.provider = provider;
        provider.addObserver(this);

    }
    void startStreaming()
    {
       t = new Thread(provider);
       t.start();
    }


    @Override
    public void update(Observable observable, Object o) {
        BufferedImage image = (BufferedImage) o;
        Image fxImage = SwingFXUtils.toFXImage(image, null);
        container.setImage(fxImage);
    }
}
