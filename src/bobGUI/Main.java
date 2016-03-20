package bobGUI;
/**
 * Created by Raphael on 06/03/2016.
 */

import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("BobGUI.fxml"));
        primaryStage.setTitle("Bob the SmartCar");
        primaryStage.setScene(new Scene(root, 1000, 650));
        primaryStage.show();
       

       primaryStage.setMaximized(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
