package bob.car;

import java.net.Socket;

public class SmartcarConnectionMaker implements Runnable{
    String ip;
    int port;
    Smartcar car;
    public SmartcarConnectionMaker(Smartcar car) {
        this.ip = car.ip;
        this.port = car.port;
        this.car = car;
    }

    @Override
    public void run() {
        try {
            car.setConnection(new Socket(ip, port));
        }
        catch(Exception e){
           car.setConnectionException(e);
        }
    }
}