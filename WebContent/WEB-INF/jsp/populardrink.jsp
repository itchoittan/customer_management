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
if (errormessage == null) {
	errormessage = "";
}


%>


<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">

 <style>
        a {
            text-decoration: none;
            color: #000000;
            background-color: #ffffff;
        }

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
    </style>


<script>
window.onload =function(){
	const trs = document.getElementById('visit_table').getElementsByTagName('tr');

	for(let i = 1; i < trs.length; i++){
		trs[i].addEventListener('click',function(){
			document.getElementById('form_' + i).submit();
		})
	}
}

</script>


<title>ドリンクランキング</title>
</head>
<body>

   <div class="large_block">

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