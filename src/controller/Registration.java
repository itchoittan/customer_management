package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		String str_customer_id = request.getParameter("customer_id");
		int customer_id = 0;
		try {
			customer_id = Integer.parseInt(str_customer_id);
		} catch (Exception e) {
			errors.add("想定外のエラーが発生しました。システム管理者に相談してください:customer_id取得エラー");
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
		} catch (NumberFormatException e) {
			age = 0;
		}

		String photo = request.getParameter("photo");
		String likefood = request.getParameter("likefood");
		String hatefood = request.getParameter("hatefood");
		String memo = request.getParameter("memo");
		int numbervisit = 1;
		String[] str_food_price_ids = request.getParameterValues("food_price_id");
		String[] str_food_quantitys = request.getParameterValues("food_quantity");
		String[] str_drink_price_ids = request.getParameterValues("drink_price_id");
		String[] str_drink_quantitys = request.getParameterValues("drink_quantity");
		String[] messages = request.getParameterValues("message");
		String[] str_message_ids = request.getParameterValues("message");

		Date orderdate;
		try {
			orderdate = sdf.parse(request.getParameter("orderdate"));
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

		if (errors.size() == 0) {
			DbAccess.getConnection();
			try {
				DbAccess.setAutoCommit();

				CustomersTable customersTable = new CustomersTable();
				customersTable.update(customer_id, name, mobilephone, phone, birthday, age,
						photo, likefood, hatefood, memo, numbervisit);

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

		for(int i = 0; i < errors.size(); i++) {

			System.out.println(errors.get(i));
		}
		//		Customer customer = new Customer(customer_id, name, mobilephone, phone, birthday, age,
		//				photo, likefood, hatefood, memo, numbervisit);

		//		ArrayList<Food> foodregistration = null;
		//		ArrayList<Drink> drinkregistration = null;
		//		ArrayList<Message> messagelist = null;
		//		if (errors.size() == 0) {
		try {

			DbAccess.getConnection();
			Customer customer = new CustomersTable().customerRead(customer_id);
			ArrayList<Food> foodregistration = new FoodsTable().newDateRead(customer_id);
			ArrayList<Drink> drinkregistration = new DrinksTable().newDateRead(customer_id);
			ArrayList<Message> messagelist = new MessagesTable().messageRead(customer_id);

			FoodPricesTable fpt = new FoodPricesTable();
			ArrayList<FoodPrice> foodlist = fpt.allRead();

			DrinkPricesTable dpt = new DrinkPricesTable();
			ArrayList<DrinkPrice> drinklist = dpt.allRead();

			DbAccess.close();

			request.setAttribute("customer", customer);
			request.setAttribute("foodregistration", foodregistration);
			request.setAttribute("drinkregistration", drinkregistration);
			request.setAttribute("messagelist", messagelist);
			request.setAttribute("orderdate", orderdate);
			request.setAttribute("foodlist", foodlist);
			request.setAttribute("drinklist", drinklist);
			request.setAttribute("errormessage", errors);;
			request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

		} catch (Exception e) {
			System.out.println("登録後のDBReadでエラーが出ました");
			e.printStackTrace();
			errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:Db読み込みエラー");
			request.setAttribute("errormessage", errors);

			request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

		}
		//		} else {
		//
		//			request.setAttribute("errormessage", errors);
		//
		//
		//			doGet(request, response);
		//
		//		}

	}

}
