<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Radar"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled kazni</title>
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
	<h1>REST MVC - Reset radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<%
	String brojRadara = (String) request.getAttribute("brojRadara");
	String brojIzbrisanih = (String) request.getAttribute("brojIzbrisanih");
	if (brojRadara != null || brojIzbrisanih != null) {
	%>

	<table>
		<tr>
			<td>Broj radara u kolekciji:
			<td><%=brojRadara%>
		</tr>
		<tr>
			<td>Broj izbrisanih radara u kolekciji:
			<td><%=brojIzbrisanih%>
		</tr>
	</table>
	<%} else {%>
	<p>Dosle je do greske kod resetanja radara</p>
	<%
	}
	%>
</body>
</html>
