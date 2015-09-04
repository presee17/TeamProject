<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
	<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
		<style type="text/css">
		td {
		color : white;
		}
		</style>
	</head>
	
	<body>
		<form method="post" action="write" enctype="multipart/form-data">
			<table>
				<tr>
					<td>이름</td>
					<td><input type="text" name="name"/></td>
				</tr>
				
				<tr>
					<td>가격</td>
					<td><input type="number" step = "100" min = "500" max = "999999" name="price"/></td>
				</tr>
				
				<tr>
					<td>재고</td>
					<td><input type="number" min="1" max= "999" name="stock"/></td>
				</tr>
				
				<tr>
					<td>상세정보</td>
					<td><textarea name="detail" rows="5" cols="50"></textarea></td>
				</tr>
				
				<tr>
					<td>첨부</td>
					<td><input type="file" name="attach"/></td>
				</tr>
				
			</table>
			<input type="submit" value="글올리기"/>
			<input type="reset" value="다시작성"/>
		</form>
	</body>
</html>