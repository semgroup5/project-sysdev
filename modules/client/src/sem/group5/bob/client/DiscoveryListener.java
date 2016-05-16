package sem.group5.bob.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The DiscoveryListener will scan for devices attempting connection with the client,
 * it will detect BobCar IP and handle it with a set of methods.
 */


class DiscoveryListener
{
    private DatagramSocket socket;
    private String ip = "";
    private boolean listening;


    /**
     * listenIP() method will scan and filter incoming connections based on the pockets received,
     * once BobCar pocket is received its IP will be accepted and stored to establish a connection..
     */
    void listenIp() {
        try {

            listening = true;

            socket = new DatagramSocket(1235, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            while(listening) {
                System.out.println(getClass().getName() + " Ready to receive broadcast packets");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
                System.out.println(getClass().getName() + "DiscoveryBroadcaster packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + "Packet received; data: " + new String(packet.getData()));

                //See if the packet is the one we need
                String message = new String(packet.getData()).trim();
                if (message.equals("BobCar_Server")) {
                    this.ip = packet.getAddress().toString().substring(1);
                    System.out.println("Found BobCar At IP: " + packet.getAddress().toString().substring(1));
                    close();
                }
            }
        }catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
            close();
        }
    }

    /**
     * Method to close the IP listener
     * @see DiscoveryListener#listenIp()
     */
    private void close() {
        listening = false;
        if (socket.isConnected()) socket.disconnect();
        socket.close();
        System.out.println("Discovery listener closed");
    }

    /**
     * Method to get BobCar IP
     * @return this IP
     */

    String getIp() {
        return this.ip;
    }
}