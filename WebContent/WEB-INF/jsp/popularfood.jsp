<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
	ArrayList<PopularOrder> popularfoods = (ArrayList<PopularOrder>) request.getAttribute("popularOrder");

String errormessage = (String) request.getAttribute("errormessage");
if (popularfoods == null) {
	popularfoods = new ArrayList<PopularOrder>();
}

if (errormessage == null) {
	errormessage = "";
}
%>


<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">

<style>

.large_block {
	width: 800px;
	min-width: 800px;
	height: auto;
	padding: 20px;
	margin: 50px auto;
	border-radius: 15px;
	background-color: #ffd1a3;
}

table {
	width: 780px;
	margin: 10px auto;
	border-spacing: 0px 8px;
	border-collapse: separate;
	background-color: #ffd1a3;
	overflow: hidden;
}

tr {
	height: 30px;
	border: solid 1px #c1c1c1;
	background-color: #ffffff;
}

th {
	width: 25%;
	padding: 15px;
	background-color: #ffd1a3;
}

td {
	width: 25%;
	height: 100%;
	padding: 10px;
	text-align: center;
	border-top: solid 1px #c1c1c1;
	border-bottom: solid 1px #c1c1c1;
}

td:first-child {
	border-radius: 5px 0 0 5px;
	border-left: solid 1px #c1c1c1;
	border-right: transparent;
}

td:last-child {
	border-radius: 0 5px 5px 0;
	border-right: solid 1px #c1c1c1;
	border-left: transparent;
}

select {
	font-size: 100%;
	border: 1px solid #c1c1c1;
	border-radius: 5px;
	padding: 9px 15px;
	margin-right: 10px;
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

.year {
	text-align: center;
	padding: 30px;
}

.year input {
	width: 80px;
	height: 35px;
	cursor: pointer;
	border: 1px solid #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
}

.year input:active {
	box-shadow: none;
	position: relative;
	top: 4px;
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
</style>


<script>
window.onload =function(){
	const trs = document.getElementById('visit_table').getElementsByTagName('tr');

	for(let i = 1; i < trs.length; i++){
		trs[i].addEventListener('click',function(){
			document.getElementById('form_' + i).submit();
		})
	}

	(function () {

        const isLeapYear = year => (year % 4 === 0) && (year % 100 !== 0) || (year % 400 === 0);
//関数定義、引数がyear

        /**
         * 任意の年の2月の日数を数える
         * @param {number} チェックしたい西暦年号
         * @return {number} その年の2月の日数
         */
        const countDatesOfFeb = year => isLeapYear(year) ? 29 : 28;
//関数定義

        /**
         * セレクトボックスの中にオプションを生成する
         * @param {string} セレクトボックスのDOMのid属性値
         * @param {number} オプションを生成する最初の数値
         * @param {number} オプションを生成する最後の数値
         * @param {number} 現在の日付にマッチする数値
         */
        const createOption = (id, startNum, endNum, current) => {
            const selectDom = document.getElementById(id);
            let optionDom = '';
            for (let i = startNum; i <= endNum; i++) {
                if (i === current) {
                    option = '<option value="' + i + '" selected>' + i + '</option>';
                } else {
                    option = '<option value="' + i + '">' + i + '</option>';
                }
                optionDom += option;
            }
            selectDom.insertAdjacentHTML('beforeend', optionDom);
        }

        // DOM
        const yearBox = document.getElementById('year');
        const monthBox = document.getElementById('month');
       // const dateBox = document.getElementById('date');

        // 日付データ
        const today = new Date();
        const thisYear = today.getFullYear();
        const thisMonth = today.getMonth() + 1;
        //const thisDate = today.getDate();

        let datesOfYear = [31, countDatesOfFeb(thisYear), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

        // イベント
        monthBox.addEventListener('change', (e) => {
            //dateBox.innerHTML = '';
            const selectedMonth = e.target.value;
           // createOption('date', 1, datesOfYear[selectedMonth - 1], 1);
        });

        yearBox.addEventListener('change', e => {
            monthBox.innerHTML = '';
            //dateBox.innerHTML = '';
            const updatedYear = e.target.value;
         //   datesOfYear = [31, countDatesOfFeb(updatedYear), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

            createOption('month', 1, 12, 1);
          //  createOption('date', 1, datesOfYear[0], 1);
        });

        // ロード時
        createOption('year', 2000, thisYear, thisYear);
        createOption('month', 1, 12, thisMonth);
       // createOption('date', 1, datesOfYear[thisMonth - 1], thisDate);
    })();

	const year_change_date = document.getElementById('year');
	const month_change_date = document.getElementById('month');
	document.getElementById('bt_change_date').addEventListener('click',function(){

	    let year_idx = year_change_date.selectedIndex;
	    let year_date = year_change_date.options[year_idx].value;

	    let month_idx = month_change_date.selectedIndex;
	    let month_date = month_change_date.options[month_idx].value;

	    document.getElementById('year_change').value = year_date;
	    document.getElementById('month_change').value = month_date;
	    document.getElementById('form_change_date').submit();
	});

}

</script>


<title>料理ランキング</title>
</head>
<body>

	<div class="large_block">

		<div class="year">
			<select id="year" name="year"></select>
			<select id="month" name="month"></select>
			<input type="button" id="bt_change_date" value="表示">
		</div>

		<form method="post" id="form_change_date" action="./popularfood">
		<input type="hidden" name="action" value="これはいらないと思うchange_date">
		<input type="hidden" id="year_change" name="year" value="">
		<input type="hidden" id="month_change" name="month" value="">
	</form>

	<div class="error">
				<%
					if (errormessage != null && !errormessage.equals("")) {
				%>
				<p>
					<span><%=errormessage%></span>
				</p>
				<%
					}
				%>
			</div>

		<table id="visit_table">
			<tr>
				<th>料理名</th>
				<th>当月注文回数</th>
				<th>金額</th>
				<th>合計売上金額</th>
			</tr>

			<%
				for (int i = 0; i < popularfoods.size(); i++) {
			%>

			<tr>
				<td><%=popularfoods.get(i).getOrder_id()%></td>
				<td><%=popularfoods.get(i).getSumquantity()%></td>
				<td><%=popularfoods.get(i).getOrder_price()%></td>
				<td><%=popularfoods.get(i).getSumprice()%></td>

			</tr>
			<%
				}
			%>
		</table>
	</div>

</body>
</html>