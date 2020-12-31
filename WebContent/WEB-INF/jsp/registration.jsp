<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	Customer customer = (Customer) request.getAttribute("customer");

//photoが未登録の場合もあるかもしれないので、一旦デフォルト写真入れる
String photo = "./img/sample.jpg";
if (!customer.getPhoto().equals("")) {
	photo = "./img/" + customer.getPhoto();
}

//携帯電話と固定電話が未登録で渡ってきた場合nullと表示されるため、””を入れる
String mobilephone = "";
if (customer.getMobilephone() != null)
	mobilephone = customer.getMobilephone();

String phone = "";
if (customer.getPhone() != null)
	phone = customer.getPhone();

ArrayList<Food> foodregistration = (ArrayList<Food>) request.getAttribute("foodregistration");
ArrayList<Drink> drinkregistration = (ArrayList<Drink>) request.getAttribute("drinkregistration");
ArrayList<Message> messagelist = (ArrayList<Message>) request.getAttribute("messagelist");
ArrayList<FoodPrice> foodlist = (ArrayList<FoodPrice>) request.getAttribute("foodlist");
ArrayList<DrinkPrice> drinklist = (ArrayList<DrinkPrice>) request.getAttribute("drinklist");
ArrayList<String> errormessage = (ArrayList<String>) request.getAttribute("errormessage");

//当日の日付を取得し、来店日とする
Date orderdate = (Date) request.getAttribute("orderdate");
SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
SimpleDateFormat sdfM = new SimpleDateFormat("MM");
SimpleDateFormat sdfD = new SimpleDateFormat("dd");
String orderdateY = sdfY.format(orderdate);
String orderdateM = sdfM.format(orderdate);
String orderdateD = sdfD.format(orderdate);

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String birthday = sdf.format(customer.getBirthday());

//過去来店された日を格納する
ArrayList<Date> visit_dates = (ArrayList<Date>) request.getAttribute("visit_dates");
if (visit_dates == null) {
	visit_dates = new ArrayList<Date>();
}

//後で過去来店された来店日表示に使う
SimpleDateFormat sdfjp = new SimpleDateFormat("yyyy年MM月dd日");

//過去注文されている金額と個数を元に合計金額を出し、表示出来るようにする
int totalprice = 0;
for (int i = 0; i < foodregistration.size(); i++) {
	totalprice += foodregistration.get(i).getFoodprice() * foodregistration.get(i).getQuantity();
}
for (int i = 0; i < drinkregistration.size(); i++) {
	totalprice += drinkregistration.get(i).getDrinkprice() * drinkregistration.get(i).getQuantity();
}
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">

<style>
style img {
	width: 80%;
	height: auto;
	border-radius: 20%;
	padding-top: 20px;
}

input {
	font-size: 16px;
	border: 1px solid #c1c1c1;
	border-radius: 5px;
	padding: 6px 12px;
}

input:focus {
	border-color: #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
	outline: 0;
}

select {
	font-size: 100%;
	border: 1px solid #c1c1c1;
	border-radius: 5px;
	padding: 9px 15px;
	margin-right: 10px;
}

option {
	font-size: 16px;
}

textarea {
	border: 1px solid #c1c1c1;
	border-radius: 5px;
	padding: 10px;
}

textarea:focus {
	border-color: #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
	outline: 0;
}

textarea:disabled {
	background-color: #ffffff;
}

#my_image {
	width: 400px;
	height: 400px;
	padding-right: 30px;
}

.large_block {
	width: 800px;
	min-width: 800px;
	height: auto;
	margin: 50px auto;
	background-color: #ffd1a3;
	border-radius: 15px;
}

.error {
	text-align: center;
	font-size: 24px;
	color: #ffffff;
}

.error span {
	background-color: #ff0000;
	padding: 5px;
}

.customer {
	width: 660px;
	padding-top: 30px;
	margin: 20px auto;
}

.privacy {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 20px;
}

.nameneed {
	text-indent: 1em;
	font-size: 12px;
	color: #ff0000;
	margin-left: 15px;
}

.privacy2 {
	display: flex;
	justify-content: flex-end;
	flex-direction: column;
}

.privacy2>label {
	margin: 10px;
}

.privacy4 {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
}

.privacy4>textarea {
	font-size: 16px;
	line-height: 1.4em;
	width: 100%;
	margin-bottom: 20px;
}

.visit {
	display: flex;
	flex-direction: column;
	justify-content: center;
	width: 598px;
	height: auto;
	padding: 30px;
	margin: 20px auto;
	border: solid 1px #c1c1c1;
	border-radius: 5px;
	background-color: #ffffff;
}

.visit>div {
	padding: 20px;
}

.visit td {
	padding: 5px 18px;
}

.visit>div>textarea {
	font-size: 16px;
	line-height: 1.4em;
	width: 100%;
}

.visit textarea:focus {
	border: 1px solid #ffa3d1;
	box-shadow: 4px 4px 4px #ffa3d1;
	outline: 0;
}

#totalprice:focus {
	border: 1px solid #ffa3d1;
	box-shadow: 4px 4px 4px #ffa3d1;
	outline: 0;
}

.menu input {
	width: 80px;
	height: 35px;
	cursor: pointer;
	border: 1px solid #ffa3d1;
	box-shadow: 4px 4px 4px #ffa3d1;
}

.menu input:active {
	box-shadow: none;
	position: relative;
	top: 4px;
}

.date {
	display: flex;
	justify-content: space-between;
}

.year input {
	width: 80px;
	height: 35px;
	cursor: pointer;
	border: 1px solid #ffa3d1;
	box-shadow: 4px 4px 4px #ffa3d1;
}

.year input:active {
	box-shadow: none;
	position: relative;
	top: 4px;
}

.registration {
	display: flex;
	justify-content: flex-end;
	width: 658px;
	padding: 30px;
	margin: 20px auto;
}

.registration>input {
	width: 150px;
	height: 50px;
	cursor: pointer;
	border: 1px solid #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
}

.registration>input:active {
	box-shadow: none;
	position: relative;
	top: 4px;
}

.underline {
	background: linear-gradient(transparent 70%, #ffa3d1 70%);
}
</style>

<script>
//foods配列にfoodsオブジェクトを格納する
        const foods = [
        	<%for (FoodPrice fp : foodlist) {
	out.println(
			"{ id: '" + fp.getFood_price_id() + "', food: '" + fp.getFood() + "', price: " + fp.getFoodprice() + " },");

}%>
        ];

      //追加注文された料理個数を格納する
        food_stocks = {};

      //dorinks配列にdorinksオブジェクトを格納する
        const drinks = [
        	<%for (DrinkPrice dp : drinklist) {
	out.println("{ id: '" + dp.getDrink_price_id() + "', drink: '" + dp.getDrink() + "', price: " + dp.getDrinkprice()
			+ " },");

}%>
        ];
      //追加注文されたドリンク個数を格納する
        drink_stocks = {};

      //変更ボタンが押されたらdisabled属性を解除する
        const func_bt_change =function () {
			this.removeEventListener('click',func_bt_change);
			const registration = document.getElementById('registration');
			registration.innerHTML = '<input type="submit" value="登　録">';

			let input_item = document.getElementById('all_area').getElementsByTagName('input');
            for( let i = 0; i < input_item.length; i++){
            	input_item[i].disabled = false;
            }
            let text_item = document.getElementById('all_area').getElementsByTagName('textarea');
            for( let i = 0; i < text_item.length; i++){
            	text_item[i].disabled = false;
            }
            let select_item = document.getElementById('all_area').getElementsByTagName('select');
            for( let i = 0; i < select_item.length; i++){
            	select_item[i].disabled = false;
            }

            document.getElementById('numbervisit').disabled = true;
            document.getElementById('totalprice').disabled = true;
            document.getElementById('numbervisit').style.backgroundColor = '#ffffff';

        }

        window.onload = function () {

        	//アップロードボタンが押された際にイベント発生（画像読み込み）
        	document.getElementById('image_file').addEventListener('change', function(e){
        		const file = document.getElementById('image_file').files[0];
        		let reader = new FileReader();
        		reader.addEventListener('load', function() {
        			document.getElementById('my_image').src = reader.result;
        		});
        		if(file) {
        			reader.readAsDataURL(file);
        		}
        	});

        	//合計金額の取得ができるように
        	const base_totalprice = <%=totalprice%>;
        	document.getElementById('totalprice').value = '\xA5' + base_totalprice.toLocaleString();
        	let food_totalprice = 0;
        	let drink_totalprice = 0;

        	//料理のプルダウンを作成
            const sel_food = document.getElementById('sel_food');

        	/*option要素を作って、
        	 *valueにfoods[i].id（DBのID）を設定する
        	 *innerHTMLでDBのドリンク名を書き出す
        	 *上記二つをselect要素に追加する
        	 *キー（[foods[i].id]）に値（0）を設定する
        	 */
            for (let i = 0; i < foods.length; i++) {
                const op = document.createElement('option');
                op.value = foods[i].id;
                op.innerHTML = foods[i].food;
                sel_food.appendChild(op);
                food_stocks[foods[i].id] = 0;
            }

          //追加ボタンを押された際にイベント発生
            //個数を追加しプルダウン下に表示する
            document.getElementById('bt_food').addEventListener('click', function () {

            	 /*select要素内の選ばれたもののインデックスを取得する
 				 *idxで取得したインデックスのoptionのvalueに設定された（DBのID)をfood_price_idに入れる
 				 *選ばれた判定として、food_stocks配列の[food_price_id]番号の値に1加算する
  				 */
                let idx = sel_food.selectedIndex;
                let food_price_id = sel_food.options[idx].value;
                food_stocks[food_price_id] += 1;
                const tf = document.getElementById("table_food");

                let e_tr = tf.getElementsByClassName('additional_food_tr');
                let e = [];
                for(let i = 0; i < e_tr.length; i++){
                	e.push(e_tr[i]);
                }

                e.forEach(v => v.parentNode.removeChild(v));

                let additional_totalprice = 0;
                foods.forEach(v => {
                    if (food_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
                        tr.classList.add('additional_food_tr');
                        let td = '<td>' + v.food + '</td>';
                        td += '<td>' + food_stocks[v.id] + '</td>';
                        td += '<td>' + food_stocks[v.id] * parseInt(v.price) + '<input type="hidden" name="food_price_id" value="' + v.id + '"><input type = "hidden" name = "food_quantity" value = "' + food_stocks[v.id] + '" ></td>';
                        tr.innerHTML = td;
                        tf.appendChild(tr);
                        additional_totalprice += food_stocks[v.id] * parseInt(v.price);
                    }
                });

              //料理を追加した時に合計金額が変動する
                food_totalprice = additional_totalprice;
                let total = base_totalprice + food_totalprice + drink_totalprice;
                document.getElementById('totalprice').value = '\xA5' + total.toLocaleString();
            });

          //ドリンクのプルダウン作成
            const sel_drink = document.getElementById('sel_drink');
            	/*option要素を作って、
            	 *valueにdrinks[i].id（DBのID）を設定する
            	 *innerHTMLでDBのドリンク名を書き出す
            	 *上記二つをselect要素に追加する
            	 *キー（[drinks[i].id]）に値（0）を設定する
            	 */
            for (let i = 0; i < drinks.length; i++) {
                const op = document.createElement('option');
                op.value = drinks[i].id;
                op.innerHTML = drinks[i].drink;
                sel_drink.appendChild(op);
                drink_stocks[drinks[i].id] = 0;

            }
            document.getElementById('bt_drink').addEventListener('click', function () {
				/*select要素内の選ばれたもののインデックスを取得する
				 *idxで取得したインデックスのoptionのvalueに設定された（DBのID)をdrink_price_idに入れる
				 *選ばれた判定として、drink_stocks配列の[drink_price_id]番号の値に1加算する
 				 */
                let idx = sel_drink.selectedIndex;
                let drink_price_id = sel_drink.options[idx].value;
                drink_stocks[drink_price_id] += 1;
                const tf = document.getElementById("table_drink");

                let e_tr = tf.getElementsByClassName('additional_drink_tr');
                let e = [];
                for(let i = 0; i < e_tr.length; i++){
                	e.push(e_tr[i]);
                }

                e.forEach(v => v.parentNode.removeChild(v));

                let additional_totalprice =0;
                drinks.forEach(v => {
                    if (drink_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
                        tr.classList.add('additional_drink_tr');
                        let td = '<td>' + v.drink + '</td>';
                        td += '<td>' + drink_stocks[v.id] + '</td>';
                        td += '<td>' + drink_stocks[v.id] * parseInt(v.price) + '<input type="hidden" name="drink_price_id" value="' + v.id + '"><input type = "hidden" name = "drink_quantity" value = "' + drink_stocks[v.id] + '" ></td>';
                        tr.innerHTML = td;
                        tf.appendChild(tr);
                        additional_totalprice += drink_stocks[v.id] * parseInt(v.price);
                    }
                });
              //ドリンクを追加すると合計金額が変動
                drink_totalprice = additional_totalprice;
                let total = base_totalprice + food_totalprice + drink_totalprice;
                document.getElementById('totalprice').value ='\xA5' + total.toLocaleString();

            });

            //過去来店された日を選択したらその日の情報を取得する
			const select_change_date = document.getElementById('select_change_date');
			document.getElementById('bt_change_date').addEventListener('click',function(){

   			 	let idx = select_change_date.selectedIndex;
   			 	let change_date = select_change_date.options[idx].value;
   			 	document.getElementById('hidden_change_date').value = change_date;
   			 	document.getElementById('form_change_date').submit();
			});

			//常にdisabledにする
            let input_item = document.getElementById('all_area').getElementsByTagName('input');
            for( let i = 0; i < input_item.length; i++){
            	input_item[i].disabled = true;
            }
            let text_item = document.getElementById('all_area').getElementsByTagName('textarea');
            for( let i = 0; i < text_item.length; i++){
            	text_item[i].disabled = true;
            }
            let select_item = document.getElementById('all_area').getElementsByTagName('select');
            for( let i = 0; i < select_item.length; i++){
            	select_item[i].disabled = true;
            }
            document.getElementById('bt_change').disabled = false;
            document.getElementById('select_change_date').disabled = false;
            document.getElementById('bt_change_date').disabled = false;
            document.getElementById('bt_change').addEventListener('click',func_bt_change);

        }

    </script>

<title>登録情報画面</title>

</head>
<body>

	<form method="post" class="large_block" id="all_area"
		action="./registration" enctype="multipart/form-data">
		<input type="hidden" name="action" value="update">
		<div class="customer">
			<div class="error">

				<%
					if (errormessage != null) {
				%>
				<c:forEach var="error" items="${errormessage}">
					<p>
						<span><c:out value="${error}" /></span>
					</p>
				</c:forEach>
				<%
					}
				%>
			</div>

			<div class="privacy">
				<div>

					<img id="my_image" src="<%=photo%>">
					 <input type="hidden" name="photo" value="<%=customer.getPhoto()%>">
					 <input type="file" id="image_file" accept="image/*" name="image_file">
				</div>

				<div class="privacy2">
					<input type="hidden" name="customer_id"
						value="<%=customer.getCustomers_id()%>"> <label for="name">名前<span
						class="nameneed">※入力必須項目</span></label><input type="text" name="name"
						id="name" placeholder="カタカナ" value="<%=customer.getName()%>">
					<label>携帯番号</label><input type="tel" name="mobilephone"
						pattern="[0-9]{11}" placeholder="11桁の数字" value="<%=mobilephone%>"><label>固定電話</label><input
						type="tel" name="phone" pattern="[0-9]{10}" placeholder="10桁の数字"
						value="<%=phone%>"><label>誕生日</label><input type="date"
						name="birthday" value="<%=birthday%>"><label>年齢</label><input
						type="number" name="age" placeholder="だいたいの年齢"
						value="<%=customer.getAge()%>"> <label>来店回数</label><input
						type="number" id="numbervisit"
						value="<%=customer.getNumbervisit()%>"> <input
						type="hidden" name="numbervisit"
						value="<%=customer.getNumbervisit()%>">
				</div>

			</div>

			<div class="privacy4">
				<label>好きなもの（料理・ドリンク等）</label>
				<textarea name="likefood" id="" cols="30" rows="4"><%=customer.getLikefood()%></textarea>

				<label>嫌いなもの（料理・ドリンク等）</label>
				<textarea name="hatefood" id="" cols="30" rows="4"><%=customer.getHatefood()%></textarea>
				<label>全般メモ（接客時の注意事項や共有すること）</label>
				<textarea name="memo" id="" cols="30" rows="4"><%=customer.getText()%></textarea>
			</div>
		</div>

		<div class="visit">

			<div class="date"><%=orderdateY%>年<%=orderdateM%>月<%=orderdateD%>日来店記録
				<input type="hidden" name="orderdate"
					value="<%=orderdateY%>-<%=orderdateM%>-<%=orderdateD%>">

				<div class="year">
					<select id="select_change_date">
						<%
							for (int i = 0; i < visit_dates.size(); i++) {
							if (i != visit_dates.size() - 1) {
								out.println("<option value='" + sdf.format(visit_dates.get(i)) + "'>" +
								sdfjp.format(visit_dates.get(i)) + "</option>");
							} else {
								out.println("<option value='" + sdf.format(visit_dates.get(i)) + "' selected>" +
								sdfjp.format(visit_dates.get(i)) + "</option>");
							}
						}
						%>
					</select> <input type="button" id="bt_change_date" value="表示">
				</div>
			</div>
			<div>
				<p>
					<span class="underline">注文料理</span>
				</p>
				<div class="menu">
					<select name="sel_food" id="sel_food">
					</select> <input type="button" id="bt_food" value="追加">
				</div>
				<table id="table_food">

				<%
					if (foodregistration != null) {
				%>
					<c:forEach var="food" items="${foodregistration}">
						<tr>
							<td><c:out value="${food.food}" /></td>
							<td><c:out value="${food.quantity}" /></td>
							<td><c:out value="${food.foodprice}" /></td>
						</tr>
					</c:forEach>
				<%
					}
				%>
				</table>
			</div>

			<div>
				<p>
					<span class="underline">注文ドリンク</span>
				</p>
				<div class="menu">
					<select name="sel_drink" id="sel_drink">
					</select> <input type="button" id="bt_drink" value="追加">
				</div>
				<table id="table_drink">

				<%
					if (drinkregistration != null) {
				%>
					<c:forEach var="drink" items="${drinkregistration}">
						<tr>
							<td><c:out value="${drink.drink}" /></td>
							<td><c:out value="${drink.quantity}" /></td>
							<td><c:out value="${drink.drinkprice}" /></td>
						</tr>
					</c:forEach>
				<%
					}
				%>
				</table>
			</div>

			<div>
				当日単価:<input type="text" id="totalprice">
			</div>

			<div>
				<label>本日の一言メモ</label>
				<%
					for (int i = 0; i < messagelist.size(); i++) {
				%>
				<textarea name="message" id="" cols="30" rows="4"><%=messagelist.get(i).getMessage()%></textarea>
				<input type="hidden" name="message_id"
					value="<%=messagelist.get(i).getMessage_id()%>">
				<%
					}
				%>
			</div>

		</div>

		<div class="registration" id="registration">
			<input type="button" id="bt_change" value="変　更">
		</div>
	</form>

	<form method="post" id="form_change_date" action="./registration">
		<input type="hidden" name="action" value="change_date">
		<input type="hidden" id="hidden_change_date" name="change_date" value="">
		<input type="hidden" name="customer_id"
			value="<%=customer.getCustomers_id()%>">
	</form>



</body>
</html>