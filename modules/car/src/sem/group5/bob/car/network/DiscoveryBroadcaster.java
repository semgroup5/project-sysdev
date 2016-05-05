package sem.group5.bob.car.network;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscoveryBroadcaster implements Runnable{
    private DatagramSocket socket;

    /**
     * Broadcasts IP address to the network until it find a specific message, if the message is found it sends its IP address back as a answer.
     */
    private void startIPBroadcast() {
        try{

            socket = new DatagramSocket(1235);
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            byte[] sendData = "BobCar_Server_IP".getBytes();

            //Try with 255.255.255.255
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 1235);
            socket.send(sendPacket);
            System.out.println(getClass().getName() + " Request packet sent to: 255.255.255.255");

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
                        socket.send(sendPacket);
                    }catch(Exception ignored){
                    }
                    System.out.println(getClass().getName() + " Request package sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());

                }
            }
            System.out.println(getClass().getClass() + " Waiting for a reply");
            while (true) {
                //waiting for response
                byte[] receiveBuf = new byte[32];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
                if (!socket.isClosed()) socket.receive(receivePacket);

                //Getting a response
                System.out.println(getClass().getName() + " Broadcast response from: " + receivePacket.getAddress().getHostAddress());
                System.out.println(Arrays.toString(receivePacket.getData()));

                //Check if message checks
                String message = new String(receivePacket.getData()).trim();
                if (message.equals("BobCar_Client_Response")) {
                    socket.close();
                    break;
                }
            }
        }catch(IOException ex){
            Logger.getLogger(DiscoveryBroadcaster.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Used by the thread to run the function listen.
     */
    @Override
    public void run() {
        startIPBroadcast();
    }
}