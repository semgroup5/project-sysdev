package sem.group5.bob.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Observable;

public class Smartcar extends Observable{
    Socket socket;
    Writer out;

    /**
     * Initialize a new connection to a remote smartcar
     *
     * @param socket
     */

    public Smartcar(Socket socket, ControllerGUI ctr) {
        try {
        addObserver(ctr);
        this.socket = socket;
        this.socket.setTcpNoDelay(true);
        this.socket.setReuseAddress(true);
        this.out = new PrintWriter(socket.getOutputStream());
           }catch(IOException e){
            setChanged();
            notifyObservers(this);
            e.printStackTrace();
            }
        }

    /**
     * Set speed of the remote SmartCar
     *
     * @param speed speed in percentage of max capacity
     */
    public void setSpeed(int speed) throws IOException{
        try {
            out.write("s" + speed + "/");
            out.flush();
        } catch (IOException e) {
            setChanged();
            notifyObservers(this);
            e.printStackTrace();
        }
    }

    /**
     * Set angle at which to turn the remote SmartCar
     *
     * @param angle angle in degrees
     */
    public void setAngle(int angle) throws IOException{
        try {
            String toSend = "a";
            out.write(toSend + angle + "/");
            out.flush();
        } catch (IOException e) {
            setChanged();
            notifyObservers(this);
            e.printStackTrace();
        }
    }

    /**
     * Rotate the remote smartcar on the spot
     * @param angle amount of rotation in degrees
     */
    public void rotate(int angle) throws IOException {
        try {
            String toSend = "r";
            out.write(toSend + angle + "/");
            out.flush();
        } catch (IOException e) {
            setChanged();
            notifyObservers(this);
            e.printStackTrace();
        }
    }

    /**
     * Method to close sockets in the client side and server side
     * @throws IOException
     */
    public void close()throws IOException{
        out.write("close/");
        out.flush();
        this.socket.close();

    }
}


