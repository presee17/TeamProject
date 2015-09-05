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
			#pager {
				margin-top: 5px;
				text-align: center;
				font-size: small;
			}
			
			#pager a {
				text-decoration: none;
				color: red;
			}
			
			#pager a:hover {
				color: orange;
			}
			
			#pager a.pageNo {
				margin-left: 5px;
				margin-right: 5px;
			}
			
			#pager a.pageNo.selected {
				color: aqua;
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
		<h4>장바구니 목록</h4>
		
		<table>
			<tr style="height:40px">
				<th style="width:80px">체크박스</th>
				<th style="width:90px">상품번호</th>
				<th>상품이름</th>
				<th style="width:90px">수량</th>
				<th style="width:150px">가격</th>
			</tr>
			
			<c:forEach var="cart" items="${list}">
				<tr>
					<td><input type="checkbox" name="check"/></td>
					<td>${product.no}</td>
					<td>${product.name}</td>
					<td>${cart.count}</td>
					<td>${cart.price}</td>
				</tr>
			</c:forEach>
		</table>
		
	
		<div id="btns">
			<div class="btn" onclick="javascript:alert('결제완료');">결제하기</div>
			<div class="btn" onclick="javascript:alert('장바구니 삭제');">지우기</div>
		</div>
	</body>
</html>