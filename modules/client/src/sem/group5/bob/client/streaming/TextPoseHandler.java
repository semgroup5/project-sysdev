package sem.group5.bob.client.streaming;

import javafx.scene.control.TextField;
import java.util.Observable;
import java.util.Observer;

/**
 * Class responsible on passing  the position reading on to the client.
 * @see java.util.Observer
 */
public class TextPoseHandler implements Observer {
    private TextField poseInfo;

    /**
     *
     * @param poseInfo String with Position information
     * @param provider p
     */
    public TextPoseHandler(TextField poseInfo, MultiPartsParse provider) {
        this.poseInfo = poseInfo;
        provider.addObserver(this);
    }

    /**
     * Updtate the TextField in the GUI with the latest position info
     * @param o o
     * @param arg arg
     */
    @Override
    public void update(Observable o, Object arg) {
        MultiPartsParse mpp = (MultiPartsParse) o;
        poseInfo.setText(mpp.getPose());
    }
}