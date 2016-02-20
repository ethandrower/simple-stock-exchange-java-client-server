
public enum ClientConnectionType {  // these are the two types of socket connection we can have.
	// Exec is a connection where the server is listening in a loop, and client sends orders/cancel requests
	//FEED is a connection where the client is listening in a loop, and server sends price info, fill reports etc.
	
     EXEC, FEED
}
