

public class Exchange {

	
	//  1.  Listen for incoming connections
	//  2.  Start a thread for each connection
	//		2a.  generate unique ID and pass to connection object
			//2b.  we will need to create a dictionary of clientID, and the var representing
			// the thread so that messages can be sent to specific Ids i believe.
	
	//  3.  form threadsafe collection for incoming orders/messages
			
	
	
	
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
	
	
	
	
	
}
