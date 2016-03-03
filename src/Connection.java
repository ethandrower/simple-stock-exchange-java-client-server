import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PriorityQueue;
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
	
	public PriorityQueue<String> feedMessageQueue;  // not working, skipping implementation aroudn this.
	
	private boolean isStopped = false;
	
	private PrintWriter out;
	private BufferedReader in;
	private Exchange exchange;
	
	
	public Connection (Socket clientSocket, Exchange excObj)
	{
		this.conn = clientSocket;
		this.exchange = excObj;
		feedMessageQueue = new PriorityQueue<String>();
		
		
	}
	
	public void run(){ //threading method
		
		//read from the client, determine type of connection, clientID
		
		try{
			 out = new PrintWriter(conn.getOutputStream(), true);
			 in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			 // now we must get the ID, and type from the client
			//Args from client  args[clientID, connectionType]
			 String bufferString = "";
			System.out.println("Reading for type of connection from client...");
			
			bufferString = in.readLine();
			String[] clientInfo = bufferString.split("\\|");
			
			this.clientID = clientInfo[0];
			System.out.println("Conn type passed is " + clientInfo[1]);
			
			this.connType = ClientConnectionType.valueOf(clientInfo[1].trim());	
			System.out.println("Starting feed for client " + clientID + "on connection type " + connType.toString()); 
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
	
	// format for exchange exec messages
			// New orders =  clientID + '|' + messageType + '|' + [orderType] + '|' + [quantity]+ '|' + [price]
			// Cancel Orders = clientID + '|' + messageType + '|' + [orderID]
			// request for market data = clientID + '|' + messageType
	public void runExec(){
		
		//listen in loop for messages from client
		// parse message, and add to exchange global queue 
		System.out.println("Starting exec connection now...");
		while(!isStopped){
			String input;
			try{
				
			
				while( ( input = in.readLine()) != null)
				{
					String[] messageArray = input.split("\\|", 5);
					System.out.println("Message received from the client: " + input);
					
					//now we process the message and send commands to exchange accordingly
					String messageType = messageArray[1];
					
					switch(messageType){
					
					case "NewOrder":
						//create new order object.
						System.out.println("New Order received!");
						Order incomingOrder = new Order(messageArray[0], OrderType.valueOf(messageArray[2]), Integer.parseInt(messageArray[3]), Double.parseDouble(messageArray[4]));
						exchange.addOrder(incomingOrder);	
						break;
						
					case "CancelOrder":
						
						break;
						
						
					case "MarketData":
						System.out.println("Sending Request for market data, client ID: " + messageArray[0]);
						
						exchange.sendMarketData(messageArray[0]);
						break;
						
					 default:
						 
						break;
					
					}
				}
			} catch (IOException e)
			{
				System.out.println("Exception throw while reading from exec feed");
			}
			
		}//end while
		
		
	}//end runExec
	
	public void runFeed() throws IOException{
		// wait for messages to be put in this connections 'queue'
		// transmist message to client.
		System.out.println("Registering client with exchange..." + this.toString());
		exchange.registerClientFeed(clientID, this);
		// out = new PrintWriter(conn.getOutputStream(), true);
		
		while(!isStopped)
		{
			//working but i don't know why i need the print...
		//	System.out.println("feed message queue is... " + feedMessageQueue.peek());
			//feedMessageQueue.peek();
			//System.out.println("FeedQueueSize = " +  this.feedMessageQueue.size());
			
		/*
			if (   feedMessageQueue.size() > 0 ){
				
				
					
					String message = this.feedMessageQueue.remove();
					
					System.out.println("Sending message to client: " + message);
					
					out.println(message);
					//this.feedMessageQueue.remove();
					
				}
				
			//}
				*/
		}//end while
		//System.out.println("Out of runFeedLoop");
	
			
	}// end runFeed
	
	public void addMessage(String message)
	{
		System.out.println("Connection adding new message to it's own queue");
		
		this.feedMessageQueue.add(message);
		out.println(message);
	}

}
