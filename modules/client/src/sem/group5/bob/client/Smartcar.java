package sem.group5.bob.client;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

class Smartcar extends Observable{
    private Socket socket;
    private Writer out;
    private InputStream in;
    private IOException e;

    /**
     * Initialize a new connection to a remote smartcar
     *
     * @param socket used for communication
     */
    Smartcar(Socket socket) {
        try {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);
            this.socket.setKeepAlive(true);
            this.socket.setSoTimeout(8*1000);
            this.in = socket.getInputStream();
            this.out = new PrintWriter(socket.getOutputStream());
           }catch(InterruptedIOException e){
            notifyConnectionLost();
            } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Set speed of the remote SmartCar
     *
     * @param speed speed in percentage of max capacity
     */
    void setSpeed(int speed) throws IOException{
        try {
            out.write("s" + speed + "/");
            out.flush();
        } catch (InterruptedIOException e) {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Set angle at which to turn the remote SmartCar
     *
     * @param angle angle in degrees
     */
    void setAngle(int angle) throws IOException{
        try {
            String toSend = "a";
            out.write(toSend + angle + "/");
            out.flush();
        } catch (InterruptedIOException e) {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Rotate the remote smartcar on the spot
     * @param angle amount of rotation in degrees
     */
    void rotate(int angle) throws IOException {
        try {
            String toSend = "r";
            out.write(toSend + angle + "/");
            out.flush();
        } catch (InterruptedIOException e) {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Method to close sockets in the client side and server side
     * @throws IOException
     */
    void close() throws IOException{
        out.write("close/");
        out.flush();
        out.close();
        this.socket.close();
    }

    /**
     * Method to notify observer in case of errors
     */
    private void notifyConnectionLost()
    {
       setChanged();
       notifyObservers("Socket Failed");
    }

    /**
     *
     * @return if it is still connect to bobCar
     */
    boolean isConnected()
    {
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * Method to get the thrown exception
     */
    IOException getE() {
        return e;
    }

}


