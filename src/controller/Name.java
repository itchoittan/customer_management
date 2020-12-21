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
import bean.Message;
import model.CustomersTable;
import model.DbAccess;
import model.MessagesTable;

/**
 * Servlet implementation class Phone
 */
@WebServlet("/name")
public class Name extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Name() {
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
		request.getRequestDispatcher("/WEB-INF/jsp/name.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String error = "";

		String name = request.getParameter("name");
		ArrayList<Customer> customers = null;
		ArrayList<Date> orderdates = new ArrayList<>();


		try {
			DbAccess.getConnection();
			customers = new CustomersTable().nameRead(name);

			if (customers.size() != 0) {

				for (int i = 0; i < customers.size(); i++) {
					ArrayList<Message>  messagelist = new MessagesTable()
							.messageRead(customers.get(i).getCustomers_id());

					orderdates.add(messagelist.get(0).getOrderdate());
				}

			} else {
				error = "入力された名前はデータにありませんでした。";
			}

		} catch (Exception e) {
			System.out.println("名前検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "名前検索時に想定外のエラーが出ましたので、システム管理者に相談してください:名前検索読み込みエラー";

		} finally {
			DbAccess.close();
		}

		if (error.equals("")) {

			request.setAttribute("customers", customers);
			request.setAttribute("orderdates", orderdates);

			request.getRequestDispatcher("/WEB-INF/jsp/namelist.jsp").forward(request, response);
		} else {

			request.setAttribute("errormessage", error);
			doGet(request, response);
		}
	}

}
