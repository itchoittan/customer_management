package bean;

import java.io.Serializable;
import java.util.Date;

public class Drink implements Serializable{

	private static final long serialVersionUID = 1L;


	private int drink_id;
	private int customer_id;
	private String drink;
	private int drinkprice;
	private Date orderdate;
	private int quantity;


	public Drink(int drink_id, int customer_id, String drink, int drinkprice, Date orderdate, int quantity) {
		this.drink_id = drink_id;
		this.customer_id = customer_id;
		this.drink = drink;
		this.drinkprice = drinkprice;
		this.orderdate = orderdate;
		this.quantity = quantity;
	}

	public int getDrink_id() {
		return drink_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public String getDrink() {
		return drink;
	}

	public int getDrinkprice() {
		return drinkprice;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public int getQuantity() {
		return quantity;
	}

}
