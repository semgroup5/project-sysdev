package sem.group5.bob.car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

    /**
    * SmartCarComm handle the received input from the client and forward it to the arduino attached in the smartcar.
    */
    public class SmartCarComm {
        private BufferedReader input;
        private OutputStream output;

        /**
         * Constructor
         * @param input Buffer reader used for the serialConnect class
         * @param output OutputStream used for the serialConnect class
         * @throws NoSuchFieldError
         */
    public SmartCarComm(BufferedReader input, OutputStream output) throws NoSuchFieldError{
        this.input = input;
        this.output = output;
    }

        /**
         * WriteData method is responsible for sending the data to the arduino in the smartcar
         * @param data data to be send
         */
    public synchronized void writeData(String data) {
        try {
            System.out.println("Sending : " + data);
            output.write(data.getBytes());
            output.flush();
            System.out.println("Data sent to arduino");
        } catch (IOException e) {
            System.out.println("could not write to port");
            e.printStackTrace();
        }
    }

        /**
         * Method to send speed values to the smartcar
         * @param speed
         */
    public void setSpeed(int speed){
        if((speed <= 100) && (speed >= -100)){
            writeData("w" + speed + "/");
        }
    }

        /**
         * Method to send angle values to the smartcar
         * @param angle
         */
    public void setAngle(int angle){
        if(angle < 360 && angle > -360){
            writeData("a" + angle + "/");
        }
    }

        /**
         * Method to send command to the smartcar initiate rotate in the spot.
         * If sent 1 it rotates clockwise else if sent -1 it rotates anticlockwise
         * @param direction
         */
    public void setRotate(int direction){
        if(direction >= -1 || direction <= 1){
            writeData("r" + direction + "/");
        }
    }

}