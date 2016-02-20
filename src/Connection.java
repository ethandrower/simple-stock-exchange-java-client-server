
public class Connection {
	
	
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
	
	
	

}
