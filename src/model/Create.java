package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Create {

	private static Connection connection;

	public static void main(String[] args) {

		getConnection();
		create();
		close();

	}

	public static void create() {
		try (

				PreparedStatement pstmt1 = connection.prepareStatement(
						"CREATE TABLE customers(customer_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20), mobilephone VARCHAR(20) UNIQUE, phone VARCHAR(20) UNIQUE, birthday DATE, age INT, photo VARCHAR(250), likefood VARCHAR(250), hatefood VARCHAR(250), memo TEXT, numbervisit INT)");

				PreparedStatement pstmt2 = connection.prepareStatement(
						"CREATE TABLE foods(food_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, food_price_id INT, orderdate DATE, quantity INT)");

				PreparedStatement pstmt3 = connection.prepareStatement(
						"CREATE TABLE food_prices(food_price_id INT PRIMARY KEY AUTO_INCREMENT, food VARCHAR(250),foodprice INT)");

				PreparedStatement pstmt4 = connection.prepareStatement(
						"CREATE TABLE drinks(drink_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, drink_price_id INT, orderdate DATE, quantity INT)");

				PreparedStatement pstmt5 = connection.prepareStatement(
						"CREATE TABLE drink_prices(drink_price_id INT PRIMARY KEY AUTO_INCREMENT, drink VARCHAR(250),drinkprice INT)");

				PreparedStatement pstmt6 = connection.prepareStatement(
						"CREATE TABLE messages(message_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, message TEXT, orderdate DATE)");

				) {

			pstmt1.executeUpdate();
			System.out.println("顧客情報テーブルの作成が成功しました。");

			pstmt2.executeUpdate();
			System.out.println("料理テーブルの作成が成功しました。");

			pstmt3.executeUpdate();
			System.out.println("料理値段テーブルの作成が成功しました。");

			pstmt4.executeUpdate();
			System.out.println("飲み物テーブルの作成が成功しました。");

			pstmt5.executeUpdate();
			System.out.println("飲み物値段テーブルの作成が成功しました。");

			pstmt6.executeUpdate();
			System.out.println("本日の一言テーブルの作成が成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getConnection() {
		/*
		 * JDBCドライバのロード
		 * ※JDBC4.0対応のドライバでは、このステップは必要ありません
		 */
		try {

			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		connection = null;

		try {
			/*
			 *  DBMSとの接続。
			 *
			 *  JDBC URLは、環境に合わせて変更してください
			 */

			connection = DriverManager.getConnection("jdbc:mysql://localhost/customer_management", "root", "");

			/*
			 * SQLを実行してデータベースへのアクセス(検索など)を行なう
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//DBのクローズを行う
	public static void close() {
		if (connection != null) {
			try {
				/*
				 *  DBMSとの切断。
				 */
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
