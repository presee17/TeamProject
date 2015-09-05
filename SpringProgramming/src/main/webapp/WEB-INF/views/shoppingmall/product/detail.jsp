<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<style type="text/css">
			body {
				font-family: "돋움";
				font-size: 12px;
			}
			span {
				display: inline-block;
				margin: 2px 10px;
			}
			
			span.title {
				margin: 2px 10px;
				border: 1px solid darkgray;
				background: lightgray;
				width: 70px;
				text-align: center;
				color: black;
			}
			
			pre {
				margin: 10px;
				border: 1px solid darkgray;
				padding: 10px;
				width: 90%;
				height: 100px;
				font-size: 12px;
			}
			
			#part1 {
				display: flex;
			}
			#part1_1 {
				flex: 1;
			}
			
			#buttonGroup {
				margin: 10px;
				text-align: center;
				width: 300px;
				height:100px;
			}
			
			#buttonGroup a {
				
				line-height: 30px;
				text-decoration: none;
				font-size: small;
				color: white;
				border: 1px solid darkgray;
				background-color: gray;
				font-weight: bold;
			}
			
			#buttonGroup a:hover {
				color: black;
				background-color: lightgray;
			}
			
		</style>
	</head>
	
	<body>
	<h4>상품 세부사항</h4>
		<div id="part1">
			<div id="part1_1">	
				
				<span class="title">상품이름: </span> 
				<span class="content">${product.name}</span> <br/>
				
				<span class="title">단가: </span> 
				<span class="content">${product.price}</span> <br/>
				
				<span class="title">주문수량:</span> 
				<input id="title" type="number" name="count" value="${cart.count}"/> <br/>
				
			</div>
			
		</div>
			<div id="part2">
			<span class="title">상품설명:</span> <br/>
			<span class="content" style="width:300px; height:100px;">${product_detail}</span>
		</div>
		
		<div id="buttonGroup">
			<div class="btn" onclick="javascript:alert('장바구니에 담았습니다^^');"><a href="product">장바구니 담기</a></div>
		</div>		
	</body>
</html>