package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.DrinkPrice;

public class DrinkPricesTable extends DbAccess {

	//ドリンクのプルダウン表示に使用
	public ArrayList<DrinkPrice> allRead() {

		ArrayList<DrinkPrice> lists = new ArrayList<>();

		try (
				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM drink_prices");) {
			try (
					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {
					int drink_price_id = rs.getInt("drink_price_id");
					String drink = rs.getString("drink");
					int drinkprice = rs.getInt("drinkprice");

					DrinkPrice drinkPrice = new DrinkPrice(drink_price_id, drink, drinkprice);
					lists.add(drinkPrice);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lists;
	}
}
