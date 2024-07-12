<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Login</title>
<style type="text/css">
table, th, td {
	border: 1px solid;
}

th {
	text-align: center;
	font-weight: bold;
}

.desno {
	text-align: right;
}
</style>
</head>
<body>
	<form method="post" action="${pageContext.servletContext.contextPath}/mvc/login">
		<table>
			<tr>
				<td>Korisnicko ime</td>
				<td><input name="korIme" required="true" type="text"></td>
			</tr>
			<tr>
				<td>Lozinka</td>
				<td><input name="lozinka" required="true" type="text"></td>
			</tr>
			</tr><input type="submit" value="Login"></tr>
		</table>
	</form>
</body>