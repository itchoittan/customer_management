package bean;

import java.io.Serializable;
import java.util.Date;

public class Food implements Serializable{

	private static final long serialVersionUID = 1L;

	private int food_id;
	private int customer_id;
	private String food;
	private int foodprice;
	private Date orderdate;
	private int quantity;

	public Food(int food_id, int customer_id, String food, int foodprice, Date orderdate, int quantity) {
		this.food_id = food_id;
		this.customer_id = customer_id;
		this.food = food;
		this.foodprice = foodprice;
		this.orderdate = orderdate;
		this.quantity = quantity;
	}

	public int getFood_id() {
		return food_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public String getFood() {
		return food;
	}

	public int getFoodprice() {
		return foodprice;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public int getQuantity() {
		return quantity;
	}
}
