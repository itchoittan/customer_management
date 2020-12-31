package bean;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;

	private int message_id;
	private int customer_id;
	private String message;
	private Date orderdate;

	public Message(int message_id, int customer_id, String message, Date orderdate) {
		this.message_id = message_id;
		this.customer_id = customer_id;
		this.message = message;
		this.orderdate = orderdate;
	}

	public int getMessage_id() {
		return message_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public String getMessage() {
		return message;
	}

	public Date getOrderdate() {
		return orderdate;
	}
}
