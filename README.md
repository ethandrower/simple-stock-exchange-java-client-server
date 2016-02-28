# distrib-systems-final
csc376 final project
Project Proposal

Name of Project: Online Exchange

Students:
Ethan Drower
Chris Vick
Vince Zipparro
Prerak Thakkar

Summary (25 words or less):
We will create an online exchange that accepts and matches orders from connecting clients.


Functional Specification with Milestones:

Server:
A java socket server that listens for connections and shuts down gracefully when terminated   Feb 19th.
Server Launches a new thread for each connection acceptd.	Feb 19th.
Server generates unique ID for connected client, and sends to client as first response  Feb 20th
Server creates thread safe global container to hold/process all incoming orders Feb 28th
Server sends order confirmation when order has been received with a unique order ID. Feb 28th
Order processing logic to either match order instantly (and remove) or place order in an active threadsafe queue (by price level) Feb 28th
Sever sends fill notifications (of a trade executed) to the corresponding parties involved in the trade.  Feb 28th
Server accepts order cancellation requests from client, removes order from it's appropriate queue.  March 6th

Client:
Client connects to server and read it's assigned clientID (needed for orders) Feb 19th
Client creates and sends an order to the exchange - Feb 25th
Client receives and handles order confirmation from the exchange Feb 25th
Client requests 'market data' in the form of market depth orders from the exchange, and handles/prints the response Feb 25th
Client sends order cancellation message to server to remove order  March 6th
Client keeps track locally of 'current positions' and can display them upon request.  March 6th


Functional Specifications part II 
Notes on specific implementations.

The multi-threaded server instance will be responsible for several things.
1. Creating threads to represent client connections.
	a. Client connections will be one of two possible types, and EXEC type and a FEED type (as an enumeration)
	b. EXEC connections, the exchange server after establishing a socket, will perpetually read using a buffered InputStream for messages from a client.  As an EXEC connection, the server will be expecting commands from a client.  Creating an order, or cancelling an order.
	c.  FEED connections will represent a 'push' style of socket, where the client will be perpetually waiting for the server to send messages.  The server will send notifications of trades, market data updates, and notifications of cancellations to the client.  

2.  Messaging Protocol.  There will be a standardized messaging protocol allowing quick and simple communication between clients and servers.  the form will (tentatively) be a string following this format:


	clientID + '|' + messageType + '|' + [orderType] + '|' + [orderID] + '|' + [price]

clientID - this will be a unique ID that each client will present upon initial exchange connection.
The exchange server will use a dictionary with Key = clientID, Value = socket connection object.

messageType -  message type will define how the server handles the request.  it can be one of:
	New Order - place an order for purchase/sale
	Cancel order - find the order ID specified and remove it from the order book.

OrderID - orderID to be cancelled,  if not needed simply set to 0

Price - price to place a new order at, if not needed simply set to 0

All fields will be separated by the '|' character. This is so the exchange server can read a client message as 1 string, parse it by the '|' char and quickly handle the required action appropriately.


3.  The order book. 
  For the order book, I have decided to use a concurrentMap object.  The keys of the map will be doubles, one double for each price.  The values will be Queues of Order objects.    Doubles are used to allow our exchange to offer 'real' price levels. Perhaps this needs to be restricted to 2 decimal places.

  Queues allow us to maintain order priority, meaning first in first out at each price level.  The queue will make the matching engine's job easier in determining which order to fill first.  
  
  ConcurrentMap orderBook <Double, Queue<Orders> > 

  This data structure will provide a thread safe collection to hold multiple orders at multiple price levels.  It will be the matching engine's job to parse the Map appropriately and match orders.


4.  Handling requests from multiple clients.

The exchange server will also maintain an 'incoming message'  concurrent thread-safe queue.  All connection threads will be able to place their messages into the queue, and the queue due to it's nature will maintain integrity of time by processing messages in the order they are received.   


5.  The matching engine.  

The matching engine logic will be called when incoming orders are received.  The basic logic will be as follows:

On reception of a new order,  parse the orderBook to see if there are any orders that can be matched (price agreement) right away.  
If yes, remove that matched order, and notify both parties (both  ClientIDs )  that a trade  has been made.  
Then, log the trade to a trades log file on the server.
If no (no match was found right away), add the order object to the queue at it's specified price level.





