package sem.group5.bob.car.streaming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * This class implements client sockets that allows data read from the Kinect to be communicated to the PC side.
 * @see java.util.Observable
 */

public class DepthVideoStreamSocket extends Observable
{
    private Socket socket;
    private ServerSocket serverSocket;

    /**
     * A method that establishes a socket connection with a preset port and arguments.
     */
    public DepthVideoStreamSocket(int port)
    {
        try
        {

            System.out.println("Opening Stream socket");

            //Establish a new connection at port
            serverSocket = new ServerSocket(port);

            //enables to reuse a socket even if it was busy
            serverSocket.setReuseAddress(true);

            this.socket = serverSocket.accept();

            // Sets true to turn off Nagle's algorithm to improve packet latency
            // this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);
            System.out.println("Stream socket established");
        }

        catch(Exception e)
        {
            System.out.println("Couldn't Create Socket");
            setChanged();
            notifyObservers(this);
        }
    }

    /**
     * Method to close the created depth stream socket
     */
    public void closeSocketStream()
    {
        try
        {
            serverSocket.close();
            socket.close();
            System.out.println("Stream Socket Closed");

            // Catch and log any errors
        } catch (IOException e)
        {
            System.out.println("Caught Exception");
        }
    }

    /**
     * Method to return the value of the depth socket
     * @return socket
     */
    public Socket getSocket()
    {
        return this.socket;
    }
}
