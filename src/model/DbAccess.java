package model;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class DbAccess {
	protected Connection connection;

	public DbAccess() {
		getConnection();
	}

	//DBとの接続を行う
	private void getConnection() {

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
	public void close() {
		if (this.connection != null) {
			try {

				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
