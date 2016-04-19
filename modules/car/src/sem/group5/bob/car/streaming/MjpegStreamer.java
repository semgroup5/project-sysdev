package sem.group5.bob.car.streaming;

import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MjpegStreamer implements Runnable{
    String ip;
    int port;
    ServerSocket serverSocket;
    DepthJpegProvider cjp;

    public MjpegStreamer(ServerSocket s, DepthJpegProvider cjp) {
        this.serverSocket = s;
        this.cjp = cjp;
    }

    public void stream() throws Exception
    {
        System.out.println("Streaming");
        Socket socket = serverSocket.accept();
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
        while(true) {
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
        }
    }
}
