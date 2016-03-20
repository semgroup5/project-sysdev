package udpSockets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

/*This is an example of a Java Client which connects to a server..
 * We are using the Pi's IP (10.0.1.201) and port 1234 to connect
 * String PWM will be modified to only take doubles and not strings
 * This program will send any values entered in the string to the Server
 */

public class Client {

	Socket outputSocket = null;
	OutputStream out = null;
	static InputStream in ;

	

public static void main(String[] args)
{
	int pin = 5; //5 on pi-blaster (GPIO 23 or P16)
	Client client = new Client();
}
	 
	public Client()
	{

		int port = 1234; //port number
		String pi = "192.168.1.83"; //IP address

		System.out.println("Welcome to the MotorController client for the Rasberry Pi.");

		try
		{

			outputSocket = new Socket(pi, port);
			System.out.println("Connected on port: " + port);

			out = outputSocket.getOutputStream();
			in = outputSocket.getInputStream();

		}catch (UnknownHostException e) {
			System.err.println("Don't know about host: Raspberry Pi.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to: Raspberry Pi.");
			System.exit(1);
		}

		boolean running = true;


        JFrame jFrame = new JFrame("hey");
        BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);

        Graphics g1 = bi.getGraphics();
        g1.setColor(Color.GREEN);
        g1.fillRect(0,0,640,480);

        ImageIcon ii = new ImageIcon(bi);
        JLabel lbl=new JLabel();
        lbl.setIcon(ii);
        jFrame.add(lbl);

        //jFrame.getContentPane().add(j, BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(640,480);
        jFrame.setVisible(true);

        Graphics g;
        try{
			while (running)
			{
				{
					out.write(5);
                    int count = 0;
					while(in.available() > 0 && count<614400) {
                        int b = in.read() << in.read();
                        count++;
                        bi.setRGB(count%640, (count/640)%480, b);
					}
                    System.out.println("count" + count);
                    g = bi.getGraphics();
                    lbl.paint(g);
                    jFrame.repaint();
                    Thread.sleep(1000);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Caught Exception " + e.getMessage());
		}

		System.exit(1);
	}
}

