<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrate</title>

<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script
	src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

</head>
<body>
	<div class="container mt-5">
		<div class="row">
			<div class="col-12 col-md-6">
				<form action="login" method="POST">
					<div class="form-group">
						<label for="userName">Nombre de usuario</label> <input type="text"
							class="form-control" id="userName" name="user" required
							placeholder="Escribe tu nombre de usuario">
					</div>
					<div class="form-group">
						<label for="userMail">Dirección email</label> <input type="email"
							class="form-control" id="userMail" name="email" required
							placeholder="Escribe tu email">
					</div>
					<div class="form-group">
						<label for="userPass">Password</label> <input type="password"
							class="form-control" id="userPass" name="password" required
							placeholder="Escribe tu contraseña">
					</div>
					<button type="submit" class="btn btn-primary">Registrarme</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>