package sem.group5.bob.car;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

class DepthStreamSocket extends Observable {
    private Socket socket;
    private ServerSocket serverSocket;

    DepthStreamSocket() {
        try {

            System.out.println("Opening depth socket");
            int port = 50001;
            //Establish a new connection at port 50001
            serverSocket = new ServerSocket(port);

            //enables to reuse a socket even if it was busy
            serverSocket.setReuseAddress(true);

            // Listen to a connection to be made with the socket and accepts it
            this.socket = serverSocket.accept();

            // Sets true to turn off Nagle's algorithm to improve packet latency
            this.socket.setTcpNoDelay(true);
            this.socket.setReuseAddress(true);
            System.out.println("Stream socket established");
        }
        /**
         * Logs errors in case of connection failure
         * and send it to a reconnect method in BobCarObserver Class
         */
        catch(Exception e) {
            System.out.println("Couldn't Create Socket");
            e.printStackTrace();
            setChanged();
            notifyObservers(this);
        }
    }

    /**
     * Method to close the created socket
     */
    void closeSocketDepthStream() {
        try {
            //Closes the server
            serverSocket.close();

            // Send the remaining data and terminate the outgoing connection
            socket.shutdownOutput();
            socket.close();

            // Catch and log any errors
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket getSocket() {
        return this.socket;
    }
}
