package bobGUI;

import com.sun.javafx.css.StyleCacheEntry;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import udpSockets.Client;

/**
 * Created by Raphael on 06/03/2016.
 */
public class Controller {


    public MenuBar menuBar;
    public MenuItem close;
    public MenuItem about;
    public ImageView kinectView;
    public Button map;
    public Button up;
    public Button down;
    public Button left;
    public Button right;
    public TextArea textFeedback;
    public Button connect;
    public Button save;
    public Button load;
    public boolean isConnected = false;



    public void sayHi(ActionEvent actionEvent){
       textFeedback.setText("Hi I'm mapping!");
    }

    public void closeApplication(ActionEvent actionEvent) {
        menuBar.getScene().getWindow().hide();
    }


    public void shadow(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(new DropShadow());
        if(event.getSource().equals(save))save.setEffect(new DropShadow());
        if(event.getSource().equals(load))load.setEffect(new DropShadow());

    }

    public void shadowOff(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(null);
        if(event.getSource().equals(save))save.setEffect(null);
        if(event.getSource().equals(load))load.setEffect(null);

    }

    public void change(Event event) {


    }

    public void setFocused(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(new InnerShadow());
        if(event.getSource().equals(save))save.setEffect(new InnerShadow());
        if(event.getSource().equals(load))load.setEffect(new InnerShadow());
        if(event.getSource().equals(up))up.setEffect(new InnerShadow());
        if(event.getSource().equals(down))down.setEffect(new InnerShadow());
        if(event.getSource().equals(left))left.setEffect(new InnerShadow());
        if(event.getSource().equals(right))right.setEffect(new InnerShadow());

    }

    public void connect(ActionEvent actionEvent) {
        if(!isConnected) {
            connect.setStyle("-fx-background-color: linear-gradient(#ff6767, #ff1a1a),        " +
                    "radial-gradient(center 50% -40%, radius 200%, #ff4d4d 45%, #ff0000 50%); " +
                    "-fx-background-insets: 0, 1; " +
                    "-fx-text-fill: #f5f5f5; " +
                    "-fx-background-radius: 25 0 0 0;");
            connect.setText("Disconnect");
            isConnected = true;

        }
        else{
            connect.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00),        " +
                    "radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%); " +
                    "-fx-background-insets: 0, 1; " +
                    "-fx-text-fill: #395306; " +
                    "-fx-background-radius: 25 0 0 0;");
            connect.setText("Connect");
            isConnected = false;
        }
    }
}
