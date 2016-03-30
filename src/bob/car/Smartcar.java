package bob.car;

import bob.common.ISmartcar;

import java.net.Socket;

/**
 * Created by jpp on 30/03/16.
 */
public class Smartcar implements ISmartcar{
    Socket socket;

    public Smartcar(String ip, int port) {
        //Start thread to initialize connection
        //Thread Start



        //Thread END
    }

    public void setConnection(Socket socket){
        this.socket = socket;
    }

    @Override
    public void rotate(int angle) {

    }

    @Override
    public void setAngle(int angle) {

    }

    @Override
    public void setSpeed(int speed) throws Exception {

    }
}

public class ConnectionMaker implements Runnable{
    String ip;
    int port;

    public ConnectionMaker(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {

    }
}