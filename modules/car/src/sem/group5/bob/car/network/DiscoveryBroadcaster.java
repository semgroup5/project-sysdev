package sem.group5.bob.car.network;

import sem.group5.bob.car.RemoteControlListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that will be broadcasting the raspberry pi when needed
 * Created by Emanuel on 4/4/2016.
 */
public class DiscoveryBroadcaster {
    private DatagramSocket socket;

    /**
     * Method to start the broadcasting
     */
    public void startIPBroadcast() {
        try{
            //Open a socket to listen to UPD traffic that is aimed at this port
            socket = new DatagramSocket(1235, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            socket.setReuseAddress(true);

            while(true){
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
                if (message.equals("Discovery_Pi_Request")){
                    byte[] sendData = "Discovery_Pi_Response".getBytes();

                    //send an answer
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length ,packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);

                    System.out.println(getClass().getName() + "Sent packet to: " +  sendPacket.getAddress().getHostAddress());
                    System.out.println("Done Broadcasting IP");
                    socket.close();
                    break;
                }
            }
        }catch(IOException ex){
            Logger.getLogger(DiscoveryBroadcaster.class.getName()).log(Level.SEVERE, null, ex);
            socketKill();
        }
    }

    /**
    Method to close socket when encountering error on runtime
     */
    public void socketKill(){
        System.out.print(socket + "closing socket..");
        try{
            socket.close();
            System.out.print("Socket closed");
        }catch(Exception e) {
            System.out.print("Could not close socket");
        }
        startBroadcast();
    }

    private static class DiscoveryHolder{
        private static final DiscoveryBroadcaster INSTANCE = new DiscoveryBroadcaster();
    }
    public static DiscoveryBroadcaster getInstance(){
        return DiscoveryHolder.INSTANCE;
    }
}