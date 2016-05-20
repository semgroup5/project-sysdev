package sem.group5.bob.client.componentStyle;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import sem.group5.bob.client.ControllerGUI;

/**
 * Adds graphical styling to the GUI.
 */
public class ButtonsStyle
{
    private ControllerGUI gui;

    /**
     * Constructor
     * @param gui User GUI
     */
    public ButtonsStyle(ControllerGUI gui) {
        this.gui = gui;
    }

    /**
     * Method to adds graphical changes to the GUI.
     * @param button The button on GUI
     * @param action Action event from this button
     */
    public void styleButton(Button button, String action) {

        if (button.equals(gui.map)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#ffd65b, #e68400),        " +
                        "linear-gradient(#ffef84, #f2ba44),        " +
                        "linear-gradient(#ffea6a, #efaa22),        " +
                        "linear-gradient(#ffe657 0%, #8cff1a 50%, #72e600 100%),        " +
                        "linear-gradient(from 0% 0% to 15% 50%, #a5ff4d, #59b300); " +
                        "-fx-background-radius: 30; " +
                        "-fx-background-insets: 0,1,2,3,0; " +
                        "-fx-text-fill: #654b00; " +
                        "-fx-font-weight: bold;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#ffd65b, #e68400),        " +
                        "linear-gradient(#ffef84, #f2ba44),        " +
                        "linear-gradient(#ffea6a, #efaa22),        " +
                        "linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),        " +
                        "linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0)); " +
                        "-fx-background-radius: 30; " +
                        "-fx-background-insets: 0,1,2,3,0; " +
                        "-fx-text-fill: #654b00; " +
                        "-fx-font-weight: bold;");
            }
        } else if (button.equals(gui.up)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%), " +
                        "linear-gradient(#020b02, #3a3a3a), " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%), " +
                        "linear-gradient(#f5f5f5 0%, #a9c4f5 50%, #6495ed 51%, #3676e8 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 25 25 0 0;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 25 25 0 0;");
            }
        } else if (button.equals(gui.down)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #a9c4f5 50%, #6495ed 51%, #3676e8 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 0 0 25 25;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 0 0 25 25;");
            }
        } else if (button.equals(gui.left)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #a9c4f5 50%, #6495ed 51%, #3676e8 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 25 0 0 25;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 25 0 0 25;");
            }
        } else if (button.equals(gui.right)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #a9c4f5 50%, #6495ed 51%, #3676e8 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 0 25 25 0;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),        " +
                        "linear-gradient(#020b02, #3a3a3a),        " +
                        "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),        " +
                        "linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%); " +
                        "-fx-background-insets: 0,1,4,5; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: Helvetica; " +
                        "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1); " +
                        "-fx-background-radius: 0 25 25 0;");
            }
        } else if (button.equals(gui.connect)) {
            if (action.equals("active")) {
                button.setStyle("-fx-background-color: linear-gradient(#ff6767, #ff1a1a),        " +
                        "radial-gradient(center 50% -40%, radius 200%, #ff4d4d 45%, #ff0000 50%); " +
                        "-fx-background-insets: 0, 1; " +
                        "-fx-text-fill: #f5f5f5; " +
                        "-fx-background-radius: 25 0 0 0;");
            } else {
                button.setStyle("-fx-background-color: linear-gradient(#f0ff35, #a9ff00),        " +
                        "radial-gradient(center 50% -40%, radius 200%, #b8ee36 45%, #80c800 50%); " +
                        "-fx-background-insets: 0, 1; " +
                        "-fx-text-fill: #395306; " +
                        "-fx-background-radius: 25 0 0 0;");
            }
        }
    }

    /**
     * Method to set InnerShadow effect to buttons
     * @param event which button has been clicked
     */
    public void setFocused(Event event) {
        if (event.getSource().equals(gui.connect)) gui.connect.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.save)) gui.save.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.load)) gui.load.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.up)) gui.up.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.down)) gui.down.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.left)) gui.left.setEffect(new InnerShadow());
        if (event.getSource().equals(gui.right)) gui.right.setEffect(new InnerShadow());

    }

    /**
     * Method to apply shadow effect to buttons
     * @param event which button the mouse have hovered over
     */
    public void shadow(Event event) {
        if (event.getSource().equals(gui.connect)) gui.connect.setEffect(new DropShadow());
        if (event.getSource().equals(gui.save)) gui.save.setEffect(new DropShadow());
        if (event.getSource().equals(gui.load)) gui.load.setEffect(new DropShadow());

    }

    /**
     * Method to take off shadow effect of the buttons
     * @param event which button the mouse have hovered off
     */
    public void shadowOff(Event event) {
        if (event.getSource().equals(gui.connect)) gui.connect.setEffect(null);
        if (event.getSource().equals(gui.save)) gui.save.setEffect(null);
        if (event.getSource().equals(gui.load)) gui.load.setEffect(null);
    }
}
