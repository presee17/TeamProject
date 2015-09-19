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
	 	<h5>주문 상세 내역</h5>
	 	
	 	<table id="orderItemlist">
			<tr>
	 			<td style="widtd:30px">제품번호</td>
	 			<td style="widtd:80px">제품이름</td>
	 		 	<td style="widtd:40px">제품가격</td>
	 		 	<td style="widtd:30px">수량</td>
	 			<td style="widtd:50px">합계</td>
			</tr>	

			<c:forEach var="orderitem" items="${list}">
				<tr>
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
			<div class="btn" onclick="javascript:alert('결제완료');">결제하기</div>
		</div>
	</body>
</html> 