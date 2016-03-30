package Connect.TestControls;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RemoteControlListener {
    private InputStream in;

    public static void main(String[] args) {
        try {
            System.out.print("Listening");
            RemoteControlListener rcl = new RemoteControlListener(1234);//change this port number
        } catch (Exception e) {
            System.out.print("Failed " + e.getMessage());
        }
    }

    public RemoteControlListener(int port) throws IOException {
        ServerSocket listener = new ServerSocket(port);
        SCAct sc = new SCAct();

        Socket socket = listener.accept();
        while (true) {
            try {
                in = socket.getInputStream();
                String buffer = "";
                while (in.available() > 0) {
                    buffer += in.read();
                    char first;
                    first = buffer.charAt(0);
                    if (first == 's') {
                        sc.setSpeed(Integer.valueOf(buffer.substring(1)));
                    } else if (first == 'a') {
                        sc.setSpeed(Integer.valueOf(buffer.substring(1)));
                    } else if (first == 'r') {
                        sc.setSpeed(Integer.valueOf(buffer.substring(1)));
                    }
                }
            }catch(Exception e){}
        }

    }

    public void listen(InputStream in) throws IOException {

        SCAct sc = new SCAct();
        String buffer = "";
        while (in.available() > 0) {
            buffer += in.read();
            char first;
            first = buffer.charAt(0);
            if (first == 's') {
                sc.setSpeed(Integer.valueOf(buffer.substring(1)));
            } else if (first == 'a') {
                sc.setSpeed(Integer.valueOf(buffer.substring(1)));
            } else if (first == 'r') {
                sc.setSpeed(Integer.valueOf(buffer.substring(1)));
            }
        }
    }
}
