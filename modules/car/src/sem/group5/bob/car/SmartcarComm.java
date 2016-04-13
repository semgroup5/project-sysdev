package sem.group5.bob.car;

import sem.group5.bob.car.streaming.SerialPortConnect;

import java.io.BufferedReader;
import java.io.OutputStream;

public class SmartCarComm {
    public BufferedReader input;
    public OutputStream output;
    SerialConnect obj;
    SerialPortConnect con;

    public SmartCarComm() throws NoSuchFieldError{
        obj = new SerialConnect();
        obj.initialize();
        input = SerialConnect.input;
        output = SerialConnect.output;
//        con = new SerialPortConnect();
//        con.initialize();
    }

//    public synchronized void writeData(String data) {
//        try {
//            System.out.println("Sending : " + data);
//            con.write(data);
//        } catch (Exception e) {
//            System.out.println("could not write to port");
//        }
//    }

    public synchronized void writeData(String data) {
        try {
            System.out.println("Sending : " + data);
            output.write(data.getBytes());
            output.flush();
        } catch (Exception e) {
            System.out.println("could not write to port");
        }
    }

    public void setSpeed(int speed){
        if((speed <= 100) && (speed >= -100)){
            writeData("w" + speed + "/");
        }
    }

    public void setAngle(int angle){
        if(angle < 360 && angle > -360){
            writeData("a" + angle + "/");
        }
    }

    public void setRotate(int angle){
        if(angle < 360 && angle > -360){
            writeData("r" + angle + "/");
        }
    }

    /**
     * here
     */
    public void closeConnection() {
        obj.close();
    }
}
