<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String errormessage = (String) request.getAttribute("errormessage");
if (errormessage == null) {
	errormessage = "";
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<style>
.large_block {
	text-align: center;
	width: 650px;
	min-width: 650px;
	height: auto;
	padding: 20px;
	margin: 50px auto;
	background-color: #ffd1a3;
	border-radius: 15px;
}

.phone {
	width: 400px;
	height: 50px;
	font-size: 24px;
	border: 1px solid #c1c1c1;
	border-radius: 5px;
	padding: 6px 12px;
}

input:focus {
	border-color: #ff7f00;
	box-shadow: 4px 4px 4px #ff7f00;
	outline: 0;
}

.registration {
	padding: 20px;
}

.registration>input {
	width: 150px;
	height: 50px;
	font-size: 16px;
	cursor: pointer;
	border: 1px solid #ff7f00;
	border-radius: 5px;
	box-shadow: 4px 4px 4px #ff7f00;
	padding: 6px 12px;
}

.registration>input:active {
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



<title>名前検索画面</title>
</head>
<body>

	<div class="large_block">

		<div>

			<p>検索したい名前をカタカタで入力してください</p>

			<form method="POST">

				<input type="tel" class="phone" name="name">

				<div class="registration">
					<input type="submit" value="検　索">
				</div>

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

		</div>

	</div>


</body>
</html>