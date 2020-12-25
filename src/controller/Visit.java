package controller;

import java.io.IOException;
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
 * Servlet implementation class Phone
 */
@WebServlet("/visit")
public class Visit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Visit() {
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

		String error = "";

		ArrayList<Customer> customers = null;
		ArrayList<Date> orderdates = new ArrayList<>();

		try {
			DbAccess.getConnection();
			customers = new CustomersTable().allRead();

			if (customers.size() != 0) {

				for (int i = 0; i < customers.size(); i++) {
					ArrayList<Message>messagelist = new MessagesTable()
							.messageRead(customers.get(i).getCustomers_id());

					orderdates.add(messagelist.get(0).getOrderdate());
				}
			} else {
				error = "まだ顧客情報はデータにありませんでした。";
			}

		} catch (Exception e) {
			System.out.println("顧客情報検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "顧客情報検索時に想定外のエラーが出ましたので、システム管理者に相談してください:顧客情報検索読み込みエラー";

		} finally {
			DbAccess.close();
		}
		if (error.equals("")) {
			request.setAttribute("customers", customers);
			request.setAttribute("orderdates", orderdates);

			request.getRequestDispatcher("/WEB-INF/jsp/visit.jsp").forward(request, response);

		} else {
			request.setAttribute("errormessage", error);
			request.getRequestDispatcher("/WEB-INF/jsp/visit.jsp").forward(request, response);

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String error = "";

		String str_customer_id = request.getParameter("customer_id");
		Customer customer = null;

		ArrayList<Food> foodregistration = null;
		ArrayList<Drink> drinkregistration = null;
		ArrayList<Message> messagelist = null;
		ArrayList<FoodPrice> foodlist = null;
		ArrayList<DrinkPrice> drinklist = null;
		Date orderdate = null;

		try {

			DbAccess.getConnection();
			int customer_id = Integer.parseInt(str_customer_id);
			customer = new CustomersTable().customerRead(customer_id);

			if (customer != null) {

				foodregistration = new FoodsTable().newDateRead(customer_id);
				drinkregistration = new DrinksTable().newDateRead(customer_id);
				messagelist = new MessagesTable().messageRead(customer_id);

				FoodPricesTable fpt = new FoodPricesTable();
				foodlist = fpt.allRead();

				DrinkPricesTable dpt = new DrinkPricesTable();
				drinklist = dpt.allRead();
				orderdate = messagelist.get(0).getOrderdate();

			} else {
				System.out.println("顧客情報検索時にシステムエラーが出ました");
				error = "顧客情報検索時にエラーがでました。システム管理者に相談してください:customer_id読み込みエラー";
			}

		} catch (Exception e) {
			System.out.println("顧客情報検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "顧客情報検索時に想定外のエラーが出ましたので、システム管理者に相談してください:customer_id読み込みエラー";


		} finally {
			DbAccess.close();
		}
		if (error.equals("")) {
			request.setAttribute("customer", customer);
			request.setAttribute("foodregistration", foodregistration);
			request.setAttribute("drinkregistration", drinkregistration);
			request.setAttribute("messagelist", messagelist);
			request.setAttribute("orderdate", orderdate);
			request.setAttribute("foodlist", foodlist);
			request.setAttribute("drinklist", drinklist);
			request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

		} else {
			request.setAttribute("errormessage", error);
			request.getRequestDispatcher("/WEB-INF/jsp/visit.jsp").forward(request, response);
		}

	}

}
