<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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


<title>Insert title here</title>
</head>
<body>

   <div class="large_block">

        <table>
            <tr>
                <th>名前</th>
                <th>電話番号</th>
                <th>来店回数</th>
                <th>前回来店日</th>
            </tr>

            <a href="#">
                <tr class="profile2">
                    <td class="left">な</td>
                    <td>telget</td>
                    <td>1</td>
                    <td>来店日GGET</td>
                </tr>
            </a>

            <tr class="profile2">
                <a href="#">
                    <td>rrrrrr</td>
                    <td>000000000</td>
                    <td>1555</td>
                    <td>来店日GGET</td>
                </a>
            </tr>

            <tr class="profile2">
                <a href="#">
                    <td class="left">なgagagaagaga</td>
                    <td>telgegaat</td>
                    <td>12</td>
                    <td>来店日GGET</td>
                </a>
            </tr>

            <tr class="profile2">
                <a href="#">
                    <td>タナカ</td>
                    <td>telget</td>
                    <td>300</td>
                    <td>来店日GGET</td>
                </a>
            </tr>

        </table>

    </div>

</body>
</html>