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
 * Servlet implementation class Input
 */
@WebServlet("/input")
@MultipartConfig(location = "/tmp")
public class Input extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Input() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		DbAccess.getConnection();
		FoodPricesTable fpt = new FoodPricesTable();
		ArrayList<FoodPrice> foodlist = fpt.allRead();

		DrinkPricesTable dpt = new DrinkPricesTable();
		ArrayList<DrinkPrice> drinklist = dpt.allRead();
		DbAccess.close();

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

		request.setCharacterEncoding("UTF-8");

		ArrayList<String> errors = new ArrayList<>();

		/*アップロードボタンを押してデータが入ってきた時は
		 * アップデートフラグをtrueにして後にデータの上書きを行うようにする
		 */
		Part part = request.getPart("image_file");
		String photo = this.getFileName(part);
		boolean photo_update_flg = true;
		if (photo == null || photo.equals("")) {
			photo_update_flg = false;
		}

		String name = request.getParameter("name");
		if (name == null) {
			name = "";
		}

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

		String likefood = request.getParameter("likefood");
		if (likefood == null) {
			likefood = "";
		}

		String hatefood = request.getParameter("hatefood");
		if (hatefood == null) {
			hatefood = "";
		}

		String memo = request.getParameter("memo");
		if (memo == null) {
			memo = "";
		}

		int numbervisit = 1;

		//選択された料理・ドリンクのIDと注文個数を複数収納できるように配列に入れる
		String[] str_food_price_ids = request.getParameterValues("food_price_id");
		String[] str_food_quantitys = request.getParameterValues("food_quantity");
		String[] str_drink_price_ids = request.getParameterValues("drink_price_id");
		String[] str_drink_quantitys = request.getParameterValues("drink_quantity");
		String message = request.getParameter("message");

		//入力した日が入る
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

		int customer_id = 0;
		if (errors.size() == 0) {
			DbAccess.getConnection();
			try {
				DbAccess.setAutoCommit();
				CustomersTable customersTable = new CustomersTable();

				//入力された携帯番号と固定電話番号をmobilephoneRead()、phoneRead()の引数に渡し、
				//データ内に同じ番号があるかどうか、データベースで検索している
				Customer customer1 = customersTable.mobilephoneRead(mobilephone);
				Customer customer2 = customersTable.phoneRead(phone);

				if (customer1 == null && customer2 == null) {
					//入力された番号が両方ともデータベースになかった場合
					customer_id = customersTable.customersInsert(name, mobilephone, phone, birthday, age, photo,
							likefood,
							hatefood, memo, numbervisit);

				} else if (customer1 != null && customer2 == null) {
					//携帯電話で引き込めて、固定電話で引き込めないとき
					//未入力の部分があれば携帯電話で引き込んだ情報と結合する
					if (phone == null) {
						phone = customer1.getPhone();
					}

					if (birthday == null) {
						birthday = customer1.getBirthday();
					}

					if (age == 0) {
						age = customer1.getAge();
					}

					if (photo == null || photo.equals("")) {
						photo_update_flg = false;
						photo = customer1.getPhoto();
					}

					likefood = customer1.getLikefood() + likefood;
					hatefood = customer1.getHatefood() + hatefood;
					memo = customer1.getText() + memo;

					//最初に登録してある情報（customer_id)に上書きする
					customersTable.update(customer1.getCustomers_id(), name, mobilephone, phone, birthday, age, photo,
							likefood, hatefood, memo, customer1.getNumbervisit() + 1);
					customer_id = customer1.getCustomers_id();

				} else if (customer1 == null && customer2 != null) {
					//固定電話で引き込めて、携帯電話で引き込めないとき
					//未入力の部分があれば携帯電話で引き込んだ情報と結合する
					if (mobilephone == null) {
						mobilephone = customer2.getMobilephone();
					}

					if (birthday == null) {
						birthday = customer2.getBirthday();
					}

					if (age == 0) {
						age = customer2.getAge();
					}

					if (photo == null || photo.equals("")) {
						photo_update_flg = false;
						photo = customer2.getPhoto();
					}

					likefood = customer2.getLikefood() + likefood;
					hatefood = customer2.getHatefood() + hatefood;
					memo = customer2.getText() + memo;

					//最初に登録してある情報（customer_id)に上書きする
					customersTable.update(customer2.getCustomers_id(), name, mobilephone, phone, birthday, age, photo,
							likefood, hatefood, memo, customer2.getNumbervisit() + 1);
					customer_id = customer2.getCustomers_id();

				} else if (customer1 != null && customer2 != null) {
					//携帯電話と固定電話の両方で引き込めたとき
					if (customer1.getCustomers_id() == customer2.getCustomers_id()) {
						//両方の番号が登録してあるデータと一緒の場合
						if (birthday == null) {
							birthday = customer1.getBirthday();
						}

						if (age == 0) {
							age = customer1.getAge();
						}

						if (photo == null || photo.equals("")) {
							photo_update_flg = false;
							photo = customer1.getPhoto();
						}

						likefood = customer1.getLikefood() + likefood;
						hatefood = customer1.getHatefood() + hatefood;
						memo = customer1.getText() + memo;

						customersTable.update(customer1.getCustomers_id(), name, mobilephone, phone, birthday, age,
								photo,
								likefood, hatefood, memo, customer1.getNumbervisit() + 1);
						customer_id = customer1.getCustomers_id();

					} else {
						//携帯電話と固定電話の両方で引き込めたときで過去登録してあるcustomerIDと違うとき
						//過去のcustomer_idを2つ削除し、新規customer_id作成パターン
						//customer1（携帯電話）を優先して上書き処理を行っている
						if (birthday == null) {
							birthday = customer1.getBirthday();
							if (birthday.equals(new Date(1800, 01, 01))) {
								birthday = customer2.getBirthday();
								//未入力、なおかつ携帯電話側の情報にも未登録(1800,01,01)の場合、固定電話側の情報を登録する
							}
						}

						if (age == 0) {
							age = customer1.getAge();
							if (age == 0) {
								age = customer2.getAge();
								//未入力、なおかつ携帯電話側の情報にも未登録(0)の場合、固定電話側の情報を登録する
							}
						}

						if (photo == null || photo.equals("")) {
							photo_update_flg = false;
							photo = customer1.getPhoto();
							if (photo.equals("")) {
								photo = customer2.getPhoto();
								//photo_update_flgをfalseにすることでphotoデータの上書きを防ぐ
								//未入力、なおかつ携帯電話側の情報にも未登録の場合、固定電話側の情報を登録する
							}
						}

						likefood = customer1.getLikefood() + customer2.getLikefood() + likefood;
						hatefood = customer1.getHatefood() + customer2.getHatefood() + hatefood;
						memo = customer1.getText() + customer2.getText() + memo;
						numbervisit = customer1.getNumbervisit() + customer2.getNumbervisit() + 1;

						//携帯電話側の情報と固定電話側の情報をまとめた後に二つの情報を削除する
						customersTable.delete(customer1.getCustomers_id());
						customersTable.delete(customer2.getCustomers_id());

						customer_id = customersTable.customersInsert(name, mobilephone, phone, birthday, age, photo,
								likefood, hatefood, memo, numbervisit);

						//今までのcustomer_idを新規のcustomer_idに変更する
						FoodsTable ft = new FoodsTable();
						ft.customerIdUpdate(customer1.getCustomers_id(), customer_id);
						ft.customerIdUpdate(customer2.getCustomers_id(), customer_id);
						DrinksTable dt = new DrinksTable();
						dt.customerIdUpdate(customer1.getCustomers_id(), customer_id);
						dt.customerIdUpdate(customer2.getCustomers_id(), customer_id);
						MessagesTable mt = new MessagesTable();
						mt.customerIdUpdate(customer1.getCustomers_id(), customer_id);
						mt.customerIdUpdate(customer2.getCustomers_id(), customer_id);
					}
				}

				if (customer_id != 0) {
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

					MessagesTable messagesTable = new MessagesTable();
					messagesTable.messageInsert(customer_id, message, orderdate);

					/*画像をアップロードされた時、フラグ判定により実行
					 *photoの名前の前にcustomer_idを付け、その後上書きを行う。
					 *customer_idを付けることでphotoの名前の重複を防ぐ
					 */
					if (photo_update_flg) {
						photo = customer_id + "." + photo;
						customersTable.photoUpdate(customer_id, photo);
					}
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

		if (errors.size() == 0 && customer_id != 0) {
			try {
				//画像がアップデートされたらフラグ判定によりimgフォルダに先ほどの名前（customer_id + "." + photo）で保存される
				if (photo_update_flg) {
					String uploadDir = getServletContext().getRealPath("/img") + File.separator;
					part.write(uploadDir + photo);
				}

				DbAccess.getConnection();
				Customer customer = new CustomersTable().customerRead(customer_id);
				ArrayList<Message> messagelist = new MessagesTable().messageRead(customer_id);
				orderdate = messagelist.get(0).getOrderdate();
				String str_orderdate = sdf.format(orderdate);
				ArrayList<Food> foodregistration = new FoodsTable().dateRead(customer_id, str_orderdate);
				ArrayList<Drink> drinkregistration = new DrinksTable().dateRead(customer_id, str_orderdate);

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
				request.getRequestDispatcher("/WEB-INF/jsp/registration.jsp").forward(request, response);

			} catch (Exception e) {
				System.out.println("登録後のDBReadでエラーが出ました");
				e.printStackTrace();
				errors.add("登録時に想定外のエラーが出ましたので、システム管理者に相談してください:Db読み込みエラー");

				request.setAttribute("errormessage", errors);
				doGet(request, response);
			}
		} else {
			request.setAttribute("errormessage", errors);
			doGet(request, response);

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
