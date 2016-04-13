package sem.group5.bob.car;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;


/**
 * Created by Raphael on 13/04/2016 for project-sysdev.
 */
public class SerialPortConnect implements SerialPortEventListener{
    SerialPort port;
    public static BufferedReader input;
    public static OutputStream output;

    /**
     * Milliseconds to block while waiting for port open
     */
    public static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    public static final int DATA_RATE = 9600;

    public void initialize() {
        try {
            port = new SerialPort("/dev/ttyACM0");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not find COM port.");
            System.exit(1);
            return;
        }

        try {
            System.out.println("Opening port.");
            // open serial port, and use class name for the appName.
            port.openPort();

            System.out.println("Setting parameter");
            // set port parameters
            port.setParams(DATA_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            System.out.println("Setting Flow Control");
            port.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            System.out.println("Writing to port.");
            port.writeString("Test");

            port.setRTS(false);
            port.setDTR(false);

            int mask = SerialPort.MASK_RXCHAR;
            //Set the prepared mask
            port.setEventsMask(mask);

            System.out.println("Adding event listeners");
            port.addEventListener(this);
        } catch (SerialPortException e) {
            System.out.println("There are an error on writing string to port т: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
            try {
                String receivedData = port.readString(serialPortEvent.getEventValue());
                System.out.println("Received response: " + receivedData);
            }
            catch (SerialPortException ex) {
                System.out.println("Error in receiving string from COM-port: " + ex);
            }
        }
        //If the CTS line status has changed, then the method event.getEventValue() returns 1 if the line is ON and 0 if it is OFF.
        else if(serialPortEvent.isCTS()){
            if(serialPortEvent.getEventValue() == 1){
                System.out.println("CTS - ON");
            }
            else {
                System.out.println("CTS - OFF");
            }
        }
        else if(serialPortEvent.isDSR()){
            if(serialPortEvent.getEventValue() == 1){
                System.out.println("DSR - ON");
            }
            else {
                System.out.println("DSR - OFF");
            }
    }}

    public void write(String data) {
        try {
            port.writeString(data);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public synchronized void close() {
        if (port != null) {
            try {
                port.removeEventListener();
                port.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
