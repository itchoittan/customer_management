package bean;

import java.io.Serializable;

public class FoodPrice implements Serializable{

	private static final long serialVersionUID = 1L;

	private int food_price_id;
	private String food;
	private int foodprice;

	public FoodPrice(int food_price_id, String food, int foodprice) {
		this.food_price_id = food_price_id;
		this.food = food;
		this.foodprice = foodprice;
	}

	public int getFood_price_id() {
		return food_price_id;
	}

	public String getFood() {
		return food;
	}

	public int getFoodprice() {
		return foodprice;
	}
}
