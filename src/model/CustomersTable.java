package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Customer;

public class CustomersTable extends DbAccess {

	public int customersInsert(String name, String mobilephone, String phone, Date birthday, int age,
			String photo, String like, String hate, String text, int numbervisit) {

		int autoIncrementKey = 0;

		try (

				PreparedStatement pstmt = connection.prepareStatement(
						"INSERT INTO customers(name, mobilephone, phone, birthday, age, photo, likefood, hatefood, memo, numbervisit) VALUES(?,?,?,?,?,?,?,?,?,?)");) {

			pstmt.setString(1, name);
			pstmt.setString(2, mobilephone);
			pstmt.setString(3, phone);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (birthday == null) {
				pstmt.setString(4, "0000-00-00");
			} else {
				pstmt.setString(4, sdf.format(birthday));
			}
			pstmt.setInt(5, age);
			pstmt.setString(6, photo);
			pstmt.setString(7, like);
			pstmt.setString(8, hate);
			pstmt.setString(9, text);
			pstmt.setInt(10, numbervisit);
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

	public Customer mobilephoneRead(String inputMobilephone) {

		if(inputMobilephone.equals("")) {
			return null;
		}

		Customer customer = null;

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM customers WHERE mobilephone=?");) {
			pstmt.setString(1, inputMobilephone);

			try (

					ResultSet rs = pstmt.executeQuery();) {

				if (rs.next()) {

					int customer_id = rs.getInt("customer_id");
					String name = rs.getString("name");
					String mobilephone = rs.getString("mobilephone");
					String phone = rs.getString("phone");
					Date birthday = rs.getTimestamp("birthday");
					int age = rs.getInt("age");
					String photo = rs.getString("photo");
					String like = rs.getString("like");
					String hate = rs.getString("hate");
					String text = rs.getString("text");
					int numbervisit = rs.getInt("numbervisit");

					customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
							photo, like, hate, text, numbervisit);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return customer;
	}

	public Customer phoneRead(String inputPhone) {

		if(inputPhone.equals("")) {
			return null;
		}

		Customer customer = null;

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM customers WHERE mobilephone=?");) {
			pstmt.setString(1, inputPhone);

			try (

					ResultSet rs = pstmt.executeQuery();) {

				if (rs.next()) {

					int customer_id = rs.getInt("customer_id");
					String name = rs.getString("name");
					String mobilephone = rs.getString("mobilephone");
					String phone = rs.getString("phone");
					Date birthday = rs.getTimestamp("birthday");
					int age = rs.getInt("age");
					String photo = rs.getString("photo");
					String like = rs.getString("like");
					String hate = rs.getString("hate");
					String text = rs.getString("text");
					int numbervisit = rs.getInt("numbervisit");

					customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
							photo, like, hate, text, numbervisit);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return customer;
	}

	public ArrayList<Customer> nameRead(String inputName) {

		ArrayList<Customer> lists = new ArrayList<>();

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM customers WHERE name=? ORDER BY numbervisit DESC");) {
			pstmt.setString(1, inputName);
			try (

					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {

					int customer_id = rs.getInt("customer_id");
					String name = rs.getString("name");
					String mobilephone = rs.getString("mobilephone");
					String phone = rs.getString("phone");
					Date birthday = rs.getTimestamp("birthday");
					int age = rs.getInt("age");
					String photo = rs.getString("photo");
					String like = rs.getString("like");
					String hate = rs.getString("hate");
					String text = rs.getString("text");
					int numbervisit = rs.getInt("numbervisit");

					Customer customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
							photo, like, hate, text, numbervisit);
					lists.add(customer);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;

	}

	public ArrayList<Customer> allRead() {

		ArrayList<Customer> lists = new ArrayList<>();

		try (

				PreparedStatement pstmt = connection
						.prepareStatement("SELECT * FROM customers ORDER BY numbervisit DESC");) {
			try (

					ResultSet rs = pstmt.executeQuery();) {

				while (rs.next()) {

					int customer_id = rs.getInt("customer_id");
					String name = rs.getString("name");
					String mobilephone = rs.getString("mobilephone");
					String phone = rs.getString("phone");
					Date birthday = rs.getTimestamp("birthday");
					int age = rs.getInt("age");
					String photo = rs.getString("photo");
					String like = rs.getString("like");
					String hate = rs.getString("hate");
					String text = rs.getString("text");
					int numbervisit = rs.getInt("numbervisit");

					Customer customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
							photo, like, hate, text, numbervisit);
					lists.add(customer);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return lists;

	}

	public void update(int customers_id, String name, String mobilephone, String phone, Date birthday, int age,
			String photo, String like, String hate, String text, int numbervisit) {

		try (
				PreparedStatement pstmt = connection.prepareStatement(
						"UPDATE customers SET name=?,mobilephone=?,phone=?,birthday=?,"
								+ "age=?,photo=?,like=?,hate=?,text=?,numbervisit=? WHERE customer_id=?");) {

			pstmt.setString(1, name);
			pstmt.setString(2, mobilephone);
			pstmt.setString(3, phone);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			pstmt.setString(4, sdf.format(birthday));
			pstmt.setInt(5, age);
			pstmt.setString(6, photo);
			pstmt.setString(7, like);
			pstmt.setString(8, hate);
			pstmt.setString(9, text);
			pstmt.setInt(10, numbervisit);
			pstmt.setInt(11, customers_id);
			pstmt.executeUpdate();
			System.out.println("データの更新に成功しました。");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
