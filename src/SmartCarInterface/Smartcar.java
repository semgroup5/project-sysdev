package SmartCarInterface;

import java.io.*;
import java.net.Socket;

public class Smartcar {
    static public String ip = "127.0.0.1";
   static public int port = 1234;
    Socket socket;
    Writer out;
    public static void main(String args[]){
        try {
            System.out.print("Initializing...");
            Smartcar SCC = new Smartcar(ip,port);//change this port number
        } catch (Exception e) {
            System.out.print("Failed " + e.getMessage());
        }
    }

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
            while (true) {
                out.append("").append("\n");
                out.flush();
            }}catch(IOException e){
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


