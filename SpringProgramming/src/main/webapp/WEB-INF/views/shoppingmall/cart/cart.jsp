<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<script type="text/javascript">
		
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
	</head>
	<body>
		<h4 style="color:white">장바구니 목록</h4>
		
		<table>
			<tr style="height:40px">
				<th style="width:80px">체크박스</th>
				<th style="width:90px">상품번호</th>
				<th>상품이름</th>
				<th style="width:90px">수량</th>
				<th style="width:150px">가격</th>
			</tr>
			
			<c:forEach var="cart" items="${list}">
				<tr id="list">
					<td><input type="checkbox" name="check"/></td>
					<td>${cart.productNo}</td>
					<td>${cart.productName}</td>
					<td>${cart.cartCount}</td>
					<td>${cart.cartPrice}</td>
				</tr>
			</c:forEach>
		</table>
		
	
		<div id="btns">
			<form method="post" action="../cart/insert">
				<input class="btn" onclick="javascript:alert('결제완료^^');" type="submit" value="결제하기"/>
			</form>
			<form method="post" action="../cart/delete">
				<input class="btn" onclick="javascript:alert('삭제완료^^');" type="submit" value="장바구니삭제"/>
			</form>
		</div>
	</body>
</html>