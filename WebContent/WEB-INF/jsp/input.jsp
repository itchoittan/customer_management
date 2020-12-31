<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
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

int totalprice = 0;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<style>
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
	font-size: 16px;
	line-height: 1.4em;
	width: 100%;
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
	width: 330px;
	height: 330px;
	padding-top: 13px;
}

#totalprice:focus {
	border: 1px solid #ffa3d1;
	box-shadow: 4px 4px 4px #ffa3d1;
	outline: 0;
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
	margin-bottom: 20px;
}

.profile {
	display: flex;
	flex-direction: column;
}

.nameneed {
	text-indent: 1em;
	font-size: 12px;
	color: #ff0000;
	margin-left: 15px;
}

.profile label {
	margin: 10px;
}

.memo {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
}

.memo textarea {
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

visit p {
	margin-left: 20px;
}

.visit div {
	padding: 20px;
}

.visit td {
	padding: 5px 18px;
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

.registration {
	display: flex;
	justify-content: flex-end;
	width: 658px;
	padding: 30px;
	margin: 20px auto;
}

.registration input {
	width: 150px;
	height: 50px;
	cursor: pointer;
	border: 1px solid #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
}

.registration input:active {
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
				out.println("{ id: '" + dp.getDrink_price_id() + "', drink: '" + dp.getDrink() + "', price: "
						+ dp.getDrinkprice() + " },");

			}%>
        ];

       	//追加注文されたドリンク個数を格納する
        drink_stocks = {};

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

                let idx = sel_food.selectedIndex;
                let food_price_id = sel_food.options[idx].value;
                food_stocks[food_price_id] += 1;
                const tf = document.getElementById("table_food");
                tf.innerHTML = "";
                let additional_totalprice =0;
                foods.forEach(v => {
                    if (food_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
                        let td = '<td>' + v.food + '</td>';
                        td += '<td>' + food_stocks[v.id] + '</td>';
                        td += '<td>' + food_stocks[v.id] * parseInt(v.price) + '<input type="hidden" name="food_price_id" value="' + v.id + '"><input type = "hidden" name = "food_quantity" value = "' + food_stocks[v.id] + '" ></td>';
                        tr.innerHTML = td;
                        tf.appendChild(tr);
                        additional_totalprice +=food_stocks[v.id] * parseInt(v.price);
                    }
                });
                //料理を追加した時に合計金額が変動する
                food_totalprice = additional_totalprice;
                let total = base_totalprice + food_totalprice + drink_totalprice;
                document.getElementById('totalprice').value ='\xA5' + total.toLocaleString();
            });

            //ドリンクのプルダウン作成
            const sel_drink = document.getElementById('sel_drink');
            for (let i = 0; i < drinks.length; i++) {
                const op = document.createElement('option');
                op.value = drinks[i].id;
                op.innerHTML = drinks[i].drink;
                sel_drink.appendChild(op);
                drink_stocks[drinks[i].id] = 0;
            }

            //追加ボタンが押された際にイベント発生
            //個数を追加し、プルダウン下に表示する
            document.getElementById('bt_drink').addEventListener('click', function () {

                let idx = sel_drink.selectedIndex;
                let drink_price_id = sel_drink.options[idx].value;
                drink_stocks[drink_price_id] += 1;
                const tf = document.getElementById("table_drink");
                tf.innerHTML = "";
                let additional_totalprice =0;
                drinks.forEach(v => {
                    if (drink_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
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
        }

    </script>

<title>入力画面</title>
</head>

<body>
	<form method="post" class="large_block" enctype="multipart/form-data">
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

					<img id="my_image"> <input type="file" id="image_file"
						accept="image/*" name="image_file">
				</div>

				<div class="profile">
					<label>名前<span class="nameneed">※入力必須項目</span></label> <input
						type="text" name="name" placeholder="カタカナ"> <label>携帯番号</label>
					<input type="tel" name="mobilephone" pattern="[0-9]{11}"
						placeholder="11桁の数字"> <label>固定電話</label> <input
						type="tel" name="phone" pattern="[0-9]{10}" placeholder="10桁の数字">
					<label>誕生日</label> <input type="date" name="birthday"> <label>年齢</label>
					<input type="number" name="age" placeholder="だいたいの年齢">
				</div>

			</div>

			<div class="memo">
				<label>好きなもの（料理・ドリンク等）</label>
				<textarea name="likefood" id="" cols="30" rows="4"></textarea>
				<label>嫌いなもの（料理・ドリンク等）</label>
				<textarea name="hatefood" id="" cols="30" rows="4"></textarea>
				<label>全般メモ（接客時の注意事項や共有すること）</label>
				<textarea name="memo" id="" cols="30" rows="4"></textarea>
			</div>
		</div>

		<div class="visit">

			<p><%=orderdateY%>年<%=orderdateM%>月<%=orderdateD%>日来店記録
			</p>
			<input type="hidden" name="orderdate"
				value="<%=orderdateY%>-<%=orderdateM%>-<%=orderdateD%>">
			<div>
				<p>
					<span class="underline">注文料理</span>
				</p>
				<div class="menu">
					<select name="sel_food" id="sel_food">
					</select> <input type="button" id="bt_food" value="追加">
				</div>
				<table id="table_food">
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
				</table>
			</div>

			<div>
				当日単価:<input type="text" id="totalprice" disabled>
			</div>

			<div>
				<label>本日の一言メモ</label>
				<textarea name="message" id="" cols="30" rows="4"></textarea>
			</div>

		</div>

		<div class="registration">
			<input type="submit" value="登　録">
		</div>

	</form>

</body>
</html>