package sem.group5.bob.client;

import sem.group5.bob.client.ConnectionManager;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Emanuel on 4/4/2016.
 */
public class DiscoveryListener implements Runnable {
    DatagramSocket c;
    String ip = "";

    public void run() {
        try {
            c = new DatagramSocket(1235);
            c.setBroadcast(true);
            byte[] sendData = "Discovery_Pi_Request".getBytes();

            //Try with 255.255.255.255
            try{
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 1235);
                c.send(sendPacket);
                System.out.println(getClass().getName() + "Request packet sent to: 255.255.255.255");
            }catch (Exception e){
            }
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()){
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()){
                    continue;
                }
               for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()){
                   InetAddress broadcast = interfaceAddress.getBroadcast();
                   if (broadcast == null){
                       continue;
                   }
                   // Send the broadcast package
                   try{
                       DatagramPacket sendPacket= new DatagramPacket(sendData, sendData.length, broadcast, 1235);
                       c.send(sendPacket);
                   }catch(Exception e){
                   }
                   System.out.println(getClass().getName() + "Request package sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());

               }
            }
            System.out.println(getClass().getClass() + "Waiting for a reply");
            while (true) {
                //waiting for response
                byte[] recvBuf = new byte[32];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                c.receive(receivePacket);

                //Getting a response
                System.out.println(getClass().getName() + "Broadcast response from: " + receivePacket.getAddress().getHostAddress());
                System.out.println(receivePacket.getData());

                //Check if message checks
                String message = new String(receivePacket.getData()).trim();
                if (message.equals("Discovery_Pi_Response")) {
                    this.ip = receivePacket.getAddress().toString().substring(1);
                    System.out.println("Found Pi at ip: " + receivePacket.getAddress().toString().substring(1));
                    break;
                }
            }

        }catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getIp() {
        return this.ip;
    }
}