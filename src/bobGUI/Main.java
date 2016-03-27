package bobGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Raphael on 06/03/2016 for project-sysdev.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("BobGUI.fxml"));
        Scene scene = new Scene(root, 0, 0);
        primaryStage.setTitle("Bob the SmartCar");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
