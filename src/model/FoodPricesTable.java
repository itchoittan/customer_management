package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.FoodPrice;

public class FoodPricesTable extends DbAccess{


	public ArrayList<FoodPrice> allRead() {

		ArrayList<FoodPrice> lists = new ArrayList<>();

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM food_prices");) {
			try (

					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {

					int food_price_id = rs.getInt("food_price_id");
					String food = rs.getString("food");
					int foodprice = rs.getInt("foodprice");


					FoodPrice foodPrice = new FoodPrice(food_price_id, food, foodprice);
					lists.add(foodPrice);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;

	}


}
