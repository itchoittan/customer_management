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
@WebServlet("/phone")
public class Phone extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Phone() {
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
		request.getRequestDispatcher("/WEB-INF/jsp/phone.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String error = "";

		String phone = request.getParameter("phone");
		Customer customer = null;

		ArrayList<Food> foodregistration = null;
		ArrayList<Drink> drinkregistration = null;
		ArrayList<Message> messagelist = null;
		ArrayList<FoodPrice> foodlist = null;
		ArrayList<DrinkPrice> drinklist = null;
		Date orderdate = null;

		try {
			DbAccess.getConnection();
			customer = new CustomersTable().mobilephoneRead(phone);

			if (customer == null) {
				customer = new CustomersTable().phoneRead(phone);
			}
			if (customer != null) {

				foodregistration = new FoodsTable().newDateRead(customer.getCustomers_id());
				drinkregistration = new DrinksTable().newDateRead(customer.getCustomers_id());
				messagelist = new MessagesTable().messageRead(customer.getCustomers_id());

				FoodPricesTable fpt = new FoodPricesTable();
				foodlist = fpt.allRead();

				DrinkPricesTable dpt = new DrinkPricesTable();
				drinklist = dpt.allRead();
				orderdate = messagelist.get(0).getOrderdate();

			} else {
				error = "入力された電話番号はデータにありませんでした。";
			}

		} catch (Exception e) {
			System.out.println("電話番号検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "電話番号検索時に想定外のエラーが出ましたので、システム管理者に相談してください:電話番号検索読み込みエラー";

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
			doGet(request, response);
		}
	}

}
