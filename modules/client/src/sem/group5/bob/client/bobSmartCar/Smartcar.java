package sem.group5.bob.client.bobSmartCar;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

/**
 * Class that will establish the remote connection with BobCar and send the controls input from the controller.
 * @see java.util.Observable
 */
public class Smartcar extends Observable
{
    private Socket socket;
    private Writer out;
    private IOException e;

    /**
     * Initialize a new connection to a remote smartcar
     * @param socket the socket used for connection
     */
    public Smartcar(Socket socket)
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
     * Set angle at which to turn the remote SmartCar
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
     * Rotate the remote smartcar on the spot
     * @param angle amount of rotation in degrees
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
     * Method to close sockets in the client controller.
     * @throws IOException
     */
    public void close() throws IOException
    {
        out.write("close/");
        out.flush();
        out.close();
        this.socket.close();
    }

    /**
     * Method to notify observer in case of connection error
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
   public boolean isConnected()
    {
        return socket.isConnected() && !socket.isClosed();
    }

    /**
     * Method to get the IoException error
     * @return  This error
     */
    public IOException getE()
    {
        return e;
    }

}


