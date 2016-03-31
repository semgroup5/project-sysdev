package SmartCarInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;

public class Smartcar {
    //"127.0.0.1"
    static public String ip;
    //1234
   static public int port;
    Socket socket;
    Writer out;
//    public static void main(String args[]){
//        try {
//
//            System.out.println("Connecting");
//            Smartcar smc = new Smartcar(ip,port);//change this port number
//        } catch (Exception e) {
//            System.out.println("Failed " + e.getMessage());
//        }
//    }

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
           }catch(IOException e){
                System.out.println(e);
            }
        }

    /**
     * Set speed of the remote Smartcar
     *
     * @param speed speed in percentage of max capacity
     */
    public void setSpeed(int speed) throws IOException{
        out.write("s" + speed);
        out.flush();
    }

    /**
     * Set angle at which to turn the remote Smartcar
     *
     * @param angle angle in degrees
     */
    public void setAngle(int angle) throws IOException{
        String toSend = "a";
        out.write(toSend + angle);
        out.flush();
    }

    /**
     * Rotate the remote smartcar on the spot
     *
     * @param angle amount of rotation in degrees
     */
    public void rotate(int angle) throws IOException {
        String toSend = "r";
        out.write(toSend + angle);
        out.flush();
    }
    public void close()throws IOException{
        this.socket.close();
    }
}


