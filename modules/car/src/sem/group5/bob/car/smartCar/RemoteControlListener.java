package sem.group5.bob.car.smartcar;

import org.openkinect.freenect.TiltStatus;
import sem.group5.bob.car.network.BobCarSocketTimer;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * Class responsible for establishing a connection and handling input between a remote control (PC Client) and BobCar.
 * @see java.util.Observable
 * @see java.lang.Runnable
 */

public class RemoteControlListener extends Observable implements Runnable
{
    private InputStream in;
    private Writer out;
    private int port;
    private SmartCarComm sc;
    private Socket socket;
    private ServerSocket listener;
    public BobCarSocketTimer timer;
    private boolean socketOpen;
    private Thread threadHeartbeat;

    /**
     * Constructor
     * @param port socket port for communication
     * @param sc smartCarComm to send data to arduino
     */
    public RemoteControlListener(int port, SmartCarComm sc)
    {
        this.port = port;
        this.sc = sc;
    }

    /**
     * Opens up the port and handle received inputs in it.
     * Adds a timer to the socket.
     *
     * read received input from the remote control as char and adds arguments.
     * 's' set speed, 'a' set Angle, 'r' rotate, '/' close connection.
     * @see SmartCarComm#setAngle(int)
     * @see SmartCarComm#setSpeed(int)
     * @see SmartCarComm#setRotate(int)
     */
    private void listen()
    {
        socket = null;
        listener = null;

        System.out.println("Establishing control sockets");
        try
        {
            //Creates a server socket at this port
            listener = new ServerSocket(port);

            //Enables the socket to be reused
            listener.setReuseAddress(true);
            System.out.println("Waiting for the client");

            //Accepts and establish connection
            socket = listener.accept();
            socketOpen = true;
            setChanged();
            notifyObservers("Done Broadcasting");

            //Time out on the socket if no commands received, restarts counting after the last command.
            timer = new BobCarSocketTimer(60*1000, this);
            timer.start();
            timer.reset();

            // Sets true to turn off Nagle's algorithm to improve packet latency
            socket.setTcpNoDelay(true);

            //Enable the socket to be reused even if busy.
            socket.setReuseAddress(true);

            System.out.println("Controls socket established!");
            timer.reset();

            in = socket.getInputStream();
            out = new PrintWriter(socket.getOutputStream());

            // sends a heartbeat to check if the connection is still alive
            sendHeartBeatToClient();
            timer.reset();
            // Catch and logs errors
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        // While the socket is open
        while (socketOpen)
        {
            try
            {
                String buffer = "";

                while (in.available() > 0)
                {
                    buffer += (char)in.read();
                }

                if(buffer.length() > 0)
                {
                    char first = buffer.charAt(0);
                    if (first == 's')
                    {
                        sc.setSpeed(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (first == 'a')
                    {
                        sc.setAngle(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (first == 'r')
                    {
                        sc.setRotate(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (buffer.substring(0,buffer.indexOf('/')).equals("close"))
                    {
                        closeConnections();
                    } else if (first == 'k')
                    {
                        System.out.println("Tilting Kinect");
                        if (BobCarConnectionManager.device != null)
                        {
                            BobCarConnectionManager.device.refreshTiltState();
                            if (BobCarConnectionManager.device.getTiltStatus() != TiltStatus.MOVING)
                            {
                                BobCarConnectionManager.device.setTiltAngle(Double.parseDouble(buffer.substring(1,buffer.indexOf('/'))));
                            }
                        }
                        timer.reset();
                    }
                    else if (first == 'x'){
                        sc.resetArduino();
                    }
                }

                // Catch and logs errors
            }catch(Exception e)
            {
                System.out.println("Caught Exception");
            }
        }
    }
    /**
     * A "heartbeat" method to check whether network connection is alive.
     * The method sends an out string "A" every x seconds, the string is then read and conformed by the client.
     */
    private void sendHeartBeatToClient()
    {
        threadHeartbeat = new Thread(()->{
            while (socketOpen)
            {
                try
                {
                    String beat = "A";
                    out.write(beat);
                    out.flush();
                    Thread.sleep(10*1000);
                }catch (IOException | InterruptedException e)
                {
                    break;
                }
            }
        });
        threadHeartbeat.start();
    }

    /**
     * Used by the thread to run this application simultaneously
     */
    public void run()
    {
        listen();
    }

    /**
     * Closes the network connections and notifies an observer, notifies observer.
     */
    public void closeConnections()
    {
        try
        {
            socketOpen = false;
            if (threadHeartbeat.isAlive())threadHeartbeat.interrupt();
            timer.setCountingDown(false);
            if (timer.isAlive())timer.interrupt();
            listener.close();
            in.close();
            socket.close();
            System.out.println("All connections were closed!");
            setChanged();
            notifyObservers("Connection Closed");

            // Catches and logs errors
        }catch (Exception e)
        {
            System.out.println();
            e.printStackTrace();
        }
    }
}