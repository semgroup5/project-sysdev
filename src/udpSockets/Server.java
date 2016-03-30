package udpSockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/*This is an example of a Java server which accepts a client and prints entries.
 * This file also interacts with a Shell script on our Raspberry Pi (/home/pi/scripts/motorcontrol.sh)
 * It forwards the inputs from the client to motorcontrol.sh and runs the file which echos values to Pi-Blaster
 */

public class Server 
{ 
	private static Socket client = null;
	private ServerSocket server = null;
	private DataInputStream streamIn = null;

	public static void main(String[] args) throws IOException,  java.lang.InterruptedException
	{ 
		int port = 1234; 
		boolean serverUp = true;
		ServerSocket server = null;

		try 
		{ 


			System.out.println("Listening on port " + port);
			server = new ServerSocket(1234); 
		}
		catch(IOException e) {
			System.err.println("Could not listen on port: 1234.");
			System.exit(1);
		}

		while( true ) {
			Socket client = null;
			try {
				client = server.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String inputLine;
			
			out.println("Connected to the Raspberry Pi server".toString());


			while ((inputLine = in.readLine()) != null) {
				try {
					// Client can force shutdown of server
					if (inputLine.equals("shutdown")) {
						System.out.println("Shutting down");
						server.close();
						System.exit(0);
					}
					
					String PWM = (inputLine);
					System.out.println(PWM);
					Runtime rt = Runtime.getRuntime();
					Process p = rt.exec(new String[] {"/bin/bash", "-c", "/home/pi/scripts/motorcontrol.sh " + PWM});
					
					BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null;
					while ((line = in2.readLine()) != null)
					{
						System.out.print(line);
					}
					
				} catch(IOException e) {
					e.printStackTrace();
					throw e;
				}

				try
				{


					System.out.println("Listening on port " + port);
					server = new ServerSocket(1234);
				}
				catch(IOException e1) {
					System.err.println("Could not listen on port: 1234.");
					System.exit(1);
				}
			}
			out.close();
			in.close();
			client.close();
		}
	}



}

