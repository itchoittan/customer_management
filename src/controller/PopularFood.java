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

import bean.PopularOrder;
import model.DbAccess;
import model.FoodsTable;

/**
 * Servlet implementation class Phone
 */
@WebServlet("/popularfood")
public class PopularFood extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PopularFood() {
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

		ArrayList<PopularOrder> popularOrder = null;
		Date orderdate = new Date();

		try {
			DbAccess.getConnection();
			popularOrder = new FoodsTable().popularRead(orderdate);

			if (popularOrder.size() == 0) {
				error = "まだ注文された料理はデータにありませんでした。";
			}

		} catch (Exception e) {
			System.out.println("人気料理検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "人気料理検索時に想定外のエラーが出ましたので、システム管理者に相談してください:人気料理検索読み込みエラー";

		} finally {
			DbAccess.close();
		}
		if (error.equals("")) {
			request.setAttribute("popularOrder", popularOrder);

			request.getRequestDispatcher("/WEB-INF/jsp/popularfood.jsp").forward(request, response);

		} else {
			request.setAttribute("errormessage", error);
			request.getRequestDispatcher("/WEB-INF/jsp/popularfood.jsp").forward(request, response);

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String year_date = request.getParameter("year");
		String month_date = request.getParameter("month");
		System.out.println(year_date);
		System.out.println(month_date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

		String error = "";

		ArrayList<PopularOrder> popularOrder = null;
//		Date orderdate = new Date();

		try {
			Date change_date = sdf.parse(year_date + "/" +  month_date);
			DbAccess.getConnection();
			popularOrder = new FoodsTable().popularRead(change_date);

			if (popularOrder.size() == 0) {
				error = "選択された年月に、注文された料理はデータにありませんでした。";
			}

		} catch (Exception e) {
			System.out.println("人気料理検索時にシステムエラーが出ました");
			e.printStackTrace();
			error = "人気料理検索時に想定外のエラーが出ましたので、システム管理者に相談してください:人気料理検索読み込みエラー";

		} finally {
			DbAccess.close();
		}
		if (error.equals("")) {
			request.setAttribute("popularOrder", popularOrder);

			request.getRequestDispatcher("/WEB-INF/jsp/popularfood.jsp").forward(request, response);

		} else {
			request.setAttribute("errormessage", error);
			request.getRequestDispatcher("/WEB-INF/jsp/popularfood.jsp").forward(request, response);

		}

	}

}
