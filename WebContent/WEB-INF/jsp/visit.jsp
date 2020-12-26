<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="bean.*"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

    <%
	ArrayList<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");
ArrayList<Date> orderdates = (ArrayList<Date>) request.getAttribute("orderdates");
String errormessage = (String) request.getAttribute("errormessage");
if (customers == null) {
	customers = new ArrayList<Customer>();
}
if (orderdates == null) {
	orderdates = new ArrayList<Date>();
}
if (errormessage == null) {
	errormessage = "";
}

SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
ArrayList<String> visitdates = new ArrayList<>();
for (int i = 0; i < orderdates.size(); i++) {
	String orderdate = sdf.format(orderdates.get(i));
	visitdates.add(orderdate);
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
            cursor: pointer;
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


<title>来店回数一覧</title>
</head>
<body>

   <div class="large_block">

        <table id="visit_table">
			<tr>
				<th>名前</th>
				<th>携帯番号</th>
				<th>固定電話番号</th>
				<th>来店回数</th>
				<th>前回来店日</th>
			</tr>

			<%
				for (int i = 0; i < customers.size(); i++) {
			%>

			<tr>
				<td><%=customers.get(i).getName()%></td>
				<td><%=(customers.get(i).getMobilephone() == null ? "" : customers.get(i).getMobilephone())%></td>
				<td><%=(customers.get(i).getPhone() == null ? "" : customers.get(i).getPhone())%></td>
				<td><%=customers.get(i).getNumbervisit()%></td>
				<td><%=visitdates.get(i)%>
					<form method="POST" id="form_<%=i + 1%>">
						<input type="hidden" name="customer_id"
							value="<%=customers.get(i).getCustomers_id()%>">
					</form></td>

			</tr>
			<%
				}
			%>
		</table>
    </div>

</body>
</html>