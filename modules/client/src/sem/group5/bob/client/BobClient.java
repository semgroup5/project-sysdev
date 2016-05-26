package sem.group5.bob.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sem.group5.bob.client.gui.ControllerGUI;

/**
 * Main class for the client interface, loads client.fxml.
 * @see javafx.application.Application
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
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1404);
        ControllerGUI controller = loader.getController();
        controller.fireConnection();
        primaryStage.setOnCloseRequest(event -> {
            controller.fireConnection();
            System.exit(0);
        });

    }

    /**
     * Main function
     * @param args main args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
