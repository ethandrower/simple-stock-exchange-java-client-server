import java.net.Socket;
import java.util.Queue;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {
	
	// This class represents the initial LOCAL running client.
	
  // methods
	/*
	 *  1.  connect to server and passes  clientID, type EXEC 
	 *  
	 *  	1a.  make a NEW ClientGateway object passing client ID
	 *  	 the Client gateway reader is a SECOND SOCKET, and it
	 *         listens for messages from gateway.
	 *  
	 *  2.  send orders to server -> wait for stdin, or show menu etc.
	 *  
	 *  
	 *  
	 *  3. send order cancelleations
	 *  
	 *  4. request market data.
	 * 
	 * 
	 * 
	 */
	//private String clientID;
	//private ClientConnectionType connectionType;
	
	
	
	//Args from client  args[clientID, connectionType]
	public static void main(String args[])
	{
		String clientID = args[0];
		String connectionType = args[1];
		
		switch(connectionType){
		
		case "FEED":
			
			new Thread(
					new ClientGatewayReader()).start();					
			
				
						
			break;
			
		case "EXEC":
			//create exec connection
			new Thread(
					new ClientGatewayExec()).start();	
			
			
			break;
			
		default:
			System.out.println("Could not read connection type. Exiting...");
			
		}
		
		
		
	}
	
	public static void startExec(String clientID, String connType) throws IOException{
		
		//read args from user input.
		//open socket connection to server.
		Socket connExchange = new Socket("localhost", 123);
		BufferedReader input = new BufferedReader(new InputStreamReader(connExchange.getInputStream()));
		PrintWriter output = new PrintWriter(connExchange.getOutputStream());
		
		
		// send initial message to server to identify connection type.  array[clientID, Type]
		String initExec = clientID + "|" +  "EXEC";
		output.println(initExec);
		
		boolean isRunning = true;
		//Loop asking for user input, sending orders etc.    
		
		
		
		while(isRunning)
		{
			printMenu();
			Console c = System.console();
			String command = c.readLine();
			int action = Integer.parseInt(command);
			switch(action){
			case 1:
				//creat order
				break;
				
			case 2: 
				//cancel order
				break;
				
			case 3:
				//request market data
				break;
			case 4:
				//exit
				isRunning = false;
				break;
				
			default:
				System.out.println("Not a valid option");
				break;
			
			}
			
			
		}
		
		
	}
	public static void printMenu(){
		System.out.println("You are connected to the exchange as an EXEC Feed:");
		System.out.println("1 : Create Order" );
		System.out.println("2 : Cancel Order" );
		System.out.println("3 : Request Market Data");
		System.out.println("4 : Quit");
		
		
	}
	
	public static void startFeed(String clientID, int port) throws IOException{

		Socket connFeedExchange = new Socket("localhost", 123);
		BufferedReader input = new BufferedReader(new InputStreamReader(connFeedExchange.getInputStream()));
		PrintWriter output = new PrintWriter(connFeedExchange.getOutputStream());
		
		
		// send initial message to server to identify connection type.  array[clientID, Type]
		String initExec = clientID + "|" +  "FEED";
		output.println(initExec);
		
		boolean isRunning = true;
		
		while(isRunning)
		{
			String feed = input.readLine();
			System.out.println("Message from gateway received!");
			System.out.println(feed);
		}
		
	}
	
	
}


