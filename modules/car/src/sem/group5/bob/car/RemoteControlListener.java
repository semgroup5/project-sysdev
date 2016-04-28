package sem.group5.bob.car;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * Class responsible for establishing connection between client and raspberry pi and use received data to forward it to the arduino
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
     * @param bobCarObserver @
     */
    RemoteControlListener(int port, SmartCarComm sc, BobCarObserver bobCarObserver) {
        addObserver(bobCarObserver);
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
            listener = new ServerSocket(port);
            listener.setReuseAddress(true);
            System.out.println("Waiting for the client");
            socket = listener.accept();

            timer = new BobCarSocketTimer(60*1000, this);
            timer.start();
            timer.reset();


            socket.setTcpNoDelay(true);
            socket.setReuseAddress(true);
            socket.setSoTimeout(50*1000);
            socket.setKeepAlive(true);
            System.out.println("Controls socket established!");

            timer.reset();

        }catch(Exception e){
            e.printStackTrace();
        }

        while (!socket.isClosed()) {
            try {
                in = socket.getInputStream();
                out = new PrintWriter(socket.getOutputStream());
                sendHeartBeatToClient();
                String buffer = "";

                //if there's any input do the following
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

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void sendHeartBeatToClient()
    {
        Thread t = new Thread(()->{
            while (!socket.isClosed())
            {
                try {
                    String beat = "A";
                    out.write(beat);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    /**
     * Method used by the thread to run this application simultaneously
     */
    public void run(){
        listen();
    }

    /**
     *
     */
    void closeConnections() {
        try {
            listener.close();
            in.close();
            socket.close();
            System.out.println("All connections were closed!");
            setChanged();
            notifyObservers(this);
        }catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
    }
}