package sem.group5.bob.car.streaming;

import sem.group5.bob.car.BobCarObserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

public class MjpegStreamer extends Observable implements Runnable{
    private ServerSocket serverSocket;
    private DepthJpegProvider cjp;
    private Socket socket;

    public MjpegStreamer(Socket s, DepthJpegProvider cjp, BobCarObserver bobCarObserver) {
        this.serverSocket = serverSocket;
        this.socket = s;
        this.cjp = cjp;
        addObserver(bobCarObserver);
    }

    private void stream() throws Exception
    {
        System.out.println("Streaming");
        OutputStream out = socket.getOutputStream();
        out.write( ( "HTTP/1.0 200 OK\r\n" +
                     "Server: YourServerName\r\n" +
                     "Connection: close\r\n" +
                     "Max-Age: 0\r\n" +
                     "Expires: 0\r\n" +
                     "Cache-Control: no-cache, private\r\n" +
                     "Pragma: no-cache\r\n" +
                     "Content-Type: multipart/x-motion-jpeg; " +
                     "boundary=BoundaryString\r\n\r\n" ).getBytes() );

        byte[] data;
        while (!socket.isClosed()) {
            System.out.println("Sending frame");
            data = cjp.getLatestJpeg();
            out.write(("--BoundaryString\r\n" +
                    "Content-type: image/jpeg\r\n" +
                    "Content-Length: " + data.length + "\r\n\r\n").getBytes());
            out.write(data);
            out.write("\r\n\r\n".getBytes());
            out.flush();
        }
    }

    public void run(){
        try{
            this.stream();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            closeStreamingSocket();
        }
    }

    private void closeStreamingSocket()
    {
        try {
            serverSocket.close();
            socket.shutdownOutput();
            socket.close();
            setChanged();
            notifyObservers(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
