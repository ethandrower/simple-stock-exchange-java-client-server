import java.awt.List;
import java.util.Dictionary;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.*;
public class Exchange {

	private boolean isStopped;
	
	//  1.  Listen for incoming connections
	//  2.  Start a thread for each connection  ( each thread will be a Connection object)
	private ServerSocket serverSock; //servers main listening socket.
	
	private Dictionary<String, Connection> clientFeeds;
	
	private ConcurrentMap<Double, Queue<Order>> orderbook;
	
	public static void main(String args[]) throws IOException{
		
		//Read port from stdinput
		// Then init one Exchange object.  this will create all the message queues and order structs
		//  call runServer
		int port = Integer.parseInt(args[0]);
		Exchange OurExchange = new Exchange();
		
		OurExchange.runServer(port);
		
	}
	
	public Exchange(){
		// constructor for our main exchange
		
		
	}
	public void runServer(int port) throws IOException{
		
		Socket clientSock = null;
		this.serverSock = new ServerSocket(port);
		
		
		
		while(!isStopped){
			try {
				
				clientSock = this.serverSock.accept();
			}// end try
			catch (IOException e) {
				System.out.println("Error connecting to client");
			}
			
			System.out.println
			
			new Thread(
					new Connection(clientSock, this) ).start();					
			
			
			
		}//end while 
		
		
	}// end runsever
	
	public void addOrder(Order orderToAdd)
	{
		System.out.println("Exchange addOrder has been called");
		
		//if we cant fill right away, add the order to the orderbook
		if( !instantFill(orderToAdd))
		{
			//add the order to the book
			orderbook.get(orderToAdd.price).add(orderToAdd);
			
			//Send a fill message to the correct client
			clientFeeds.get(orderToAdd.clientID).feedMessageQueue.add("Order added with ID: " + orderToAdd.orderID.toString());
			
		}

	}
	
	public boolean instantFill(Order orderToFill){
		
		if (orderToFill.type == OrderType.BUY)
		{
			
			for ( ConcurrentMap.Entry<Double, Queue<Order> > priceLevel : orderbook.entrySet())
			{
				for (Order individualOrder : priceLevel.getValue())
				{
					if (orderToFill.price <= individualOrder.price && individualOrder.type == OrderType.SELL)
					{
						priceLevel.getValue().remove(individualOrder);// remove the order.
						match(orderToFill, individualOrder);
						return true;		
					}			
				}
				
			}// end of outer for each
			
			
			
			
		} // end order type IF statement
		else if (orderToFill.type == OrderType.SELL)
		{
	
			
			Order currentBestFill = new Order();
			for ( ConcurrentMap.Entry<Double, Queue<Order> > priceLevel : orderbook.entrySet())
			{
				for (Order individualOrder : priceLevel.getValue())
				{
					
					
					if (orderToFill.price >= individualOrder.price && individualOrder.type == OrderType.BUY)
					{
						//potential match found,  lets keep looking for a better price.
						currentBestFill = individualOrder;	
						break;
					//	return true;		
					}			
				}
			}// end oouter for
			
			if (currentBestFill.isRealOrder)
			{
				
				orderbook.get(currentBestFill.price).remove(currentBestFill);  // this may be buggy
				
				//priceLevel.getValue().remove(individualOrder);// remove the order.
				match(orderToFill, currentBestFill);
				return true;
			}
			
		} // end iff 
		
		
		
		//
		return false;
		
	}
	
	public void match(Order orderOne, Order orderTwo)
	{
		// send fill notification to each clientID
		String fill = "Fill Notification!  Buy Side: " + orderOne.clientID + " Sell Side: " + orderTwo.clientID + " Price: " + String.valueOf(orderTwo.price) + "Quantity: " + String.valueOf(orderTwo.quantity) ;
		
		clientFeeds.get(orderOne.clientID).feedMessageQueue.add(fill);
		clientFeeds.get(orderTwo.clientID).feedMessageQueue.add(fill);
		
		//add to log somewhere*****
		
	}
	
	public void cancelOrder(String clientID, String orderID)
	{
		
		// iterate through the orderbook until you find the ORDER ID
		// when you do find it, remove it
		//then send message to the client.
		
		
		
	}
	
	public void sendMarketData(String clientID)
	{
		//giant strig builder   print statment that is all (top) orders in the queue.
		
		System.out.println("Sending Market Data...");
		
		StringBuilder book = new StringBuilder();
		
		
		// loop to build the book string
		for ( ConcurrentMap.Entry<Double, Queue<Order> > priceLevel : orderbook.entrySet()){
			
			Order order = priceLevel.getValue().peek();
		
			book.append("Price: " + String.valueOf(order.price) + "Quantity: " + String.valueOf(order.quantity) + "Type: " +  order.type.toString()  ) ;
			book.append(System.getProperty("line.separator"));
			
		//	book.append(
			
			
			//book.append(str)
		}
		
		//send book to the client that requested it
		clientFeeds.get(clientID).feedMessageQueue.add(book.toString());
	}
	
	
	public boolean registerClientFeed(String clientID, Connection connObject)
	{
		//this method needs to put clients in an exchange dictionary <clientID, connObject>
		
		this.clientFeeds.put(clientID,  connObject);

		return false;
		
	}
	public boolean removeClientFeed(String clientID )
	{
		this.clientFeeds.remove(clientID);
		return true;
		
	}
	
	
	//	
			//2b.  we will need to create a  dictionary of clientID, and the var representing
			// the thread so that messages can be sent to specific Ids i believe.
	// tthis dictionary will be public, and each Connection object will be responsible
	// for adding iteslf to the dictionary, as soon as it learns what 'type' of connection it 
	// is.  type being either exececution, or reporting .
	        
	//  3.  form threadsafe collection for incoming orders/messages

	// not sure we need the below...
	public ConcurrentLinkedQueue<Message> incomingQueue;

	
	//	4. write loop to continuously parse ^order queue 
	/*  logic for  loop is this
	 * 
	 * for each message in the messageQueue 
	 * 
	 * 		if its an order,  check the order book and see if it can be filled instantly
	 * 				if it can be filled, remove itself, and the the matched order and send
	 * 				a fill message to each clientID
	 * 
	 *      if it's an order cancelletion,  find the order and remove it.  send message to clientID
	 *      
	 */
	
	//  5.  Create data structure (or class) for an 'order book'
	//  this will need to store orders at various price levels and client ids 
	/*   The logic will be as follows
	 * 
	 * 
	 *   Generate a java map   Map <doubles, orders>
	 *   So it's a 2d data structure, the 'doubles' are the keys and represent 
	 *   order price levels, the 'orders' will be a separate class and will store the 
	 *   price (though it's self explanatory i guess), and the client ID that sent the order
	 *   We could add type to this, but probably not necessary right now.
	 * 
	 */
	
	//public ConcurrentMap<Double, Collection<String>> orderbook;  //order book for all prices.
	
	
	
	
	
	
}
