package sem.group5.bob.car;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteControlListener implements Runnable{
    private InputStream in;
    int port;
    SmartCarComm sc;

    public RemoteControlListener(int port, SmartCarComm sc) {
        this.port = port;
        this.sc = sc;
    }

    public void listen()
    {
        Socket socket = null;

        try{
            ServerSocket listener = new ServerSocket(port);
            socket = listener.accept();
        }catch(Exception e){
            e.printStackTrace();
        }

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
                    } else if (buffer.substring(0,buffer.indexOf('/')).equals("close")) {
                        try {
                            sc.closeConnection();
                            socket.close();
                            System.out.println("All connections were closed!");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void run(){
        listen();
    }
}