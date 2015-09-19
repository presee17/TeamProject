<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<style type="text/css">
		.btn {
				margin: 10px;
				text-align: center;
				display: inline-block;
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
			
		div{
			margin-top:100px;
			text-align: center;
		}
		</style>
	</head>
	
	<body>
		<div>
			<div class="btn"><a href="../product/product">상품리스트</a></div>

			<div class="btn"><a href="../cart/cart" target="iframe">장바구니</a></div>
			
			<div class="btn"><a href="../order/order">주문내역</a></div>
		</div>
	</body>
</html>