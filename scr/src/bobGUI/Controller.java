package bobGUI;

import com.sun.javafx.css.StyleCacheEntry;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.AmbientLight;
import javafx.scene.control.*;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * Created by Raphael on 06/03/2016.
 */
public class Controller {


    public MenuBar menuBar;
    public MenuItem close;
    public MenuItem about;
    public MediaView kinectView;
    public Button map;
    public Button up;
    public Button down;
    public Button left;
    public Button right;
    public TextArea textFeedback;
    public Button connect;
    public Button save;
    public Button load;

    public void sayHi(ActionEvent actionEvent){
        
    }

    public void closeApplication(ActionEvent actionEvent) {
        menuBar.getScene().getWindow().hide();
    }


}
