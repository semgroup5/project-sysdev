package Connect;

/**
 * Created by Geoffrey on 2016/3/28.
 */


        import java.net.*;
        import java.io.*;


public class Client {

    ObjectInputStream Sinput;		// to read the socket
    ObjectOutputStream Soutput;	// towrite on the socket
    Socket socket;

    // Constructor connection receiving a socket number
    Client(int port) {
        // we use "localhost" as host name, the server is on the same machine
        // but you can put the "real" server name or IP address
        try {
            socket = new Socket("localhost", port);
        }
        catch(Exception e) {
            System.out.println("Error connectiong to server:" + e);
            return;
        }
        System.out.println("Connection accepted " +
                socket.getInetAddress() + ":" +
                socket.getPort() + "\n");

		/* Creating both Data Streams */
        try
        {
            Sinput  = new ObjectInputStream(socket.getInputStream());
            Soutput = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Exception creating new Input/output Streams: " + e);
            return;
        }
        // my connection is established
        String test = new String ();
        test = "What is the date & time now ??";
        // send the question (String) to the server
        System.out.println("Client sending \"" + test + "\" to serveur\n");
        try {
            Soutput.writeObject(test);
            Soutput.flush();
        }
        catch(IOException e) {
            System.out.println("Error writting to the socket: " + e);
            return;
        }
        // read back the answer from the server
        String response;
        try {
            response = (String) Sinput.readObject();
            System.out.println("Read back from server: " + response);
        }
        catch(Exception e) {
            System.out.println("Problem reading back from server: " + e);
        }

        try{
            Sinput.close();
            Soutput.close();
        }
        catch(Exception e) {}
    }

    public static void main(String[] arg) {
        new Client(5006);
    }
}



