package sem.group5.bob.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

class DiscoveryListener {
    private DatagramSocket socket;
    private String ip = "";
    private boolean listening;


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
                    break;
                }
                Thread.sleep(5000);
            }
        }catch (IOException ex) {
            Logger.getLogger(DiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void close() {
        listening = false;
        socket.disconnect();
        socket.close();
        System.out.println("Discovery listener closed");
    }

    String getIp() {
        return this.ip;
    }
}