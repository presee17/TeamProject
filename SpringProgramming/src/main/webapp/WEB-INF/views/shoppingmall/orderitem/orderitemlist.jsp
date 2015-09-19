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
			color: black;
			}
		
			table {
				width: 100%;
				border-collapse: collapse;
				font-size: small;
			}
			table, th, td {
				border: 1px solid black;
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
				color:black;
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
				color:black;
			}
			.title{
			text-decoration: none;
			color: black;
			}			
			
			.title:hover {
			color:orange;
			}
		</style>
	</head>
	
	<body>
	 	<h4 style="color:black">주문 상세 내역</h4>
	 	
	 	<table id="orderItemlist">
			<tr>
	 			<th style="widtd:30px">제품번호</td>
	 			<th style="widtd:80px">제품이름</td>
	 		 	<th style="widtd:40px">제품가격</td>
	 		 	<th style="widtd:30px">수량</td>
	 			<th style="widtd:50px">합계</td>
			</tr>	

			<c:forEach var="orderitem" items="${list}">
				<tr id="list">
					<td>${orderitem.productNo}</td>
		 			<td>${orderitem.productName}</td>
		 		 	<td>${orderitem.productPrice}</td>
		 		 	<td>${orderitem.orderItemCount}</td>
		 			<td>${orderitem.orderItemPrice}</td>
				</tr>
			</c:forEach>
	 	</table>
		<div id="pager">
			<a href="list?pageNo=1">[처음]</a>
			
			<c:if test="${groupNo>1}">
				<a href="list?pageNo=${startPageNo-pagesPerGroup}">[이전]</a>
			</c:if>
			
			<c:forEach var="i" begin="${startPageNo}" end="${endPageNo}">
				<a class='pageNo <c:if test="${pageNo==i}">selected</c:if>' href="list?pageNo=${i}">${i}</a>
			</c:forEach>
			
			<c:if test="${groupNo<totalGroupNo}">
				<a href="list?pageNo=${endPageNo+1}">[다음]</a>
			</c:if>
			<a href="list?pageNo=${totalPageNo}">[맨끝]</a>
		</div>
		<div id="btns">
					
			<a href="../order/orderlist?pageNo=${pageNo}">목록</a>
			<a href="delete?boardNo=${board.no}">삭제</a>
			
		</div>
	</body>
</html> 