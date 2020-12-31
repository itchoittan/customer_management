package controller;

import java.io.IOException;
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
 * Servlet implementation class Phone
 */
@WebServlet("/namelist")
public class Namelist extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Namelist() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.sendRedirect("./name");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		ArrayList<Date> visit_dates = null;

		try {
			DbAccess.getConnection();
			int customer_id = Integer.parseInt(str_customer_id);
			customer = new CustomersTable().customerRead(customer_id);

			if (customer != null) {

				//データベースのmessagesテーブルには日付が一緒に登録されているので
				//最終書き込み日を必ず取得できるmessageテーブルから日付取得している
				messagelist = new MessagesTable().messageRead(customer_id);
				orderdate = messagelist.get(0).getOrderdate();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String str_orderdate = sdf.format(orderdate);

				//注文された料理・ドリンクのデータを取得
				foodregistration = new FoodsTable().dateRead(customer_id, str_orderdate);
				drinkregistration = new DrinksTable().dateRead(customer_id, str_orderdate);

				//料理のプルダウン表示に使用
				FoodPricesTable fpt = new FoodPricesTable();
				foodlist = fpt.allRead();

				//ドリンクのプルダウン表示に使用
				DrinkPricesTable dpt = new DrinkPricesTable();
				drinklist = dpt.allRead();

				orderdate = messagelist.get(0).getOrderdate();
				visit_dates = new MessagesTable().getVisitDates(customer_id);

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
			request.setAttribute("visit_dates", visit_dates);
			request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);
		} else {
			request.setAttribute("errormessage", error);
			request.getRequestDispatcher("/WEB-INF/jsp/name.jsp").forward(request, response);
		}
	}

}
