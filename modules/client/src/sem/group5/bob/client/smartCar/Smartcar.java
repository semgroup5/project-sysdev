package sem.group5.bob.client.smartcar;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

/**
 * Class that will establish the remote connection with BobCar and send the controls input from the controller.
 * @see java.util.Observable
 */
class Smartcar extends Observable
{
    private Socket socket;
    private Writer out;
    private IOException e;

    /**
     * Initialize a new connection to a remote smartcar
     * @param socket the socket used for connection
     */
    Smartcar(Socket socket)
    {
        try
        {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);
            this.socket.setKeepAlive(true);
            this.socket.setSoTimeout(8*1000);
            this.out = new PrintWriter(socket.getOutputStream());
        }catch(InterruptedIOException e)
        {
            notifyConnectionLost();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    /**
     * Moves the kinect's motor to change the viewing angle on a y axes.
     * @param angle angle of the kinect view.
     * @throws IOException
     */
    void tiltkinect(double angle) throws IOException
    {
        try
        {
            out.write("k" + angle + "/");
            out.flush();
            //Catch connection error
        } catch (InterruptedIOException e)
        {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Set speed of the remote SmartCar
     * @param speed speed in percentage of max capacity
     * @throws IOException
     */
    void setSpeed(int speed) throws IOException
    {
        try
        {
            out.write("s" + speed + "/");
            out.flush();
            //Catch connection error
        } catch (InterruptedIOException e)
        {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Set angle at which to turn BobCar.
     * @param angle angle in degrees
     */
    void setAngle(int angle) throws IOException
    {
        try
        {
            String toSend = "a";
            out.write(toSend + angle + "/");
            out.flush();

        } catch (InterruptedIOException e)
        {
            this.e = e;
            notifyConnectionLost();
        }
    }


    /**
     * Rotate the smartcar on the spot
     * @param angle rotation angle.
     * @throws IOException
     */
    void rotate(int angle) throws IOException
    {
        try
        {
            String toSend = "r";
            out.write(toSend + angle + "/");
            out.flush();
        } catch (InterruptedIOException e)
        {
            this.e = e;
            notifyConnectionLost();
        }
    }

    /**
     * Soft resets the arduino
     * @throws IOException
     */
    void resetArduino() throws IOException{
    try{
        String toSend = "x";
        out.write(toSend + "/");
        out.flush();
    }catch (InterruptedIOException e){
        this.e = e;
        notifyConnectionLost();
    }

}

    /**
     * Close sockets in the client controller.
     * @throws IOException
     */
    void close() throws IOException
    {
        out.write("close/");
        out.flush();
        out.close();
        this.socket.close();
    }

    /**
     * Notify observer in case of connection error
     */
    private void notifyConnectionLost()
    {
        setChanged();
        notifyObservers("Socket Failed");
    }

    /**
     * boolean checks if the socket is connected.
     * @return true if it is still connected to bobCar and the socket is open.
     */
    boolean isConnected()
    {
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * Gets IoException error
     * @return Io Exception error
     */
    IOException getE()
    {
        return e;
    }

}


