package sem.group5.bob.client;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A controller class for the GUI FXML file, named client.fxml.
 * @see java.util.Observable
 */


public class ControllerGUI extends Observable {

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
    public TextArea textFeedback;
    public Button connect;
    public Button save;
    public Button load;
    public ImageView scanLineView;
    private boolean isMapping = false;
    private boolean connectClicked = false;
    public Slider speedControl;
    public ImageView loadImage;
    private ClientState clientState;
    private ButtonsStyle style;
    private Text infoText;

    /**
     * Constructor constructs a object clientState and adds an observer to it.
     * @see ClientState
     */
    public ControllerGUI() {
        clientState = new ClientState(this);
        addObserver(clientState);
        style = new ButtonsStyle(this);
    }

    /**
     * Connect button handler
     */
    public void connect() {
        if(!clientState.isConnected() && !connectClicked)
        {
            connectClicked = true;
            setState("Connected");
            setChanged();
            notifyObservers("Connect");
        }
        else if (clientState.isConnected && !connectClicked)
        {
            connectClicked = true;
            setState("Disconnected");
            setChanged();
            notifyObservers("Disconnect");
        }
    }

    /**
     * Method update the GUI depending on the string value.
     * @param state A string that represents the state of the Text annotation.

     */
    void setState(String state) {
        loadImage.setVisible(true);
        if(state.equals("Connected"))
        {
            style.styleButton(connect, "active");
            connect.setText("Disconnect");
            mConnect.setText("Disconnect");
        }
        else if (state.equals("Disconnected"))
        {
            style.styleButton(connect, "");
            connect.setText("Connect");
            mConnect.setText("Connect");

        }
    }

    /**
     * Method to start streaming on the client side
     */
    void stream()
    {
        try {
            clientState.startStream();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to handle events like mapping, load and save.
     * @param event clicked button event
     */
    public void handle(ActionEvent event) {
        FileChooser fileChooser;
        File file;
        if(event.getSource().equals(map) && clientState.isConnected) {
            if (!isMapping) {
                style.styleButton(map, "active");
                isMapping = true;

            }
            else {
                style.styleButton(map, "");
                replaceStatus("Mapping stopped!");
                kinectView = new ImageView();
                isMapping = false;
            }
        }
        else if (event.getSource().equals(load) || event.getSource().equals(mLoad)) {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Open a map");
            file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
            if (!(file == null)) {
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
     * @see ButtonsStyle
     */
    public void shadow(Event event) {
        style.shadow(event);

    }

    /**
     * Method to take off shadow effect of the buttons
     * @param event see @ButtonStyle
     */
    public void shadowOff(Event event) {
        style.shadowOff(event);
    }

    /**
     * Method to handle the keylisteners when a key is pressed.
     * @param event key pressed event
     * @throws IOException
     */
    public void keyListenersPressed(KeyEvent event) throws IOException {
        if (!clientState.isConnected && event.getCode() != KeyCode.V) {
            replaceStatus("SmartCar is disconnected...");
        }else if (event.getCode() == KeyCode.V) {
            fireConnection();
        }
        else {
            int currentSpeed = (int)speedControl.getValue();
            switch (event.getCode()) {
                case W:
                    style.styleButton(up, "active");
                    clientState.getSmartcarController().pressForward(currentSpeed);
                    break;
                case S:
                    style.styleButton(down, "active");
                    clientState.getSmartcarController().pressBack(currentSpeed);
                    break;
                case A:
                    style.styleButton(left, "active");
                    clientState.getSmartcarController().pressLeft();
                    break;
                case D:
                    style.styleButton(right, "active");
                    clientState.getSmartcarController().pressRight();
                    break;
                case R:
                    speedControl.increment();
                    replaceStatus("driving at " + speedControl.getValue() + "%");
                    break;
                case F:
                    speedControl.decrement();
                    replaceStatus("driving at " + speedControl.getValue() + "%");
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
     * @param event key released event
     * @throws IOException
     */
    public void keyListenersReleased(KeyEvent event) throws IOException {
        event.consume();
        if (!clientState.isConnected) {
            replaceStatus("SmartCar is disconnected...");
        } else {
            switch (event.getCode()) {
                case W:
                    style.styleButton(up, "");
                    clientState.getSmartcarController().releaseForward();
                    break;
                case S:
                    style.styleButton(down, "");
                    clientState.getSmartcarController().releaseBack();
                    break;
                case A:
                    style.styleButton(left, "");
                    clientState.getSmartcarController().releaseLeft();
                    break;
                case D:
                    style.styleButton(right, "");
                    clientState.getSmartcarController().releaseRight();
                    break;
            }
        }
    }

    /**
     * Method to set InnerShadow effect to buttons
     * @param event ButtonStyle
     * @see ButtonsStyle
     */
    public void setFocused(Event event) {
        style.setFocused(event);
    }



    /**
     * Method to handle mouseReleased events on the smartcar control
     * @param event mouse released event
     */
    public void mouseReleased(MouseEvent event) throws IOException {
        if (!clientState.isConnected) {
            replaceStatus("SmartCar is disconnected...");
        }
        else {
            if (event.getSource() == up) {
                clientState.getSmartcarController().releaseForward();
            } else if (event.getSource() == down) {
                clientState.getSmartcarController().releaseBack();
            } else if (event.getSource() == left) {
                clientState.getSmartcarController().releaseLeft();
            } else if (event.getSource() == right) {
                clientState.getSmartcarController().releaseRight();
            }
        }
    }

    /**
     * Method to handle mousePressed events on the smartcar control.
     * @param event mouse pressed event
     */
    public void mousePressed(MouseEvent event) throws IOException {
        if (!clientState.isConnected) {
            replaceStatus("SmartCar is disconnected...");
        }
        else {
            int currentSpeed = (int)speedControl.getValue();
            if (event.getSource() == up) {
                clientState.getSmartcarController().pressForward(currentSpeed);
            } else if (event.getSource() == down) {
                clientState.getSmartcarController().pressBack(currentSpeed);
            } else if (event.getSource() == left) {
                clientState.getSmartcarController().pressLeft();
            } else if (event.getSource() == right) {
                clientState.getSmartcarController().pressRight();
            }
        }
    }

    /**
     * Method to save maps in the computer's directory.
     * @param content the data to be saved
     * @param file the file in which the content will be saved
     */
    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *TODO
     */
    void fireConnection() {
        connect.fire();
    }

    /**
     * Method that will clear the state of the gui.
     * @param s String to be written in the text field
     * @see ControllerGUI#setState(String)
     */
    void replaceStatus(String s)
    {
        textFeedback.clear();
        textFeedback.setText(s);
    }

    /**
     *TODO
     * @param b
     */
    void setConnectClicked(boolean b)

    {
        connectClicked = b;
    }

}
