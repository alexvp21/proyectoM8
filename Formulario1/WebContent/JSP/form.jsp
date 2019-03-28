<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrate!</title>
</head>
<body>
	<form action="login" method="POST">
		<table>
			<tr>
				<td>User</td>
				<td><input name="user" /></td>
			</tr>
			<tr>
				<td>password</td>
				<td><input name="password" /></td>
			</tr>
			<tr>
				<td>email</td>
				<td><input name="email" /></td>
			</tr>
		</table>
		<input type="submit" value="Enviar"/>
	</form>
</body>
</html>