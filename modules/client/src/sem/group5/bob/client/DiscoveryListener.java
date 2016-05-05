package sem.group5.bob.client;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
class DiscoveryListener extends Observable{
    private DatagramSocket socket;
    private String ip;
    private boolean isListing;


    /**
     * Method to listen to the Ip from bobCar
     */
    void listenIp() {
        try {
            isListing = true;
            ip = "";

            //Open a socket to listen to UPD traffic that is aimed at this port
            socket = new DatagramSocket(1235, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            while(isListing){
                System.out.println(getClass().getName() + " Ready to receive broadcast packets");
                //Receive a packet
                byte[] recBuf = new byte[32];
                DatagramPacket packet = new DatagramPacket(recBuf, recBuf.length);
                System.out.println("Waiting for packets...");
                socket.receive(packet);

                //Packet received
                System.out.println(getClass().getName() + "DiscoveryBroadcaster packet received from: " + packet.getAddress().getHostAddress());
                System.out.println(getClass().getName() + "Packet received; data: " + new String(packet.getData()));

                //See if the packet is the one we need
                String message = new String(packet.getData());
                if (message.equals("BobCar_Server_IP")){
                    this.ip = packet.getAddress().toString().substring(1);
                    System.out.println("Found Pi at carIp: " + packet.getAddress().toString().substring(1));

                    byte[] sendData = "BobCar_Client_Response".getBytes();

                    //send an answer
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length ,packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                    System.out.println(getClass().getName() + "Sent packet to: " +  sendPacket.getAddress().getHostAddress());
                    System.out.println("Done listening IP");
                    close();
                    break;
                }
            }
        }catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
            close();
        }

    }

    /**
     * Method to shut down the discoveryListener
     */
    private void close() {
        isListing = false;
        System.out.println("Disconnecting");
        if (socket.isConnected()) socket.disconnect();
        System.out.println("Closing Socket");
        socket.close();
        System.out.println("Discovery listener closed");
    }


    /**
     * Method to return the found IP adress
     * @return ip
     */
    String getIp() {
        return this.ip;
    }
}