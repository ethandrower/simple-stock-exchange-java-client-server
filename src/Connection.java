import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;

public class Connection implements Runnable {
	
	
	//  This will implement runnable
	
	//Each thread of this class will be a new client connection
	// on connection, we msut first read for the type of connection we are getting
	// type 1:  execution (meaning we listen for messages from client)
	// type 2:  feed/reporting(meaning the client will be listening for us)
	
	// we will also read from the initial client message, what the clien'ts id is
	// then we will check to see if it's valid.
	//  if it is valid,  then the Connection object will add itself to the Exchanges dictionary
	//  (static method call)  Exchange.register(this.socket, clientID, typeOfCon)   passing this threads socket
	// and the client ID associaated with it, and the type of conenction Exec/Reporting.
	
	private Socket conn;
	private String clientID;
	private ClientConnectionType connType;
	
	public Queue<String> feedMessageQueue;  // exchange will add things into this queue
	private boolean isStopped = false;
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Connection (Socket clientSocket)
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
