package sem.group5.bob.car;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
/**
 * This class implements client sockets that allows data read from the Kinect to be communicated to the PC side.
 * @see java.util.Observable
 */

class DepthStreamSocket extends Observable {
    private Socket socket;
    private ServerSocket serverSocket;

    /**
     * A method that establishes a socket connection with a preset port and arguments.
     */
    DepthStreamSocket() {
        try {

            System.out.println("Opening depth socket");
            int port = 50001;
            //Establish a new connection at port 50001
            serverSocket = new ServerSocket(port);

            //enables to reuse a socket even if it was busy
            serverSocket.setReuseAddress(true);

            this.socket = serverSocket.accept();

            // Sets true to turn off Nagle's algorithm to improve packet latency
            //todo set to false and see if the latency change is notable compared to depth packet stability
            this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);
            System.out.println("Stream socket established");
        }

        catch(Exception e) {
            System.out.println("Couldn't Create Socket");
            e.printStackTrace();
            setChanged();
            notifyObservers(this);
        }
    }

    /**
     * Method to close the created depth stream socket
     */
    void closeSocketDepthStream() {
        try {

            serverSocket.close();

            // Send the remaining data and terminate the outgoing connection
            socket.shutdownOutput();
            socket.close();

            // Catch and log any errors
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to return the value of the depth socket
     * @return socket
     */
    Socket getSocket() {
        return this.socket;
    }
}
