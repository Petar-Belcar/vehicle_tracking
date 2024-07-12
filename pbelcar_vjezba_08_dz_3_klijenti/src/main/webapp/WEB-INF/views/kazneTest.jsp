<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Kazna"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Testiranje PosluziteljKazni</title>
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
	<h1>REST MVC - Testiranje PosluziteljKazni</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<%
	String tekst = (String) request.getAttribute("test");
	%>
	<p><%=tekst%></p>
</body>
</html>
