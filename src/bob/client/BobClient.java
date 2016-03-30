package bob.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by jpp on 30/03/16.
 */
public class BobClient extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/client.fxml"));
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
