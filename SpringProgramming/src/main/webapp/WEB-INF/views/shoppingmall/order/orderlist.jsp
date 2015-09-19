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
	 			<td style="widtd:80px">제품이름</td>
	 		 	<td style="widtd:40px">가격</td>
	 		 	<td style="widtd:30px">수량</td>
	 			<td style="widtd:50px">합계</td>
			</tr>	
			
			<c:forEach var="orderlist" items="${list}">
				<tr>
					<td>${orderlist.orderItemNo}</td>
		 			<td>${orderlist.productName}</td>
		 		 	<td>${orderlist.productPrice}</td>
		 		 	<td>${orderlist.orderItemCount}</td>
		 			<td>${orderlist.orderItemPrice}</td>
				</tr>
			</c:forEach>
	 	</table>
			주문이 완료되었습니다.
	</body>
</html> 