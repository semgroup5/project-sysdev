package Connect.TestControls;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

public class Smartcar {
    String ip;
    int port;
    Socket socket;
    Writer out;

    /**
     * Initialize a new connection to a remote smartcar
     *
     * @param ip
     *            IP at which the smartcar is connected to the network
     * @param port
     *            port at which the smartcar is listening
     */
    public Smartcar(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream());
            System.out.println("Socket established");
            out.append("").append("\n");
            out.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Set speed of the remote Smartcar
     *
     * @param speed
     *            speed in percentage of max capacity
     */
    public void setSpeed(int speed) throws IOException{
        out.write("s" + speed);
        out.flush();
    }

    /**
     * Set angle at which to turn the remote Smartcar
     *
     * @param angle
     *            angle in degrees
     */
    public void setAngle(int angle) throws IOException{
        String toSend = "a" + angle;
        out.write(toSend);
    }

    /**
     * Rotate the remote smartcar on the spot
     *
     * @param angle
     *            amount of rotation in degrees
     */
    public void rotate(int angle) throws IOException {
        String toSend = "r" + angle;
        out.write(toSend);
    }
}

// int port;
// AxelsSmartcar sc; //This is the *other* smartcar class
//
//
/// **
// * Initialize a RemoteControlListener
// * @param port which port to listen at
// */
//
// public RemoteControlListener(int port, AxelsSmartcar sc) {
// this.port = port;
// }
//
//
// public void listen()
// {
// //Initialize a SocketServer at port
//
// //Wait for clients with .accept()
//
// //While there's bytes available
//
// //Read and react to them
//
// }
// }
