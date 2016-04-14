package sem.group5.bob.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

public class Smartcar {
    Socket socket;
    Writer out;

    /**
     * Initialize a new connection to a remote smartcar
     *
     * @param socket
     */

    public Smartcar(Socket socket) {
        try {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream());
           }catch(IOException e){
                System.out.println(e);
            }
        }

    /**
     * Set speed of the remote SmartCar
     *
     * @param speed speed in percentage of max capacity
     */
    public void setSpeed(int speed) throws IOException{
        out.write("s" + speed + "/");
        out.flush();
    }

    /**
     * Set angle at which to turn the remote SmartCar
     *
     * @param angle angle in degrees
     */
    public void setAngle(int angle) throws IOException{
        String toSend = "a";
        out.write(toSend + angle + "/");
        out.flush();
    }

    /**
     * Rotate the remote smartcar on the spot
     * @param angle amount of rotation in degrees
     */
    public void rotate(int angle) throws IOException {
        String toSend = "r";
        out.write(toSend + angle + "/");
        out.flush();
    }
    public void close()throws IOException{
        out.write("c");
        out.flush();
        this.socket.close();
    }
}


