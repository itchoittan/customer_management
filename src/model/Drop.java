package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Drop {

	private static Connection connection;

	public static void main(String[] args) {

		getConnection();
		drop();
		close();

	}

	public static void drop() {
		try (
				PreparedStatement pstmt = connection.prepareStatement("DROP TABLE customers");) {

			int cnt = pstmt.executeUpdate();
			System.out.println("messagesテーブルの削除に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getConnection() {
		/*
		 * JDBCドライバのロード
		 * ※JDBC4.0対応のドライバでは、このステップは必要ありません
		 */
		//		try {
		//
		//			Class.forName("com.mysql.jdbc.Driver");
		//		} catch (ClassNotFoundException e) {
		//			e.printStackTrace();
		//		}

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
