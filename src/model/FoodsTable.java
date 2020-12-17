package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Food;
import bean.PopularOrder;

public class FoodsTable extends DbAccess {

	public int foodInsert(int customer_id, int food_price_id, Date orderdate, int quantity) {

		int autoIncrementKey = 0;

		try (

				PreparedStatement pstmt = connection.prepareStatement(
						"INSERT INTO foods(customer_id,food_price_id,orderdate,quantity) VALUES(?,?,?,?)");) {

			pstmt.setInt(1, customer_id);
			pstmt.setInt(2, food_price_id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(3, sdf.format(orderdate));
			pstmt.setInt(4, quantity);

			pstmt.executeUpdate();
			System.out.println("データの挿入に成功しました。");

			// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
			ResultSet res = pstmt.getGeneratedKeys();

			if (res.next()) {
				autoIncrementKey = res.getInt(1);
			}

			res.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return autoIncrementKey;
	}


	public ArrayList<PopularOrder> popularRead(Date inputOrderdate) {

		ArrayList<PopularOrder> lists = new ArrayList<>();

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT food,foodprice,SUM(foodprice*quantity) AS sumprice, SUM(quantity) AS sumquantity FROM foods JOIN foods_prices ON foods.food_price_id=food_prices.food_price_id WHERE orderdate>=? AND orderdate<=? GROUP BY foods.food_price_id ORDER BY sumquantity DESC ");) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01");
			SimpleDateFormat sdf31 = new SimpleDateFormat("yyyy-MM-31");
			pstmt.setString(1,sdf1.format(inputOrderdate));
			pstmt.setString(2,sdf31.format(inputOrderdate));
			try (

					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {

					String food = rs.getString("food");
					int foodprice = rs.getInt("foodprice");
					int sumprice = rs.getInt("sumprice");
					int sumquantity = rs.getInt("sumquantity");

					PopularOrder popularOrder = new PopularOrder(food, foodprice, sumprice, sumquantity);
					lists.add(popularOrder);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;
	}

	public ArrayList<Food> newDateRead() {

		ArrayList<Food> lists = new ArrayList<>();

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM foods JOIN food_prices ON foods.food_price_id=food_prices.food_price_id WHERE customer_id=? AND orderdate=(SELECT MAX(orderdate) FROM foods)");) {
			try (

					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {

					int food_id = rs.getInt("foods.food_id");
					int customer_id = rs.getInt("customer_id");
					String food = rs.getString("food");
					int foodprice = rs.getInt("foodprice");
					Date orderdate = rs.getTimestamp("orderdate");
					int quantity = rs.getInt("quantity");

					Food foods = new Food(food_id, customer_id, food, foodprice, orderdate, quantity);
					lists.add(foods);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;
	}

	public void update(int customer_id, int food_price_id, Date orderdate, int quantity) {

		try (
				PreparedStatement pstmt = connection.prepareStatement(
						"UPDATE foods SET customer_id=?,food_price_id=?,orderdate=?,quantity=? WHERE food_id=?");) {

			pstmt.setInt(1, customer_id);
			pstmt.setInt(2, food_price_id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(3, sdf.format(orderdate));
			pstmt.setInt(4, quantity);

			pstmt.executeUpdate();
			System.out.println("データの更新に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
