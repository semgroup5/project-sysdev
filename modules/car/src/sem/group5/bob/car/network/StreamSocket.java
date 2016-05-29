package sem.group5.bob.car.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * Class implements client sockets that allows depth data read from the kinect to be communicated to the PC side.
 * @see java.util.Observable
 */

public class StreamSocket extends Observable
{
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean socketOpen = false;

    /**
     * Establishes a socket connection with a preset port.
     * fixing
     */
    public StreamSocket(int port)
    {
        try
        {
            System.out.println();
            System.out.println("Opening Stream socket");

            //Establish a new connection at port
            serverSocket = new ServerSocket(port);

            //enables to reuse a socket even if it was busy
            serverSocket.setReuseAddress(true);

            this.socket = serverSocket.accept();
            socketOpen = true;

            // Sets true to turn off Nagle's algorithm to improve packet latency
            this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);

            System.out.println("Stream socket established");
        }
        catch(Exception e)
        {
            System.out.println("Couldn't Create Socket!");
        }
    }

    /**
     * Close the created depth stream socket
     */
    public void closeSocketStream()
    {
        if (isSocketOpen()) {
            try
            {
                serverSocket.close();
                socket.close();
                System.out.println("Stream Socket Closed!");

            } catch (IOException e)
            {
                System.out.println("Caught Exception");
            }
        }
    }

    private boolean isSocketOpen()
    {
        return socketOpen;
    }

    /**
     * A getter for the value of the depth socket
     * @return socket
     */
    public Socket getSocket()
    {
        return this.socket;
    }
}
