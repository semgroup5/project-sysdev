package sem.group5.bob.client.streamReceiver;

import javafx.scene.control.TextField;
import java.util.Observable;
import java.util.Observer;

/**
 * Class responsible on passing  the position reading on to the client.
 */
public class TextPoseHandler implements Observer {
    private TextField poseInfo;

    /**
     * todo
     * @param poseInfo p
     * @param provider p
     */
    public TextPoseHandler(TextField poseInfo, MultiPartsParse provider) {
        this.poseInfo = poseInfo;
        provider.addObserver(this);
    }

    /**
     * todo
     * @param o o
     * @param arg arg
     */
    @Override
    public void update(Observable o, Object arg) {
        MultiPartsParse mpp = (MultiPartsParse) o;
        poseInfo.setText(mpp.getPose());
    }
}