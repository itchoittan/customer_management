<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
ArrayList<PopularOrder> populardrinks = (ArrayList<PopularOrder>) request.getAttribute("popularOrder");

String errormessage = (String) request.getAttribute("errormessage");
if (populardrinks == null) {
	populardrinks = new ArrayList<PopularOrder>();
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

	(function () {

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

        const yearBox = document.getElementById('year');
        const monthBox = document.getElementById('month');

        const today = new Date();
        const thisYear = today.getFullYear();
        const thisMonth = today.getMonth() + 1;

        createOption('year', 2000, thisYear, thisYear);
        createOption('month', 1, 12, thisMonth);

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


<title>ドリンクランキング</title>
</head>
<body>

	<div class="large_block">

		<div class="year">
			<select id="year" name="year"></select> <select id="month"
				name="month"></select> <input type="button" id="bt_change_date"
				value="表示">
		</div>

		<form method="post" id="form_change_date" action="./populardrink">

			<input type="hidden" id="year_change" name="year" value=""> <input
				type="hidden" id="month_change" name="month" value="">
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
				<th>ドリンク名</th>
				<th>当月注文回数</th>
				<th>金額</th>
				<th>合計売上金額</th>
			</tr>

			<%
			for (int i = 0; i < populardrinks.size(); i++) {
			%>

			<tr>
				<td><%=populardrinks.get(i).getOrder_id()%></td>
				<td><%=populardrinks.get(i).getSumquantity()%></td>
				<td><%=populardrinks.get(i).getOrder_price()%></td>
				<td><%=populardrinks.get(i).getSumprice()%></td>

			</tr>
			<%
			}
			%>
		</table>
	</div>

</body>
</html>