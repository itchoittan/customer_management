package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert {


	private static Connection connection;

	public static void main(String[] args) {

		getConnection();
//		foodInsert("オムライス",890);
//		foodInsert("カレーライス",1000);
//		foodInsert("ハヤシライス",950);
//		drinkInsert("オレンジジュース",200);
//		drinkInsert("りんごジュース",200);
//		drinkInsert("カルピス",220);
//		drinkInsert("ウーロン茶",180);
//		foodInsert("ランチ",700);
//		foodInsert("うどん",400);
//		foodInsert("そば",500);
		drinkInsert("おいしいみず",100);
		drinkInsert("コーラ",200);
		drinkInsert("ミックスジュース",300);
		close();

	}

	public static void foodInsert(String food, int foodprice) {
		try (
				/*
				 * PreparedStatementを使ってSQLの準備
				 */
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO food_prices(food, foodprice) VALUES(?,?)");) {

				pstmt.setString(1, food);
				pstmt.setInt(2, foodprice);
				pstmt.executeUpdate();
				System.out.println("データの挿入に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void drinkInsert(String drink, int drinkprice) {
		try (
				/*
				 * PreparedStatementを使ってSQLの準備
				 */
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO drink_prices(drink, drinkprice) VALUES(?,?)");) {

				pstmt.setString(1, drink);
				pstmt.setInt(2, drinkprice);
				pstmt.executeUpdate();
				System.out.println("データの挿入に成功しました。");

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
