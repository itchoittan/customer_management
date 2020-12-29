package controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import bean.Customer;
import bean.Drink;
import bean.DrinkPrice;
import bean.Food;
import bean.FoodPrice;
import bean.Message;
import model.CustomersTable;
import model.DbAccess;
import model.DrinkPricesTable;
import model.DrinksTable;
import model.FoodPricesTable;
import model.FoodsTable;
import model.MessagesTable;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/registration")
@MultipartConfig(location = "/tmp")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registration() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.sendRedirect("./input");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		ArrayList<String> errors = new ArrayList<>();

		String action = request.getParameter("action");

		if (action.equals("change_date")) {
			changeDate(request, response, errors);
		} else {
			update(request, response, errors);
		}

		request.setAttribute("errormessage", errors);
		request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);
	}

	private void changeDate(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) {

		String str_customer_id = request.getParameter("customer_id");
		String change_date = request.getParameter("change_date");
		Customer customer = null;
		ArrayList<Food> foodregistration = null;
		ArrayList<Drink> drinkregistration = null;
		ArrayList<Message> messagelist = null;
		ArrayList<FoodPrice> foodlist = null;
		ArrayList<DrinkPrice> drinklist = null;
		Date orderdate = null;
		ArrayList<Date> visit_dates = new ArrayList<>();

		try {

			DbAccess.getConnection();
			int customer_id = Integer.parseInt(str_customer_id);
			customer = new CustomersTable().customerRead(customer_id);

			if (customer != null) {

				foodregistration = new FoodsTable().dateRead(customer_id, change_date);
				drinkregistration = new DrinksTable().dateRead(customer_id, change_date);
				messagelist = new MessagesTable().dateMessageRead(customer_id, change_date);

				FoodPricesTable fpt = new FoodPricesTable();
				foodlist = fpt.allRead();

				DrinkPricesTable dpt = new DrinkPricesTable();
				drinklist = dpt.allRead();

				visit_dates = new MessagesTable().getVisitDates(customer_id);

				if (messagelist.size() != 0) {
					orderdate = messagelist.get(0).getOrderdate();
				} else {
					errors.add("その日には来店されていません");
				}

			} else {
				System.out.println("顧客情報検索時にシステムエラーが出ました");
				errors.add("顧客情報検索時にエラーがでました。システム管理者に相談してください:customer_id読み込みエラー");
			}

		} catch (Exception e) {
			System.out.println("顧客情報検索時にシステムエラーが出ました");
			e.printStackTrace();
			errors.add("顧客情報検索時に想定外のエラーが出ましたので、システム管理者に相談してください:customer_id読み込みエラー");

		} finally {
			DbAccess.close();
		}
		if (errors.size() == 0) {
			request.setAttribute("customer", customer);
			request.setAttribute("foodregistration", foodregistration);
			request.setAttribute("drinkregistration", drinkregistration);
			request.setAttribute("messagelist", messagelist);
			request.setAttribute("orderdate", orderdate);
			request.setAttribute("foodlist", foodlist);
			request.setAttribute("drinklist", drinklist);
			request.setAttribute("visit_dates", visit_dates);

		}
	}

	private void update(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) {

		String str_customer_id = request.getParameter("customer_id");
		int customer_id = 0;
		try {
			customer_id = Integer.parseInt(str_customer_id);
		} catch (Exception e) {
			errors.add("想定外のエラーが発生しました。システム管理者に相談してください:customer_id取得エラー");
		}

		String post_photo = request.getParameter("photo");
		if(post_photo == null) {
			post_photo = "";
		}
		String photo = "";

		Part part;
		try {
			part = request.getPart("image_file");
		} catch (Exception e) {
			e.printStackTrace();
			part = null;
		}

		if (part != null) {
			if (!this.getFileName(part).equals("")) {
				photo = customer_id + "." + this.getFileName(part);
				post_photo = photo;
			}
		}

		String name = request.getParameter("name");
		String mobilephone = request.getParameter("mobilephone");
		if (mobilephone.equals("")) {
			mobilephone = null;
		}
		String phone = request.getParameter("phone");
		if (phone.equals("")) {
			phone = null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date birthday;
		try {
			birthday = sdf.parse(request.getParameter("birthday"));
		} catch (ParseException e) {
			birthday = null;
		}

		int age;
		try {
			age = Integer.parseInt(request.getParameter("age"));
			if (age < 0) {
				errors.add("年齢は正の整数を入力してください");
			}
		} catch (NumberFormatException e) {
			age = 0;
			errors.add("年齢は数字を入力してください");
		}

		String likefood = request.getParameter("likefood");
		String hatefood = request.getParameter("hatefood");
		String memo = request.getParameter("memo");
		String str_numbervisit = request.getParameter("numbervisit");
		int numbervisit = 1;
		try {
			numbervisit = Integer.parseInt(str_numbervisit);
		} catch (Exception e) {
			errors.add("システムエラー:numbervisit");
		}
		String[] str_food_price_ids = request.getParameterValues("food_price_id");
		String[] str_food_quantitys = request.getParameterValues("food_quantity");
		String[] str_drink_price_ids = request.getParameterValues("drink_price_id");
		String[] str_drink_quantitys = request.getParameterValues("drink_quantity");
		String[] messages = request.getParameterValues("message");
		String[] str_message_ids = request.getParameterValues("message_id");

		String str_orderdate = request.getParameter("orderdate");
		Date orderdate;
		try {
			orderdate = sdf.parse(str_orderdate);
		} catch (ParseException e) {
			orderdate = new Date();
		}

		if (name == null || name.equals("")) {
			errors.add("名前を入力してください");
		}

		int[] food_price_ids = null;
		if (str_food_price_ids != null) {
			food_price_ids = new int[str_food_price_ids.length];
			for (int i = 0; i < str_food_price_ids.length; i++) {
				try {
					food_price_ids[i] = Integer.parseInt(str_food_price_ids[i]);
				} catch (NumberFormatException e) {
					errors.add("システムエラー:food_price_id");
					break;
				}
			}
		}

		int[] food_quantitys = null;
		if (str_food_quantitys != null) {
			food_quantitys = new int[str_food_quantitys.length];
			for (int i = 0; i < str_food_quantitys.length; i++) {
				try {
					food_quantitys[i] = Integer.parseInt(str_food_quantitys[i]);
				} catch (NumberFormatException e) {
					errors.add("システムエラー:food_quantitys");
					break;
				}
			}
		}

		if (food_price_ids != null && food_quantitys != null) {
			if (food_price_ids.length != food_quantitys.length) {
				errors.add("システムエラー:food.lengthエラー");
			}
		}

		int[] drink_price_ids = null;
		if (str_drink_price_ids != null) {
			drink_price_ids = new int[str_drink_price_ids.length];
			for (int i = 0; i < str_drink_price_ids.length; i++) {
				try {
					drink_price_ids[i] = Integer.parseInt(str_drink_price_ids[i]);
				} catch (NumberFormatException e) {
					errors.add("システムエラー:drink_price_id");
					break;
				}
			}
		}

		int[] drink_quantitys = null;
		if (str_drink_quantitys != null) {
			drink_quantitys = new int[str_drink_quantitys.length];
			for (int i = 0; i < str_drink_quantitys.length; i++) {
				try {
					drink_quantitys[i] = Integer.parseInt(str_drink_quantitys[i]);
				} catch (NumberFormatException e) {
					errors.add("システムエラー:drink_quantitys");
					break;
				}
			}
		}

		if (drink_price_ids != null && drink_quantitys != null) {
			if (drink_price_ids.length != drink_quantitys.length) {
				errors.add("システムエラー:drink.lengthエラー");
			}
		}

		int[] message_ids = null;
		if (str_message_ids != null) {
			message_ids = new int[str_message_ids.length];
			for (int i = 0; i < str_message_ids.length; i++) {
				try {
					message_ids[i] = Integer.parseInt(str_message_ids[i]);
				} catch (NumberFormatException e) {
					errors.add("システムエラー:message_id");
					break;
				}
			}
		}

		if (messages != null && message_ids != null) {
			if (messages.length != message_ids.length) {
				errors.add("システムエラー:message.lengthエラー");
			}
		}

		try {
			DbAccess.getConnection();
			Customer customer = new CustomersTable().mobilephoneRead(mobilephone);
			if (customer != null && customer.getCustomers_id() != customer_id) {
				errors.add("入力された携帯番号は登録済です。");
			}
			customer = new CustomersTable().phoneRead(phone);
			if (customer != null && customer.getCustomers_id() != customer_id) {
				errors.add("入力された固定電話は登録済です。");
			}

		} catch (Exception e) {
			errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:電話番号読み込みエラー");

		} finally {
			DbAccess.close();
		}

		if (errors.size() == 0) {
			DbAccess.getConnection();
			try {
				if (photo != null && !photo.equals("")) {
					String uploadDir = getServletContext().getRealPath("/img") + File.separator;
					part.write(uploadDir + photo);
				}

				DbAccess.setAutoCommit();

				CustomersTable customersTable = new CustomersTable();
				customersTable.update(customer_id, name, mobilephone, phone, birthday, age,
						post_photo, likefood, hatefood, memo, numbervisit);

				if (food_price_ids != null) {
					FoodsTable foodsTable = new FoodsTable();
					for (int i = 0; i < food_price_ids.length; i++) {
						foodsTable.foodInsert(customer_id, food_price_ids[i], orderdate, food_quantitys[i]);
					}
				}

				if (drink_price_ids != null) {
					DrinksTable drinksTable = new DrinksTable();
					for (int i = 0; i < drink_price_ids.length; i++) {
						drinksTable.drinkInsert(customer_id, drink_price_ids[i], orderdate, drink_quantitys[i]);
					}
				}

				MessagesTable mt = new MessagesTable();
				for (int i = 0; i < messages.length; i++) {
					mt.update(message_ids[i], customer_id, messages[i], orderdate);
				}

				DbAccess.commit();

			} catch (Exception e) {
				try {
					DbAccess.rollback();
					System.out.println("Db更新時に想定外のエラーが出たため、ロールバックしました");
					e.printStackTrace();
					errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:Db更新エラー");
				} catch (SQLException e1) {

					System.out.println("rollbackでエラーが出ました");
					e1.printStackTrace();
					errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:Dbロールバックエラー");
				}
			}
			DbAccess.close();
		}

		for (int i = 0; i < errors.size(); i++) {

			System.out.println(errors.get(i));
		}

		try {

			DbAccess.getConnection();
			Customer customer = new CustomersTable().customerRead(customer_id);
			ArrayList<Food> foodregistration = new FoodsTable().dateRead(customer_id, str_orderdate);
			ArrayList<Drink> drinkregistration = new DrinksTable().dateRead(customer_id, str_orderdate);
			ArrayList<Message> messagelist = new MessagesTable().dateMessageRead(customer_id, str_orderdate);

			FoodPricesTable fpt = new FoodPricesTable();
			ArrayList<FoodPrice> foodlist = fpt.allRead();

			DrinkPricesTable dpt = new DrinkPricesTable();
			ArrayList<DrinkPrice> drinklist = dpt.allRead();

			ArrayList<Date> visit_dates = new MessagesTable().getVisitDates(customer_id);

			DbAccess.close();

			request.setAttribute("customer", customer);
			request.setAttribute("foodregistration", foodregistration);
			request.setAttribute("drinkregistration", drinkregistration);
			request.setAttribute("messagelist", messagelist);
			request.setAttribute("orderdate", orderdate);
			request.setAttribute("foodlist", foodlist);
			request.setAttribute("drinklist", drinklist);
			request.setAttribute("visit_dates", visit_dates);

		} catch (Exception e) {
			System.out.println("登録後のDBReadでエラーが出ました");
			e.printStackTrace();
			errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:Db読み込みエラー");

		}

	}

	private String getFileName(Part part) {
		String name = null;
		for (String dispotion : part.getHeader("Content-Disposition").split(";")) {
			if (dispotion.trim().startsWith("filename")) {
				name = dispotion.substring(dispotion.indexOf("=") + 1).replace("\"", "").trim();
				name = name.substring(name.lastIndexOf("\\") + 1);
				break;
			}
		}
		return name;
	}

}
