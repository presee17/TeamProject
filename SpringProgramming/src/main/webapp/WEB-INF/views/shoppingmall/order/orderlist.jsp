<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<style>
		h5 {
		text-align: center;
		}
		</style>
	</head>
	
	<body>
	 	<h5>주문 내역</h5>
	 	
	 	<table id="orderlist">
			<tr>
	 			<td style="widtd:30px">주문번호</td>
	 			<td style="widtd:80px">총 가격</td>
	 		 	<td style="widtd:40px">배송 상태</td>
	 		 	<td style="widtd:30px">주문 날짜</td>
			</tr>	
			
			<c:forEach var="orderlist" items="${list}">
				<tr>
					<td><a href ="../orderitem/orderitemlist?orderNo=${orderlist.orderNo}">${orderlist.orderNo}</a></td>
		 			<td>${orderlist.orderPrice}</td>
		 		 	<td>${orderlist.orderDelivery}</td>
		 		 	<td>${orderlist.orderDate}</td>
				</tr>
			</c:forEach>
	 	</table>
	</body>
</html> 