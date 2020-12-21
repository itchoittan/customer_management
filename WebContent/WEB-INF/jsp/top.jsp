<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <style>
        table {
            min-width: 650px;
            height: 400px;
            margin: 50px auto;
            background-color: #ffd1a3;
            border-radius: 15px;
        }

        input {
            font-size: 16px;
            border: none;
            outline: none;
            width: 160px;
            height: 50px;
            cursor: pointer;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #ffefef;
            border-radius: 30px;
            box-shadow: 4px 4px 4px #ff7f00;
        }

        input:active {
            box-shadow: none;
            position: relative;
            top: 4px;
        }

        .left {
            margin: 0 50px 0 100px;
        }

        .right {
            margin: 0 100px 0 50px;
        }
    </style>

<script>
        function op(url) {
            let width = 900;
            let height = 1000;
            let left = window.parent.screen.width / 2 - (width / 2);
            let top = 0;

            window.open(url, "aaa", 'top=' + top + ',left=' + left + ',width=' + width + ',height=' + height + '');
        }

        window.onload = function () {

            document.getElementById('bt_input').addEventListener('click', function () {
                op('./input');
            });
            document.getElementById('bt_visit').addEventListener('click', function () {
                op('visit.html');
            });
            document.getElementById('bt_name').addEventListener('click', function () {
                op('./name');
            });
            document.getElementById('bt_phone').addEventListener('click', function () {
                op('./phone');
            });
            document.getElementById('bt_popularfood').addEventListener('click', function () {
                op('popularfood.html');
            });
            document.getElementById('bt_populardrink').addEventListener('click', function () {
                op('populardrink.html');
            });

        }

    </script>


<title>飲食店顧客管理名簿システム</title>
</head>

<body>
	<table>

		<tr>
			<td><input class="left" type="button" id="bt_input" value="入力画面">
			<td><input class="right" type="button" id="bt_visit"
				value="来店回数一覧">
		</tr>

		<tr>
			<td><input class="left" type="button" id="bt_name" value="名前検索">
			<td><input class="right" type="button" id="bt_phone"
				value="電話番号検索">
		</tr>

		<tr>
			<td><input class="left" type="button" id="bt_popularfood"
				value="人気メニュー検索">
			<td><input class="right" type="button" id="bt_populardrink"
				value="人気ドリンク検索">
		</tr>

	</table>


</body>
</html>