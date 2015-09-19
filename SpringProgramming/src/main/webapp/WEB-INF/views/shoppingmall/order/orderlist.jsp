<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
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
			
			.btn a {
				display:inline-block;
				width: 70px;
				line-height: 30px;
				text-decoration: none;
				font-size: small;
				color: white;
				border: 1px solid darkgray;
				background-color: gray;
				font-weight: bold;
			}
			
			.btn a:hover {
				color: black;
				background-color: lightgray;
			}
			
			.btn {
				margin: 10px;
				text-align: center;
				display:inline-block;
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
	 	<h4 style="color:white">주문 내역</h4>
	 	
	 	<table id="orderlist">
			<tr>
	 			<th style="widtd:30px">주문번호</td>
	 			<th style="widtd:80px">총 가격</td>
	 		 	<th style="widtd:40px">배송 상태</td>
	 		 	<th style="widtd:30px">주문 날짜</td>
			</tr>	
			
			<c:forEach var="orderlist" items="${list}">
				<tr id="list">
					<td><a href ="../orderitem/orderitemlist?orderNo=${orderlist.orderNo}">${orderlist.orderNo}</a></td>
		 			<td>${orderlist.orderPrice}</td>
		 		 	<td>${orderlist.orderDelivery}</td>
		 		 	<td>${orderlist.orderDate}</td>
				</tr>
			</c:forEach>
	 	</table>

		<div id="btns">
					
			<div class="btn"><a href="../main">메인 메뉴</a></div>
			
		</div>
	</body>
</html> 