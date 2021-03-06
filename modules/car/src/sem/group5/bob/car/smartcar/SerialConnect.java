package sem.group5.bob.car.smartcar;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Properties;

/**
 * Class responsible for establishing the serial port connection between the raspberry pi and the arduino.
 */
class SerialConnect extends Observable implements SerialPortEventListener {
    private SerialPort serialPort;
    private int retryArduinoConnect = 0;


    // The port that's being used for the connection.

    private static final String PORT_NAMES[] = {
            //"/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyACM0", // Raspberry Pi
            //"/dev/ttyACM2", // Raspberry Pi
            //"/dev/ttyACM3", // Raspberry Pi
    };

    private BufferedReader input;
    private OutputStream output;

    //      Milliseconds to block while waiting for port open
    private static final int TIME_OUT = 2000;

    //Default bits per second for COM port.
    private static final int DATA_RATE = 9600;

    /**
     * Method to establish the serial connection.
     * Support for Windows, MAC and Linux.
     */
    void initialize() {
        Properties properties = System.getProperties();
        String currentPorts = properties.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
        properties.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");


        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        /**
         * First, Find an instance of serial port as set in PORT_NAMES.
         */
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }

        try {
            System.out.println("Opening port.");
            if(portId == null) throw new Exception("portId Null");
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            System.out.println("Setting parameter");
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            System.out.println("Opening streams");
            input = new BufferedReader(new InputStreamReader(
                    serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            char ch = 1;
            System.out.println("Writing to port.");
            output.write(ch);

            System.out.println("Adding event listeners");
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            //Catch and logs errors
        } catch (Exception e) {
            System.out.println("Caught exception");
            e.printStackTrace();
        }
    }

    /**
     * Method to close the serial connection
     */
    synchronized void close() {
        // If the port is open
        if (serialPort != null) {
            try {
                //Close the port connections.
                output.close();
                input.close();
                serialPort.removeEventListener();
                serialPort.close();
                System.out.println("Serial Port Closed Successfully");

                //Catch and logs errors
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to return this OutputStream
     *
     * @return output
     */
    OutputStream getOutputStream() {
        return this.output;
    }


    /**
     * Method that will be watching over events in the serial connection port
     *
     * @param oEvent Object event
     */

    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                //Sends the input line to class PoseManager
                String inputLine = input.readLine();
                System.out.println(inputLine);
                setChanged();
                notifyObservers(inputLine);

                //Catch and logs errors
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}