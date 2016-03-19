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
	PrintWriter out = null;
	BufferedReader in = null;

	

public static void main(String[] args)
{
	boolean running = true;

	int pin = 5; //5 on pi-blaster (GPIO 23 or P16)
	Client client = new Client();
	
	while (running)
	{
		
	String PWM;
	PWM =  JOptionPane.showInputDialog(null, "Enter a PWM between 0 and 1");
	if (PWM.equalsIgnoreCase("quit"))
	{
		running = false;
	}
	else 
		{running = true;
	
	double percentage = Double.parseDouble(PWM);
	
	try {
		client.motor(pin, percentage);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	}
}
	 
	public Client()
	{

		int port = 1234; //port number
		String pi = "129.16.78.84"; //IP address

		System.out.println("Welcome to the MotorController client for the Rasberry Pi.");

		try
		{

			outputSocket = new Socket(pi, port);
			System.out.println("Connected on port: " + port);

			out = new PrintWriter(outputSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(outputSocket.getInputStream()));

		}catch (UnknownHostException e) {
			System.err.println("Don't know about host: Raspberry Pi.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to: Raspberry Pi.");
			System.exit(1);
		}
	
	}
	
	

	public void motor (int pin, double percentage) throws IOException 
	{
		String PWM = pin + "=" + percentage;
		out.println(PWM);
	}
}

