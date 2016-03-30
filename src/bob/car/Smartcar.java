package bob.car;

import bob.common.ISmartcar;

import java.net.Socket;

/**
 * Created by jpp on 30/03/16.
 */
public class Smartcar implements ISmartcar{
    Socket socket;
    public final String ip;
    public final int port;
    Exception connectionException;
    public Smartcar(String ip, int port) {
        //Start thread to initialize connection
        this.ip = ip;
        this.port = port;

        //Thread START
        SmartcarConnectionMaker connectionMaker = new SmartcarConnectionMaker(this);
        Thread ConnectionThread = new Thread(connectionMaker);
        ConnectionThread.run();
        //Thread END
    }

    public void setConnection(Socket socket){
        this.socket = socket;
    }

    public void setConnectionException(Exception e)
    {
        this.connectionException = e;
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
