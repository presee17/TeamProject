<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script type="text/javascript">
		</script>
		
		<script>
		//전체 선택 함수
		function CheckAll() {
		    var x = document.getElementById("checkCart");
		    x.checked = true;
		}
		function CheckNot () {
		    var x = document.getElementById("checkCart");
		    x.checked = false;
		}
		</script>
		
		<style type="text/css">
			a{
			color: white;
			}
		
			table {
				width: 100%;
				border-collapse: collapse;
				font-size: small;
			}
			table, th, td {
				border: 1px solid white;
				text-align: center;
			}
			th {
				background-color: orange;
				color: black;
			}
			.btn {
				margin: 10px;
				text-align: center;
				display:inline-block;
				background-color: black;
				color:white;
				width:90px;
				height:30px;
			}
			#btns{
				text-align: center;
			}
			
			.btn:hover {
				color: black;
				background-color: lightgray;
			}
			#list{
				color:white;
			}
			.title{
			text-decoration: none;
			color: white;
			}			
			
			.title:hover {
			color:orange;
			}
		</style>
		<script type="text/javascript">
			function btnClick(url) {
				var form1 = document.form1;
				form1.action = url;
				form1.submit();
			} 
		</script>
	</head>
	<body>
		<h4 style="color:white">장바구니 목록</h4>
		<form name="form1">
			<table>
				<tr style="height:40px">
					<th style="width:80px">체크박스</th>
					<th>상품이름</th>
					<th style="width:90px">수량</th>
					<th style="width:150px">가격</th>
				</tr>
				
				<c:forEach var="cart" items="${list}">
					<tr id="list">
						<td><input type="checkbox" name="cartNo" value="${cart.cartNo}"/></td>
						<td>${cart.productName}</td>
						<td>${cart.cartCount}</td>
						<td>${cart.cartPrice}</td>
					</tr>
				</c:forEach>
			</table>

			<div id="btns">
				<input class="btn" type="button" onclick="javascript:btnClick('../order/insert')" value="결제하기"/>
				<input class="btn" type="button" onclick="javascript:btnClick('../cart/delete');" value="장바구니삭제"/>
			</div>
		</form>
	</body>
</html>