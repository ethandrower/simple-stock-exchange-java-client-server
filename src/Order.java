
import java.util.UUID;


public class Order
{
	//simple class that will contain a clientID string, and a price.
	// could be expanded to contain productID, order type, status...
	private double price;
	private int quantity;
	private String clientID;
	private UUID orderID;
	private OrderType type;
	
	public Order(int qty,  double price, String client, OrderType type)
	{
		this.price = price;
		this.quantity = qty;
		this.clientID = client;
		this.type = type;
		this.orderID = UUID.randomUUID();	
	}
	
}
