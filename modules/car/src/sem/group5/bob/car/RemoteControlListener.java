package sem.group5.bob.car;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class responsible for establishing connection between client and raspberry pi and use received data to forward it to the arduino
 */
public class RemoteControlListener implements Runnable{
    private InputStream in;
    int port;
    SmartCarComm sc;
    Socket socket;

    /**
     * Constructor
     * @param port
     * @param sc
     */
    public RemoteControlListener(int port, SmartCarComm sc) {
        this.port = port;
        this.sc = sc;
    }

    /**
     * Method to open up the port and handle received inputs in it
     */
    public void listen()
    {
        socket = null;

        try{
            ServerSocket listener = new ServerSocket(port);
            socket = listener.accept();
            socket.setTcpNoDelay(true);
            socket.setReuseAddress(true);
        }catch(Exception e){
            e.printStackTrace();
        }

        while (!socket.isClosed()) {
            try {
                in = socket.getInputStream();
                String buffer = "";

                if (!socket.isConnected()) closeConnections();

                //if there's any input do the following
                while (in.available() > 0) {
                    buffer += (char)in.read();
                }

                if(buffer.length() > 0)
                {
                    char first = buffer.charAt(0);
                    if (first == 's') {
                        sc.setSpeed(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                    } else if (first == 'a') {
                        sc.setAngle(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                    } else if (first == 'r') {
                        sc.setRotate(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                    } else if (buffer.substring(0,buffer.indexOf('/')).equals("close")) {
                        closeConnections();
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used by the thread to run this application simultaneously
     */
    public void run(){
        listen();
    }

    public void closeConnections() {
        try {
            in.close();
            socket.close();
            System.out.println("All connections were closed!");
            BobCar.startDiscoveryListener();
            run();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}