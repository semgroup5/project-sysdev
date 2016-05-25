package sem.group5.bob.client.streamReceiver;

import javafx.scene.control.TextField;

import java.util.Observable;
import java.util.Observer;


/**
 * Class responsible on passing  the position reading on to the client.
 */


public class TextPoseHandler implements Observer {
    public MultiPartsParse provider;

    private TextField poseInfo;
    public String pose;


    public TextPoseHandler(TextField poseInfo, MultiPartsParse provider) {

        this.poseInfo = poseInfo;
        this.provider = provider;
        provider.addObserver(this);



    }

    @Override
    public void update(Observable o, Object arg) {
        MultiPartsParse mpp = (MultiPartsParse) o;
        poseInfo.setText(mpp.getPose());

//        TextPoseHandler textHandler = new TextPoseHandler;
//        this.addObserver(textHandler);
//        setChanged();
//        notifyObservers(this.pose);

    }
}