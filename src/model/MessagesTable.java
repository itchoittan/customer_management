package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Message;

public class MessagesTable extends DbAccess {

	public int messageInsert(int customer_id, String message, Date orderdate) throws SQLException {

		int autoIncrementKey = 0;

		PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO messages(customer_id,message,orderdate) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);

		pstmt.setInt(1, customer_id);
		pstmt.setString(2, message);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pstmt.setString(3, sdf.format(orderdate));

		pstmt.executeUpdate();
		System.out.println("messageデータの挿入に成功しました。");

		// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
		ResultSet res = pstmt.getGeneratedKeys();

		if (res.next()) {
			autoIncrementKey = res.getInt(1);
		}

		res.close();

		pstmt.close();

		return autoIncrementKey;
	}

	public ArrayList<Message> messageRead(int inputCustomer_id) throws SQLException {

		ArrayList<Message> msges = new ArrayList<>();

		PreparedStatement pstmt = connection
				.prepareStatement("SELECT * FROM messages WHERE customer_id=? AND orderdate=(SELECT MAX(orderdate) FROM messages WHERE customer_id=?)");

		pstmt.setInt(1, inputCustomer_id);
		pstmt.setInt(2, inputCustomer_id);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			int message_id = rs.getInt("message_id");
			int customer_id = rs.getInt("customer_id");
			String message = rs.getString("message");
			Date orderdate = rs.getTimestamp("orderdate");

			Message msg = new Message(message_id, customer_id, message, orderdate);
			msges.add(msg);
		}
		rs.close();
		pstmt.close();

		return msges;
	}

	public void update(int message_id, int customer_id, String message, Date orderdate) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE messages SET customer_id=?,message=?,orderdate=? WHERE message_id=?");

		pstmt.setInt(1, customer_id);
		pstmt.setString(2, message);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		pstmt.setString(3, sdf.format(orderdate));
		pstmt.setInt(4, message_id);

		pstmt.executeUpdate();
		System.out.println("messageデータの更新に成功しました。");

		pstmt.close();

	}

	public void customerIdUpdate(int old_customer_id, int new_customer_id) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE messages SET customer_id=? WHERE customer_id=?");

		pstmt.setInt(1, new_customer_id);
		pstmt.setInt(2, old_customer_id);
		pstmt.executeUpdate();
		System.out.println("messageデータの更新に成功しました。");

		pstmt.close();
	}

}
