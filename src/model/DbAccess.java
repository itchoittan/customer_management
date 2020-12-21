package model;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class DbAccess {

	protected static Connection connection;

	//DBとの接続を行う
	public static void getConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		connection = null;

		DataSource ds = null;
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/customer_management");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		try {
			connection = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//DBのクローズを行う
	public static void close() {
		if (connection != null) {
			try {

				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setAutoCommit() throws SQLException {

		connection.setAutoCommit(false);

	}

	public static void commit() throws SQLException {

		connection.commit();

	}

	public static void rollback() throws SQLException {

		connection.rollback();

	}

}
