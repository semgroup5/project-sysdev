package sem.group5.bob.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the client interface, loads client.fxml.
*/

public class BobClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/client.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 0, 0);
        primaryStage.setTitle("Bob the SmartCar");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        ControllerGUI controller = loader.getController();
        controller.fireConnection();
        primaryStage.setOnCloseRequest(event -> {
                       controller.fireConnection();
                       System.exit(0);
                 });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
