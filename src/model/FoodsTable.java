package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bean.Food;
import bean.PopularOrder;

public class FoodsTable extends DbAccess {

	public int foodInsert(int customer_id, int food_price_id, Date orderdate, int quantity) throws SQLException {

		int autoIncrementKey = 0;

		PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO foods(customer_id,food_price_id,orderdate,quantity) VALUES(?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, customer_id);
		pstmt.setInt(2, food_price_id);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pstmt.setString(3, sdf.format(orderdate));
		pstmt.setInt(4, quantity);

		pstmt.executeUpdate();
		System.out.println("foodデータの挿入に成功しました。");

		// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
		ResultSet res = pstmt.getGeneratedKeys();

		if (res.next()) {
			autoIncrementKey = res.getInt(1);
		}

		res.close();

		pstmt.close();

		return autoIncrementKey;
	}

	//人気料理検索に使用
	public ArrayList<PopularOrder> popularRead(Date inputOrderdate) throws SQLException {

		ArrayList<PopularOrder> lists = new ArrayList<>();

		//何日から何日までと指定することで、1ヶ月分の料理注文回数を検索している
		//idでグループ分けし、注文回数（sumquantity）で降順した後、合計売上金額（sumprice）で降順している
		PreparedStatement pstmt = connection
				.prepareStatement(
						"SELECT food,foodprice,SUM(foodprice*quantity) AS sumprice, SUM(quantity) AS sumquantity "
								+ "FROM foods JOIN food_prices ON foods.food_price_id=food_prices.food_price_id "
								+ "WHERE orderdate>=? AND orderdate<=? "
								+ "GROUP BY foods.food_price_id "
								+ "ORDER BY sumquantity DESC ,sumprice DESC ");

		//カレンダークラスを利用し、その月が28日か29日か30日か31日かを調べてくれる
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(inputOrderdate.getYear(), inputOrderdate.getMonth(), 1);
		int day = c.getActualMaximum(Calendar.DATE);

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-01");
		SimpleDateFormat sdf31 = new SimpleDateFormat("yyyy-MM-" + day);
		pstmt.setString(1, sdf1.format(inputOrderdate));
		pstmt.setString(2, sdf31.format(inputOrderdate));

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			String food = rs.getString("food");
			int foodprice = rs.getInt("foodprice");
			int sumprice = rs.getInt("sumprice");
			int sumquantity = rs.getInt("sumquantity");

			PopularOrder popularOrder = new PopularOrder(food, foodprice, sumprice, sumquantity);
			lists.add(popularOrder);
		}
		rs.close();
		pstmt.close();

		return lists;
	}

	//指定日での注文個数・注文回数を抽出する
	public ArrayList<Food> dateRead(int inputCustomer_id, String date) throws SQLException {

		ArrayList<Food> lists = new ArrayList<>();

		PreparedStatement pstmt = connection
				.prepareStatement(
						"SELECT * FROM foods JOIN food_prices ON foods.food_price_id=food_prices.food_price_id WHERE customer_id=? AND orderdate=?");

		pstmt.setInt(1, inputCustomer_id);
		pstmt.setString(2, date);
		ResultSet rs = pstmt.executeQuery();
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
		rs.close();
		pstmt.close();

		return lists;
	}

	//現在未使用
	public ArrayList<Food> newDateRead(int inputCustomer_id) throws SQLException {

		ArrayList<Food> lists = new ArrayList<>();

		//指定された人の最終オーダー日での注文回数・注文個数の取得
		PreparedStatement pstmt = connection
				.prepareStatement(
						"SELECT * FROM foods JOIN food_prices ON foods.food_price_id=food_prices.food_price_id "
								+ "WHERE customer_id=? AND orderdate=(SELECT MAX(orderdate) "
								+ "FROM foods WHERE customer_id=?)");

		pstmt.setInt(1, inputCustomer_id);
		pstmt.setInt(2, inputCustomer_id);
		ResultSet rs = pstmt.executeQuery();
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
		rs.close();
		pstmt.close();

		return lists;
	}

	//注文された料理を登録する
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
			System.out.println("foodデータの更新に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	//今までのcustomer_idを新規のcustomer_idに変更する
	public void customerIdUpdate(int old_customer_id, int new_customer_id) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE foods SET customer_id=? WHERE customer_id=?");

		pstmt.setInt(1, new_customer_id);
		pstmt.setInt(2, old_customer_id);
		pstmt.executeUpdate();
		System.out.println("foodデータの更新に成功しました。");

		pstmt.close();
	}

}
