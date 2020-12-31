package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bean.Customer;

public class CustomersTable extends DbAccess {

	public int customersInsert(String name, String mobilephone, String phone, Date birthday, int age,
			String photo, String likefood, String hatefood, String memo, int numbervisit) throws SQLException {

		int autoIncrementKey = 0;

		PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO customers(name, mobilephone, phone, birthday, age, photo, likefood, hatefood, memo, numbervisit) VALUES(?,?,?,?,?,?,?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);

		pstmt.setString(1, name);
		pstmt.setString(2, mobilephone);
		pstmt.setString(3, phone);

		//誕生日が未入力の場合はデフォルトとして1800-01-01としている
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (birthday == null) {
			pstmt.setString(4, "1800-01-01");
		} else {
			pstmt.setString(4, sdf.format(birthday));
		}
		pstmt.setInt(5, age);
		pstmt.setString(6, photo);
		pstmt.setString(7, likefood);
		pstmt.setString(8, hatefood);
		pstmt.setString(9, memo);
		pstmt.setInt(10, numbervisit);
		pstmt.executeUpdate();
		System.out.println("customerデータの挿入に成功しました。");

		// getGeneratedKeys()により、Auto_IncrementされたIDを取得する
		ResultSet res = pstmt.getGeneratedKeys();

		if (res.next()) {
			autoIncrementKey = res.getInt(1);
		}

		res.close();
		pstmt.close();

		return autoIncrementKey;
	}

	//入力された携帯電話によって、データベースから検索する
	public Customer mobilephoneRead(String inputMobilephone) throws SQLException {

		if (inputMobilephone == null) {
			return null;
		}

		if (inputMobilephone.equals("")) {
			return null;
		}

		Customer customer = null;

		PreparedStatement pstmt = connection
				.prepareStatement("SELECT * FROM customers WHERE mobilephone=?");
		pstmt.setString(1, inputMobilephone);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {

			int customer_id = rs.getInt("customer_id");
			String name = rs.getString("name");
			String mobilephone = rs.getString("mobilephone");
			String phone = rs.getString("phone");
			Date birthday = rs.getTimestamp("birthday");
			int age = rs.getInt("age");
			String photo = rs.getString("photo");
			String like = rs.getString("likefood");
			String hate = rs.getString("hatefood");
			String text = rs.getString("memo");
			int numbervisit = rs.getInt("numbervisit");

			customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
					photo, like, hate, text, numbervisit);
		}

		rs.close();
		pstmt.close();

		return customer;

	}

	//入力された固定電話によって、データベースから検索する
	public Customer phoneRead(String inputPhone) throws SQLException {

		if (inputPhone == null) {
			return null;
		}

		if (inputPhone.equals("")) {
			return null;
		}

		Customer customer = null;

		PreparedStatement pstmt = connection
				.prepareStatement("SELECT * FROM customers WHERE phone=?");
		pstmt.setString(1, inputPhone);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {

			int customer_id = rs.getInt("customer_id");
			String name = rs.getString("name");
			String mobilephone = rs.getString("mobilephone");
			String phone = rs.getString("phone");
			Date birthday = rs.getTimestamp("birthday");
			int age = rs.getInt("age");
			String photo = rs.getString("photo");
			String like = rs.getString("likefood");
			String hate = rs.getString("hatefood");
			String text = rs.getString("memo");
			int numbervisit = rs.getInt("numbervisit");

			customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
					photo, like, hate, text, numbervisit);
		}

		rs.close();
		pstmt.close();

		return customer;
	}

	//入力された名前によって、データベースから検索する
	public ArrayList<Customer> nameRead(String inputName) throws SQLException {

		//名前は重複があるので、ArrayListを使用
		ArrayList<Customer> lists = new ArrayList<>();

		if (inputName == null || inputName.equals("")) {
			return lists;
		}

		PreparedStatement pstmt = connection
				.prepareStatement("SELECT * FROM customers WHERE name=? ORDER BY numbervisit DESC");
		pstmt.setString(1, inputName);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			int customer_id = rs.getInt("customer_id");
			String name = rs.getString("name");
			String mobilephone = rs.getString("mobilephone");
			String phone = rs.getString("phone");
			Date birthday = rs.getTimestamp("birthday");
			int age = rs.getInt("age");
			String photo = rs.getString("photo");
			String like = rs.getString("likefood");
			String hate = rs.getString("hatefood");
			String text = rs.getString("memo");
			int numbervisit = rs.getInt("numbervisit");

			Customer customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
					photo, like, hate, text, numbervisit);
			lists.add(customer);
		}
		rs.close();
		pstmt.close();

		return lists;

	}

	//来店回数一覧に使用
	public ArrayList<Customer> allRead() throws SQLException {

		ArrayList<Customer> lists = new ArrayList<>();

		//customersテーブルとmessageテーブルを結合し、messagesテーブルのcustomer_idによりグループ分けを行う
		//MAX(orderdate)により一番大きい日付を１件を取得している
		//それにより重複なしの状態を作り、numbervisitの降順に並べた後に、最終来店日の降順に並べている
		PreparedStatement pstmt = connection
				.prepareStatement("SELECT customers.customer_id,name,mobilephone,phone,birthday,age,photo,likefood,hatefood,memo,numbervisit,MAX(orderdate) AS max_orderdate "
						+ "FROM customers JOIN messages ON customers.customer_id=messages.customer_id "
						+ "GROUP BY messages.customer_id "
						+ "ORDER BY numbervisit DESC,max_orderdate DESC;");

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {

			int customer_id = rs.getInt("customers.customer_id");
			String name = rs.getString("name");
			String mobilephone = rs.getString("mobilephone");
			String phone = rs.getString("phone");
			Date birthday = rs.getTimestamp("birthday");
			int age = rs.getInt("age");
			String photo = rs.getString("photo");
			String like = rs.getString("likefood");
			String hate = rs.getString("hatefood");
			String text = rs.getString("memo");
			int numbervisit = rs.getInt("numbervisit");

			Customer customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
					photo, like, hate, text, numbervisit);
			lists.add(customer);
		}
		rs.close();
		pstmt.close();

		return lists;

	}

	//指定されたcustomer_idの情報を読み込んでいる
	public Customer customerRead(int inputCustomer_id) throws SQLException {

		Customer customer = null;

		PreparedStatement pstmt = connection
				.prepareStatement("SELECT * FROM customers WHERE customer_id=?");
		pstmt.setInt(1, inputCustomer_id);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {

			int customer_id = rs.getInt("customer_id");
			String name = rs.getString("name");
			String mobilephone = rs.getString("mobilephone");
			String phone = rs.getString("phone");
			Date birthday = rs.getTimestamp("birthday");
			int age = rs.getInt("age");
			String photo = rs.getString("photo");
			String like = rs.getString("likefood");
			String hate = rs.getString("hatefood");
			String text = rs.getString("memo");
			int numbervisit = rs.getInt("numbervisit");

			customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
					photo, like, hate, text, numbervisit);
		}

		rs.close();
		pstmt.close();

		return customer;

	}

	//指定されたcustomer_idの情報を上書きしている
	public void update(int customers_id, String name, String mobilephone, String phone, Date birthday, int age,
			String photo, String likefood, String hatefood, String memo, int numbervisit) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE customers SET name=?,mobilephone=?,phone=?,birthday=?,"
						+ "age=?,photo=?,likefood=?,hatefood=?,memo=?,numbervisit=? WHERE customer_id=?");

		pstmt.setString(1, name);
		pstmt.setString(2, mobilephone);
		pstmt.setString(3, phone);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (birthday == null) {
			pstmt.setString(4, "1800-01-01");
		} else {
			pstmt.setString(4, sdf.format(birthday));
		}
		pstmt.setInt(5, age);
		pstmt.setString(6, photo);
		pstmt.setString(7, likefood);
		pstmt.setString(8, hatefood);
		pstmt.setString(9, memo);
		pstmt.setInt(10, numbervisit);
		pstmt.setInt(11, customers_id);
		pstmt.executeUpdate();
		System.out.println("customerデータの更新に成功しました。");

		pstmt.close();
	}

	//photoのみの上書きを作る事で、データ結合時にcustomer_idによって変更できるようにしている
	public void photoUpdate(int customers_id, String photo) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE customers SET photo=? WHERE customer_id=?");

		pstmt.setString(1, photo);
		pstmt.setInt(2, customers_id);
		pstmt.executeUpdate();
		System.out.println("photoデータの更新に成功しました。");

		pstmt.close();
	}

	//入力された携帯番号と固定番号が以前登録されており、それぞれが違うcustomer_idの場合使用
	public void delete(int customer_id) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(
				"DELETE FROM customers WHERE customer_id=?");

		pstmt.setInt(1, customer_id);
		pstmt.executeUpdate();
		System.out.println("customer_idの削除に成功しました。");

		pstmt.close();
	}

}
