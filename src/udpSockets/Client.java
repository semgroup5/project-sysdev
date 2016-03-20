package udpSockets;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

/*This is an example of a Java Client which connects to a server.
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

		try{
			while (running)
			{
				int PWM;
				PWM =  JOptionPane.showConfirmDialog(null, "Ping", "Server", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (PWM == JOptionPane.YES_OPTION)
				{
					running = true;
					out.write(5);

					while(in.available() > 0) {
						System.out.println("Starting to read");
						System.out.print(in.read());
					}
				}
				else
				{
					running = false;
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

