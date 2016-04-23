package sem.group5.bob.client;

import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Raphael on 06/03/2016 for project-sysdev for project-sysdev.
 */

public class ControllerGUI implements Observer{

    public MenuBar menuBar;
    public MenuItem close;
    public MenuItem mConnect;
    public MenuItem mSave;
    public MenuItem mLoad;
    public MenuItem about;
    public ImageView kinectView;
    public Button map;
    public Button up;
    public Button down;
    public Button left;
    public Button right;
    public Button dRight;
    public Button dLeft;
    public TextArea textFeedback;
    public Button connect;
    public Button save;
    public Button load;
    public ImageView kinectView1;
    public boolean isConnected = false;
    public boolean isMapping = false;
    public FileChooser fileChooser;
    public File file;
    public Slider speedControl;
    public ImageView loadImage;
    public ConnectionManager cm;
    public Socket socket;
    public Smartcar sm;
    public SmartcarController smartcarController;
    BufferedImage img;
    public ClientState clientState;

    public ControllerGUI() {
        clientState = new ClientState(this);
    }

    public void tryRemovingStyleClass(Styleable object, String className)
    {
        int index = object.getStyleClass().indexOf(className);
        if(index != -1){
            object.getStyleClass().remove(index);
        }
    }

    /**
     * 
     * @throws IOException
     */
    public void connect() throws IOException {

        if(!clientState.isConnected()) {
            loadImage.setVisible(true);
            tryRemovingStyleClass(connect, "red");
            connect.getStyleClass().add("green");
            connect.setText("Disconnect");
            mConnect.setText("Disconnect");
            clientState.connect();
        }
        else{
            tryRemovingStyleClass(connect, "green");
            connect.getStyleClass().add("red");
            connect.setText("Connect");
            mConnect.setText("Connect");
            cm.getDiscoverListener().close();
            sm.close();
            cm.controlSocket.close();
            textFeedback.setText("Disconnected.");
            isConnected = false;
            loadImage.setVisible(false);
        }
    }



    /**
     * Method to handle events like mapping, load and save.
     * @param event
     */
    public void handle(ActionEvent event) {
        if(event.getSource().equals(map) && isConnected) {
            if (!isMapping) {
                map.getStyleClass().add("active");
                textFeedback.clear();
                textFeedback.setText("Start mapping!");
                isMapping = true;

                try {

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                map.getStyleClass().remove("active");
                textFeedback.clear();
                textFeedback.setText("Stop mapping!");
                isMapping = false;
            }
        }
        else if (event.getSource().equals(load) || event.getSource().equals(mLoad)) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open a map");
            file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
            if (!file.equals(null)) {
                Image img = new Image(file.toURI().toString());
                kinectView.setImage(img);
            }
        }
        else if (event.getSource().equals(save) || event.getSource().equals(mSave)) {
            fileChooser = new FileChooser();

            //Set extension filter
//           FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//           fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            file = fileChooser.showSaveDialog(menuBar.getScene().getWindow());

            if(file != null){
                SaveFile("Map", file);
            }
        }
    }

    /**
     * Method to shutdown the GUI
     */
    public void closeApplication() {
        menuBar.getScene().getWindow().hide();
    }

    /**
     * Method to apply shadow effect to buttons
     * @param event
     */
    public void shadow(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(new DropShadow());
        if(event.getSource().equals(save))save.setEffect(new DropShadow());
        if(event.getSource().equals(load))load.setEffect(new DropShadow());

    }

    /**
     * Method to take off shadow effect of the buttons
     * @param event
     */
    public void shadowOff(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(null);
        if(event.getSource().equals(save))save.setEffect(null);
        if(event.getSource().equals(load))load.setEffect(null);

    }

    /**
     * Method to handle the keylisteners when a key is pressed.
     * @param event
     * @throws IOException
     */
    public void keyListenersPressed(KeyEvent event) throws IOException {
        if (!isConnected) {
            textFeedback.clear();
            textFeedback.setText("SmartCar is disconnected...");
        } else {
            int currentSpeed = (int)speedControl.getValue();
            switch (event.getCode()) {
                case W:
                    up.getStyleClass().add("pressed");
                    smartcarController.pressForward(currentSpeed);
                    break;
                case S:
                    down.getStyleClass().add("pressed");
                    smartcarController.pressBack(currentSpeed);
                    break;
                case A:
                    left.getStyleClass().add("pressed");
                    smartcarController.pressLeft();
                    break;
                case D:
                    right.getStyleClass().add("pressed");
                    smartcarController.pressRight();
                    break;
                case R:
                    speedControl.increment();
                    textFeedback.setText("driving at " + speedControl.getValue() + "%");
                    break;
                case F:
                    speedControl.decrement();
                    textFeedback.setText("driving at " + speedControl.getValue() + "%");
                    break;
                case Z:
                    map.fire();
                    break;
                case X:
                    save.fire();
                    break;
                case C:
                    load.fire();
                    break;
            }
        }
    }

    /**
     * Method to handle the keylisteners when a key is released.
     * @param event
     * @throws IOException
     */
    public void keyListenersReleased(KeyEvent event) throws IOException {
        event.consume();
        if (!isConnected) {
            textFeedback.clear();
            textFeedback.setText("SmartCar is disconnected...");
        } else {
            switch (event.getCode()) {
                case W:
                    up.getStyleClass().remove("active");
                    smartcarController.releaseForward();
                    break;
                case S:
                    down.getStyleClass().remove("active");
                    smartcarController.releaseBack();
                    break;
                case A:
                    left.getStyleClass().remove("active");
                    smartcarController.releaseLeft();
                    break;
                case D:
                    right.getStyleClass().remove("active");
                    smartcarController.releaseRight();
                    break;
            }
        }
    }

    /**
     * Method to set InnerShadow effect to buttons
     * @param event
     */
    public void setFocused(Event event) {
        if(event.getSource().equals(connect))connect.setEffect(new InnerShadow());
        if(event.getSource().equals(save))save.setEffect(new InnerShadow());
        if(event.getSource().equals(load))load.setEffect(new InnerShadow());
        if(event.getSource().equals(up))up.setEffect(new InnerShadow());
        if(event.getSource().equals(down))down.setEffect(new InnerShadow());
        if(event.getSource().equals(left))left.setEffect(new InnerShadow());
        if(event.getSource().equals(right))right.setEffect(new InnerShadow());

    }



    /**
     * Method to handle mouseReleased events on the smartcar control
     * @param event
     */
    public void mouseReleased(MouseEvent event) throws IOException {
        if (!isConnected) {
            textFeedback.clear();
            textFeedback.setText("SmartCar is disconnected...");
        }
        else {
            if (event.getSource() == up) {
                smartcarController.releaseForward();
            } else if (event.getSource() == down) {
                smartcarController.releaseBack();
            } else if (event.getSource() == left) {
                smartcarController.releaseLeft();
            } else if (event.getSource() == right) {
                smartcarController.releaseRight();
            } else if (event.getSource() == dRight) {}
              else if (event.getSource() == dLeft) {}
        }
    }

    /**
     * Method to handle mousePressed events on the smartcar control.
     * @param event
     */
    public void mousePressed(MouseEvent event) throws IOException {
        if (!isConnected) {
            textFeedback.clear();
            textFeedback.setText("SmartCar is disconnected...");
        }
        else {
            int currentSpeed = (int)speedControl.getValue();
            if (event.getSource() == up) {
                smartcarController.pressForward(currentSpeed);
            } else if (event.getSource() == down) {
                smartcarController.pressBack(currentSpeed);
            } else if (event.getSource() == left) {
                smartcarController.pressLeft();
            } else if (event.getSource() == right) {
                smartcarController.pressRight();
            }
        }
    }

    /**
     * Method to save maps in the computer's directory.
     * @param content
     * @param file
     */
    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void fireConnection() {
        connect.fire();
    }

    @Override
    public void update(Observable o, Object arg) {
        fireConnection();
    }

    public void replaceStatus(String s)
    {
        textFeedback.setText(s);
    }

    public void appendStatus(String s)
    {
        textFeedback.appendText(s);
    }
}
