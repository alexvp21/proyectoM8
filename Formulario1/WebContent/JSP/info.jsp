<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="infoForm" class="java.lang.String" scope="request" />
<jsp:useBean id="user" class="java.lang.String" scope="request" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Información</title>
</head>
<body>
	${infoForm}<br/>
	${user}
</body>
</html>