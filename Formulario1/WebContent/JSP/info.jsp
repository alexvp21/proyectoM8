<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="infoForm" class="java.lang.String" scope="request" />
<jsp:useBean id="user" class="java.lang.String" scope="request" />
<jsp:useBean id="status" class="java.lang.String" scope="request" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Informacion</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

</head>
<body>
	<% if(status.equals("ok")) { %>
		<div class="alert alert-success" role="alert">
		  <p>${infoForm}</p>
		  <p>${user}</p>
		</div>
	<% } else {%>
		<div class="alert alert-danger" role="alert">
		  <p>${user}</p>
		</div>
	<% } %>
</body>
</html>