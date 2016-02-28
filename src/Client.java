import java.net.Socket;
import java.util.Queue;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements Runnable{
	
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
	
	public static void startExec(){
		
		//read args from user input.
		//open socket connection to server.
		// send initial message to server to identify connection type.  array[clientID, Type]
		
		
	}
	
	
}


