package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Drink;
import bean.PopularOrder;

public class DrinksTable extends DbAccess {

	public int drinkInsert(int customer_id, int drink_price_id, Date orderdate, int quantity) throws SQLException {

		int autoIncrementKey = 0;

		PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO drinks(customer_id,drink_price_id,orderdate,quantity) VALUES(?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, customer_id);
		pstmt.setInt(2, drink_price_id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pstmt.setString(3, sdf.format(orderdate));
		pstmt.setInt(4, quantity);

		pstmt.executeUpdate();
		System.out.println("drinkデータの挿入に成功しました。");

		// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
		ResultSet res = pstmt.getGeneratedKeys();

		if (res.next()) {
			autoIncrementKey = res.getInt(1);
		}

		res.close();
		pstmt.close();

		return autoIncrementKey;
	}

	public ArrayList<PopularOrder> popularRead(Date inputOrderdate) throws SQLException {

		ArrayList<PopularOrder> lists = new ArrayList<>();

		PreparedStatement pstmt = connection
				.prepareStatement(
						"SELECT drink,drinkprice,SUM(drinkprice*quantity) AS sumprice, SUM(quantity) AS sumquantity FROM drinks JOIN drink_prices ON drinks.drink_price_id=drink_prices.drink_price_id WHERE orderdate>=? AND orderdate<=? GROUP BY drinks.drink_price_id ORDER BY sumquantity DESC ");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01");
		SimpleDateFormat sdf31 = new SimpleDateFormat("yyyy-MM-31");
		pstmt.setString(1, sdf1.format(inputOrderdate));
		pstmt.setString(2, sdf31.format(inputOrderdate));

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			String drink = rs.getString("drink");
			int drinkprice = rs.getInt("drinkprice");
			int sumprice = rs.getInt("sumprice");
			int sumquantity = rs.getInt("sumquantity");

			PopularOrder popularOrder = new PopularOrder(drink, drinkprice, sumprice, sumquantity);
			lists.add(popularOrder);
		}
		rs.close();
		pstmt.close();

		return lists;
	}

	public ArrayList<Drink> newDateRead(int inputCustomer_id) throws SQLException {

		ArrayList<Drink> lists = new ArrayList<>();

		PreparedStatement pstmt = connection
				.prepareStatement(
						"SELECT * FROM drinks JOIN drink_prices ON drinks.drink_price_id=drink_prices.drink_price_id WHERE customer_id=? AND orderdate=(SELECT MAX(orderdate) FROM drinks WHERE customer_id=?)");
		pstmt.setInt(1, inputCustomer_id);
		pstmt.setInt(2, inputCustomer_id);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			int drink_id = rs.getInt("drinks.drink_id");
			int customer_id = rs.getInt("customer_id");
			String drink = rs.getString("drink");
			int drinkprice = rs.getInt("drinkprice");
			Date orderdate = rs.getTimestamp("orderdate");
			int quantity = rs.getInt("quantity");

			Drink drinks = new Drink(drink_id, customer_id, drink, drinkprice, orderdate, quantity);
			lists.add(drinks);
		}
		rs.close();
		pstmt.close();

		return lists;
	}

	public void update(int customer_id, int drink_price_id, Date orderdate, int quantity) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE drinks SET customer_id=?,drink_price_id=?,orderdate=?,quantity=? WHERE drink_id=?");

		pstmt.setInt(1, customer_id);
		pstmt.setInt(2, drink_price_id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pstmt.setString(3, sdf.format(orderdate));
		pstmt.setInt(4, quantity);

		pstmt.executeUpdate();
		System.out.println("drinkデータの更新に成功しました。");

		pstmt.close();

	}

	public void customerIdUpdate(int old_customer_id, int new_customer_id) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE drinks SET customer_id=? WHERE customer_id=?");

		pstmt.setInt(1, new_customer_id);
		pstmt.setInt(2, old_customer_id);
		pstmt.executeUpdate();
		System.out.println("drinkデータの更新に成功しました。");

		pstmt.close();
	}

}
