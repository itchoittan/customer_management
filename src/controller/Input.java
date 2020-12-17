package controller;

import java.io.IOException;
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
import bean.DrinkPrice;
import bean.FoodPrice;
import model.CustomersTable;
import model.DrinkPricesTable;
import model.FoodPricesTable;

/**
 * Servlet implementation class Input
 */
@WebServlet("/input")
public class Input extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Input() {
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

		FoodPricesTable fpt = new FoodPricesTable();
		ArrayList<FoodPrice> foodlist = fpt.allRead();

		DrinkPricesTable dpt = new DrinkPricesTable();
		ArrayList<DrinkPrice> drinklist = dpt.allRead();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date orderdate = new Date();
		request.setAttribute("foodlist", foodlist);
		request.setAttribute("drinklist", drinklist);
		request.setAttribute("orderdate", orderdate);

		request.getRequestDispatcher("/WEB-INF/jsp/input.jsp").forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");

		ArrayList<String> errors = new ArrayList<>();

		String name = request.getParameter("name");
		String mobilephone = request.getParameter("mobilephone");
		String phone = request.getParameter("phone");
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

		Date orderdate;
		try {
			orderdate = sdf.parse(request.getParameter("orderdate"));
		} catch (ParseException e) {
			orderdate = new Date();
		}

		if (name == null || name.equals("")) {
			errors.add("名前を入力してください");
		}

		if (errors.size() == 0) {
			CustomersTable customersTable = new CustomersTable();
			Customer customer = customersTable.mobilephoneRead(mobilephone);
			if (customer == null) {
				customer = customersTable.phoneRead(phone);

			}
			if (customer == null) {
				customersTable.customersInsert(name, mobilephone, phone, birthday, age, photo, likefood, hatefood, memo,
						numbervisit);
			} else {
				customersTable.update(customer.getCustomers_id(), name, mobilephone, phone, birthday, age, photo,
						likefood, hatefood, memo, customer.getNumbervisit() + 1);
			}
			customersTable.close();
			//			MessagesTable
		}

		request.setAttribute("errormessage", errors);

		doGet(request, response);
	}

}
