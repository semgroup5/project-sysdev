package sem.group5.bob.car.smartcar;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Observable;

/**
 * SmartCarComm handles the received input from the client and forward it to the arduino attached to the smartcar.
 * @see java.util.Observable
 */
public class SmartCarComm extends Observable
{
    private OutputStream output;

    /**
     * Constructor
     * @param output OutputStream used for the serialConnect class
     * @throws NoSuchFieldError
     */
    public SmartCarComm(OutputStream output) throws NoSuchFieldError
    {
        this.output = output;
    }

    /**
     * WriteData method is responsible for sending the data to the arduino in the smartcar
     * @param data data to be sent
     */
    private synchronized void writeData(String data)
    {
        try
        {
            output.write(data.getBytes());
        } catch (IOException e) {
            System.out.println("could not write to port");
            setChanged();
            notifyObservers("Serial Port Failed");
        }
    }

    /**
     * Method to send speed values to the smartcar
     * @param speed integer speed between 1 to 100.
     * @see SmartCarComm#writeData(String)
     */
    void setSpeed(int speed)
    {
        if((speed <= 100) && (speed >= -100))
        {
            writeData("w" + speed + "/");
        }
    }

    /**
     * Method to send angle values to the smartcar
     * @param angle angle to turn the car.
     * @see SmartCarComm#writeData(String)
     */
    void setAngle(int angle)
    {
        if(angle <= 360 && angle >= -360)
        {
            writeData("a" + angle + "/");
        }
    }

    /**
     * Method to send command to the smartcar initiate rotate in the spot.
     * If sent 1 it rotates clockwise else if sent -1 it rotates anticlockwise
     * @param direction direction to rotate the car
     * @see SmartCarComm#writeData(String)
     */
    void setRotate(int direction)
    {
        if(direction >= -1 || direction <= 1)
        {
            writeData("r" + direction + "/");
        }
    }

    /**
     * todo
     */
    public void resetArduino()
    {
        writeData("x/");
    }

}
