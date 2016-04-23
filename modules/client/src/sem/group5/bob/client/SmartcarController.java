package sem.group5.bob.client;
import java.io.IOException;

public class SmartcarController {
    public Smartcar smartcar;
    boolean isDriving;
    boolean forwardPressed;
    boolean backPressed;
    boolean leftPressed;
    boolean rightPressed;

    public SmartcarController(ConnectionManager connectionManager) throws IOException {
        smartcar = connectionManager.getSmartCar();
        this.smartcar = smartcar;
    }

    public void pressForward(int speed) throws IOException{
        if(!backPressed) {
            forwardPressed = true;
            smartcar.setSpeed(speed);
        }
    }

    public void pressBack(int speed) throws IOException {
        if (!forwardPressed) {
            backPressed = true;
            smartcar.setSpeed(-speed);
        }
    }

    public void pressLeft() throws IOException {
        if (!rightPressed) {
            leftPressed = true;
            if (forwardPressed || backPressed) {
                smartcar.setAngle(-90);
            }else {
                smartcar.rotate(-1);
            }
        }
    }

    public void pressRight() throws IOException{
        if (!leftPressed) {
            rightPressed = true;
            if (forwardPressed || backPressed) {
                smartcar.setAngle(90);
            } else {
                smartcar.rotate(1);
            }
        }
    }

    public void releaseForward() throws IOException{
        forwardPressed = false;
        smartcar.setSpeed(0);
    }

    public void releaseBack() throws IOException{
        backPressed = false;
        smartcar.setSpeed(0);
    }

    public void releaseLeft() throws IOException{
        leftPressed = false;
        smartcar.setAngle(0);
    }

    public void releaseRight() throws IOException{
        rightPressed = false;
        smartcar.setAngle(0);
    }
}
