import java.net.Socket;
import java.util.Queue;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements Runnable{
	
	// This class represents the initial running client.
	
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
	
	private Socket conn;
	private String clientID;
	private ClientConnectionType connType;
	
	public Queue<String> feedMessageQueue;  // exchange will add things into this queue
	private boolean isStopped = false;
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Client (Socket clientSocket)
	{
		this.conn = clientSocket;
		
	}
	
	public void run(){ //threading method
		
		//read from the client, determine type of connection, clientID
		
		try{
			 out = new PrintWriter(conn.getOutputStream(), true);
			 in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			 // now we must get the ID, and type from the client
			String bufferString = "";
			bufferString = in.readLine();
			String[] clientInfo = bufferString.split("|", 5);
			this.clientID = clientInfo[0];
			this.connType = ClientConnectionType.valueOf(clientInfo[1]);
			 
			//we also need to add this connection to the exchange's dictionary of conenctions
			
			
			
			//now we know the client ID and the connection type, so lets do
			//our repetitive logic.
			switch(connType){
				
				case EXEC:
					runExec();
					break;
					
				case FEED:
					runFeed();
					break;
					
				default:
					System.out.println("Error starting client connection, improper connection type specified from client");
					conn.close();
					break;
			}//end switch
			
		}
		catch (IOException e)
		{
			System.out.println("error getting streams from client");
		}
		
		
	} // end of run
	
	public void runExec(){
		
		//listen in loop for messages from client
		// parse message, and add to exchange global queue 
		
		while(!isStopped){
			String input;
			try{
				
			
				while( ( input = in.readLine()) != null)
				{
					String[] messageArray = input.split("|", 5);
					//now we process the message and send commands to exchange accordingly
					
				}
			} catch (IOException e)
			{
				System.out.println("Exception throw while reading from exec feed");
			}
			
		}//end while
		
		
	}//end runExec
	
	public void runFeed(){
		// wait for messages to be put in this connections 'queue'
		// transmist message to client.
		
		while(!isStopped)
		{
			if (feedMessageQueue.size() > 0){
				for (String message : feedMessageQueue)
				{
					out.println(message);
				}
				
			}
			
		}//end while
	}// end runFeed
	
	
	

}


