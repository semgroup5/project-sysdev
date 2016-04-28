package sem.group5.bob.car;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Class responsible for establishing the serial port connection between the raspberry pi and the arduino.
 */
class SerialConnect implements SerialPortEventListener {

    private SerialPort serialPort;

    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            //"/dev/ttyUSB0", // Linux
            "/dev/ttyACM0", // Raspberry Pi
            "COM4", // Windows
    };

    private BufferedReader input;
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    /**
     * Method to establish the serial connection
     */
    void initialize(){
        // the next line is for Raspberry Pi and
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        Properties properties = System.getProperties();
        String currentPorts = properties.getProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
        if (currentPorts.equals("/dev/ttyACM0")) {
            properties.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
        } else {
            properties.setProperty("gnu.io.rxtx.SerialPorts", currentPorts + File.pathSeparator + "/dev/ttyACM0");
        }

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();

            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            System.exit(1);
            return;
        }

        try {
            System.out.println("Opening port.");
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
            System.out.println("Setting parameter");
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            System.out.println("Opening streams");
            // open the streams
            input = new BufferedReader(new InputStreamReader(
                    serialPort.getInputStream()));
            output = serialPort.getOutputStream();
            char ch = 1;
            System.out.println("Writing to port.");
            output.write(ch);

            System.out.println("Adding event listeners");
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.out.println("Caught exception");
            System.err.println(e.toString());
        }
    }

    /**
     * Method to close the serial connection
     */
    synchronized void close() {
        if (serialPort != null) {
            try {
                output.close();
                input.close();
                serialPort.removeEventListener();
                serialPort.close();
                System.out.println("Serial Port Closed Successfully");
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to return this BufferReader
     * @return @
     */
    BufferedReader getBufferReader() {
        return this.input;
    }

    /**
     * Method to return this OutputStream
     * @return @
     */
    OutputStream getOutputStream() {
        return this.output;
    }

    /**
     * Method that will be watching over events in the serial connection port
     * @param oEvent @
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}