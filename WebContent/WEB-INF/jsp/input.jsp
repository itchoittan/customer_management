<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	ArrayList<FoodPrice> foodlist = (ArrayList<FoodPrice>) request.getAttribute("foodlist");
ArrayList<DrinkPrice> drinklist = (ArrayList<DrinkPrice>) request.getAttribute("drinklist");
Date orderdate = (Date) request.getAttribute("orderdate");
SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
SimpleDateFormat sdfM = new SimpleDateFormat("MM");
SimpleDateFormat sdfD = new SimpleDateFormat("dd");
String orderdateY = sdfY.format(orderdate);
String orderdateM = sdfM.format(orderdate);
String orderdateD = sdfD.format(orderdate);
ArrayList<String> errormessage = (ArrayList<String>) request.getAttribute("errormessage");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<style>
<
style>img {
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
	padding: 6px 12px;
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

span {
	background: linear-gradient(transparent 70%, #ffa3d1 70%);
}

.large_block {
	width: 800px;
	min-width: 800px;
	height: auto;
	margin: 50px auto;
	background-color: #ffd1a3;
	border-radius: 15px;
}

.customer {
	width: 75%;
	padding: 30px;
	margin: 20px auto;
}

#slideshow {
	position: relative;
	/* width: 960px;
            height: 400px;
            margin: 0 auto; */
	width: 80%;
	height: auto;
	padding-top: 20px;
}

#slideshow img {
	position: absolute;
	top: 0;
	left: 0;
	z-index: 8;
	opacity: 0.0;
}

#slideshow img.active {
	z-index: 10;
	opacity: 1.0;
}

#slideshow img.last-active {
	z-index: 9;
}

.privacy {
	display: flex;
	justify-content: flex-end;
	margin-bottom: 20px;
}

.privacy2 {
	display: flex;
	justify-content: flex-end;
	flex-direction: column;
	/* width: 500px; */
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
	width: 75%;
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

.visit>div>textarea {
	font-size: 12px;
	line-height: 1.4em;
	width: 100%;
}

.menu>input {
	width: 100px;
	height: 40px;
	cursor: pointer;
	font-size: 16px border: 1px solid #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
}

.menu>input:active {
	box-shadow: none;
	position: relative;
	top: 4px;
}

.registration {
	display: flex;
	justify-content: flex-end;
	width: 87%;
	padding: 30px;
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
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

        function slideSwitch() {
            let $active = $('#slideshow img.active');

            if ($active.length == 0) $active = $('#slideshow img:last');

            let $next = $active.next().length ? $active.next()
                : $('#slideshow img:first');

            $active.addClass('last-active');

            $next.css({ opacity: 0.0 })
                .addClass('active')
                .animate({ opacity: 1.0 }, 1000, function () {
                    $active.removeClass('active last-active');
                });

        }

        $(function () {
            setInterval("slideSwitch()", 3500);
        });





        const foods = [
        	<%for (FoodPrice fp : foodlist) {
	out.println(
			"{ id: '" + fp.getFood_price_id() + "', food: '" + fp.getFood() + "', price: " + fp.getFoodprice() + " },");

}%>
        ]
        food_stocks = [];

        const drinks = [
        	<%for (DrinkPrice dp : drinklist) {
	out.println("{ id: '" + dp.getDrink_price_id() + "', drink: '" + dp.getDrink() + "', price: " + dp.getDrinkprice()
			+ " },");

}%>
        ]
        drink_stocks = [];

        window.onload = function () {
            const sel_food = document.getElementById('sel_food');
            for (let i = 0; i < foods.length; i++) {
                const op = document.createElement('option');
                op.value = foods[i].id;
                op.innerHTML = foods[i].food;
                sel_food.appendChild(op);
                food_stocks[foods[i].id] = 0;

            }
            document.getElementById('bt_food').addEventListener('click', function () {

                let idx = sel_food.selectedIndex;
                let food_price_id = sel_food.options[idx].value;
                food_stocks[food_price_id] += 1;
                const tf = document.getElementById("table_food");
                tf.innerHTML = "";
                foods.forEach(v => {
                    if (food_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
                        let td = '<td>' + v.food + '</td>';
                        td += '<td>' + food_stocks[v.id] + '</td>';
                        td += '<td>' + food_stocks[v.id] * parseInt(v.price) + '<input type="hidden" name="food_price_id" value="' + v.id + '"><input type = "hidden" name = "food_quantity" value = "' + food_stocks[v.id] + '" ></td>';
                        tr.innerHTML = td;
                        tf.appendChild(tr);
                    }
                });
            });

            const sel_drink = document.getElementById('sel_drink');
            for (let i = 0; i < drinks.length; i++) {
                const op = document.createElement('option');
                op.value = drinks[i].id;
                op.innerHTML = drinks[i].drink;
                sel_drink.appendChild(op);
                drink_stocks[drinks[i].id] = 0;

            }
            document.getElementById('bt_drink').addEventListener('click', function () {

                let idx = sel_drink.selectedIndex;
                let drink_price_id = sel_drink.options[idx].value;
                drink_stocks[drink_price_id] += 1;
                const tf = document.getElementById("table_drink");
                tf.innerHTML = "";
                drinks.forEach(v => {
                    if (drink_stocks[v.id] !== 0) {
                        const tr = document.createElement('tr');
                        let td = '<td>' + v.drink + '</td>';
                        td += '<td>' + drink_stocks[v.id] + '</td>';
                        td += '<td>' + drink_stocks[v.id] * parseInt(v.price) + '<input type="hidden" name="drink_price_id" value="' + v.id + '"><input type = "hidden" name = "drink_quantity" value = "' + drink_stocks[v.id] + '" ></td>';
                        tr.innerHTML = td;
                        tf.appendChild(tr);
                    }
                });
            });
        }

    </script>
<title>入力画面</title>
</head>

<body>
	<form method="post" class="large_block">
		<div class="customer">
			<div class="privacy">

				<div id="slideshow">
					<img class="mainView active" src="pasta.jpg" alt="パスタ料理"
						title="パスタ料理"> <img class="mainView" src="kawara_soba.jpg">
					<img class="mainView" src="oyakodon.jpg">
					<!-- <label><input type="file" accept="image/*"></label> -->
				</div>

				<div class="privacy2">
					<label>名前<input type="text" name="name" placeholder="カタカナ"></label>
					<label>携帯番号<input type="tel" name="mobilephone"
						pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}" placeholder="09012345678"></label>
					<label>固定電話<input type="tel" name="phone"
						pattern="[0-9]{4}-[0-9]{2}-[0-9]{4}" placeholder="0666447777"></label>
					<label>誕生日<input type="date" name="birthday"></label> <label>年齢<input
						type="text" name="age" placeholder="だいたいの年齢"></label> <label>来店回数<input
						type="number" name="numbervisit"></label>
				</div>
				<!-- 画像は切り替わるよりスライドショーが良いかもしれない
名前か電話番号が登録されていないと登録ボタンを押せないようにする -->


			</div>

			<div class="privacy4">
				<label>好きなもの（料理・ドリンク等）</label>
				<textarea name="likefood" id="" cols="30" rows="4"></textarea>
				<!-- disabledを入れると部品を無効化
                    readonlyを入れると書き換えを禁止 -->
				<label>嫌いなもの（料理・ドリンク等）</label>
				<textarea name="hatefood" id="" cols="30" rows="4"></textarea>
				<label>○○様全般メモ（接客時の注意事項や共有すること）</label>
				<textarea name="memo" id="" cols="30" rows="4" disabled>ああああああ</textarea>
			</div>
		</div>

		<div class="visit">

			<p><%=orderdateY%>年<%=orderdateM%>月<%=orderdateD%>日来店記録
			</p>
			<input type="hidden" name="orderdate"
				value="<%=orderdateY%>-<%=orderdateM%>-<%=orderdateD%>">
			<div>
				<p>
					<span>注文料理</span>
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
					<span>注文ドリンク</span>
				</p>
				<div class="menu">
					<select name="sel_drink" id="sel_drink">
					</select> <input type="button" id="bt_drink" value="追加">
				</div>
				<table id="table_drink">
				</table>
			</div>

			<div>
				<label>当日単価:<input type="number"></label>
			</div>

			<div>
				<label>本日の一言メモ</label>
				<textarea name="message" id="" cols="30" rows="4"></textarea>
			</div>

		</div>
		<%
			if (errormessage != null) {
		%>
		<c:forEach var="error" items="${errormessage}">
		<p>
			<c:out value="${error}" />
		</p>
	</c:forEach>
		<%
			}
		%>
		<div class="registration">
			<input type="submit" value="登　録">
		</div>

	</form>

</body>
</html>