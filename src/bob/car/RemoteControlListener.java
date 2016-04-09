package bob.car;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteControlListener {
    private InputStream in;
    SmartcarComm sc;

    public static void main(String[] args) {
        try {
            System.out.println("Listening");
            SmartcarComm sc = new SmartcarComm();
            RemoteControlListener rcl = new RemoteControlListener(1234, sc);

        } catch (Exception e) {
            System.out.println("Failed " + e.getMessage());
        }

        Thread t = new Thread() {
            public void run() {
                // the following line will keep this app alive for 1000 seconds,
                // waiting for events to occur and responding to them (printing
                // incoming messages to console).
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        };
        t.start();
        System.out.println("Started");

    }

    public RemoteControlListener(int port, SmartcarComm sc) throws IOException {
        ServerSocket listener = new ServerSocket(port);
        this.sc = sc;

        Socket socket = listener.accept();
        while (!socket.isClosed()) {
            try {
                in = socket.getInputStream();
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
                    } else if (first == 'a') {
                        sc.setAngle(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                    } else if (first == 'r') {
                        sc.setRotate(Integer.parseInt(buffer.substring(1,buffer.indexOf('/'))));
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }
}