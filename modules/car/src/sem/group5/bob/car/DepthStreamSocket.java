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
            int port = 50001;
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            this.socket = serverSocket.accept();
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
     *
     */
    void closeSocketDepthStream() {
        try {
            serverSocket.close();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket getSocket() {
        return this.socket;
    }
}
