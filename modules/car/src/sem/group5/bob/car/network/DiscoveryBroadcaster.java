package sem.group5.bob.car.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to discover the server on the network and attempts a connection with it.
 * @see java.lang.Runnable
 * @see java.util.Observer
 */
public class DiscoveryBroadcaster implements Runnable, Observer {
    private DatagramSocket socket;
    private boolean broadcasting;

    /**
     * Method to start the broadcasting
     */
    private void startIPBroadcast()
    {
        try
        {
            broadcasting = true;

            //Open a socket to broadcast to UPD traffic that is aimed at this port
            socket = new DatagramSocket(1235);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            while(broadcasting)
            {
                System.out.println(getClass().getName() + " Broadcasting packets to the network");

                //send a packet to the network
                byte[] sendBuf = "BobCar_Server".getBytes();
                DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,InetAddress.getByName("255.255.255.255"), 1235);
                socket.send(packet);
                Thread.sleep(2000);

            }
        }catch(IOException ex)
        {
            Logger.getLogger(DiscoveryBroadcaster.class.getName()).log(Level.SEVERE, null, ex);

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Used by the thread to run the function listen.
     */
    @Override
    public void run()
    {
        startIPBroadcast();
    }

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg)
    {
        if (arg.equals("Done Broadcasting"))
        {
            broadcasting = false;
            System.out.println("Disconnecting IP Broadcaster...");
            socket.disconnect();
            socket.close();
            System.out.println("IP Broadcaster disconnected!");
        }
    }
}