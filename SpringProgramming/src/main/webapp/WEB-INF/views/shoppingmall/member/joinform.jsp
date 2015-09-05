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
	<div id ="login">
		<form method="post" action="join">
			<table>
					<tr>
						<td>id :</td>
						<td><input type="text" name="id"/></td>
					</tr>
					
					<tr>
						<td>Pw :</td>
						<td><input type="password" name="pw"/></td>
					</tr>
					
					<tr>
						<td>name :</td>
						<td><input type="password" name="name"/></td>
					</tr>
					
					<tr>
						<td colspan="2" style="text-align: center;">
							<br/>
							<input type="submit" value="회원가입"/>
						</td>
					</tr>
					
				</table>
		</form>
	</div>
	</body>
</html>