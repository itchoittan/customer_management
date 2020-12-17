package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bean.Message;

public class MessagesTable extends DbAccess {

	public int messageInsert(int customer_id, String message, Date orderdate) {

		int autoIncrementKey = 0;

		try (

				PreparedStatement pstmt = connection.prepareStatement(
						"INSERT INTO messages(customer_id,message,orderdate) VALUES(?,?,?)");) {

			pstmt.setInt(1, customer_id);
			pstmt.setString(2, message);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(3, sdf.format(orderdate));

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


	public Message messageRead(int inputCustomer_id, Date inputOrderdate) {

		Message msg = null;

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM messages WHERE customer_id=? AND orderdate=?");) {
			pstmt.setInt(1, inputCustomer_id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(2,sdf.format(inputOrderdate));

			try (

					ResultSet rs = pstmt.executeQuery();) {

				if (rs.next()) {

					int message_id = rs.getInt("message_id");
					int customer_id = rs.getInt("customer_id");
					String message = rs.getString("message");
					Date orderdate = rs.getTimestamp("orderdate");

					msg = new Message(message_id, customer_id, message, orderdate);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return msg;
	}


	public void update(int message_id, int customer_id, String message, Date orderdate) {

		try (
				PreparedStatement pstmt = connection.prepareStatement(
						"UPDATE messages SET customer_id=?,message=?,orderdate=? WHERE message_id=?");) {

			pstmt.setInt(1, customer_id);
			pstmt.setString(2, message);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(3, sdf.format(orderdate));
			pstmt.setInt(4, message_id);

			pstmt.executeUpdate();
			System.out.println("データの更新に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
