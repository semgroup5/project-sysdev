package sem.group5.bob.car;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * Class responsible for establishing connection between
 * client and raspberry pi and use received data to forward it to
 * the arduino
 */

class RemoteControlListener extends Observable implements Runnable{
    private InputStream in;
    private Writer out;
    private int port;
    private SmartCarComm sc;
    private Socket socket;
    private ServerSocket listener;
    private BobCarSocketTimer timer;

    /**
     * Constructor
     * @param port socket port for communication
     * @param sc smartCarComm to send data to arduino
     */
    RemoteControlListener(int port, SmartCarComm sc) {
        this.port = port;
        this.sc = sc;
    }

    /**
     * Method to open up the port and handle received inputs in it
     */
    private void listen()
    {
        socket = null;
        listener = null;

        System.out.println("Establishing control sockets");
        try{
            //Creates a server socket at this port
            listener = new ServerSocket(port);

            //Enables the socket to be reused
            listener.setReuseAddress(true);
            System.out.println("Waiting for the client");

            //Accepts and establish connection
            socket = listener.accept();
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

        // Catch and logs errors
        }catch(Exception e){
            e.printStackTrace();
        }
        // While the socket is open
        while (!socket.isClosed()) {
            try {
                String buffer = "";

                while (in.available() > 0) {
                        buffer += (char)in.read();
                }

                if(buffer.length() > 0)
                {
                    char first = buffer.charAt(0);
                    if (first == 's') {
                        sc.setSpeed(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (first == 'a') {
                        sc.setAngle(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (first == 'r') {
                        sc.setRotate(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                        timer.reset();
                    } else if (buffer.substring(0,buffer.indexOf('/')).equals("close")) {
                        closeConnections();
                    }
                }

                // Catch and logs errors
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * A "heartbeat" method to check whether network connection is alive.
     */
    private void sendHeartBeatToClient()
    {
        Thread thread = new Thread(()->{
            while (!socket.isClosed()) {
                try {
                    String beat = "A";
                    out.write(beat);
                    out.flush();
                    Thread.sleep(10*1000);
                }catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
           thread.start();
    }

    /**
     * Method used by the thread to run this application simultaneously
     */
    public void run(){
        listen();
    }

    /**
     * Method to close the network connections.
     */
    void closeConnections() {
        try {
            listener.close();
            in.close();
            socket.close();
            System.out.println("All connections were closed!");
            setChanged();

            // Notify observer class to update the method
            notifyObservers("Connection Closed");

            // Catches and logs errors
        }catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
    }
}