package bean;

import java.io.Serializable;

public class DrinkPrice implements Serializable {

	private static final long serialVersionUID = 1L;

	private int drink_price_id;
	private String drink;
	private int drinkprice;

	public DrinkPrice(int drink_price_id, String drink, int drinkprice) {
		this.drink_price_id = drink_price_id;
		this.drink = drink;
		this.drinkprice = drinkprice;
	}

	public int getDrink_price_id() {
		return drink_price_id;
	}

	public String getDrink() {
		return drink;
	}

	public int getDrinkprice() {
		return drinkprice;
	}
}
