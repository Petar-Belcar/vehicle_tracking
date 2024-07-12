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
	<h1>REST MVC - Pregled radara</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/izbirisSveRadare">Izbrisi sve radare </a>
	</ul>
	<br />
	<%
	if (((String) request.getAttribute("odgovor")).contains("OK")) {
	%>
	<table>
		<tr>
			<th></th>
			<th>ID
			<th>Adresa</th>
			<th>Mrezna vrata</th>
			<th>Geografska sirina</th>
			<th>Geografska duzina</th>
		</tr>
		<%
		int i = 0;
		List<Radar> radari = (List<Radar>) request.getAttribute("radari");
		for (Radar r : radari) {
		%>
		<tr>
			<td class="desno"><%=i%></td>
			<td><a
				href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadarPoId/<%= r.getId() %>"><%=r.getId()%></href></td>
			<td><%=r.getAdresaRadara()%></td>
			<td><%=r.getMreznaVrataRadara()%></td>
			<td><%=r.getGpsSirina()%></td>
			<td><%=r.getGpsDuzina()%></td>
			<td><a href="${pageContext.servletContext.contextPath}/mvc/radari/provjeraRadarPoId/<%= r.getId()%>">Provjeri</a>
			<td><a href="${pageContext.servletContext.contextPath}/mvc/radari/izbrisi/<%= r.getId()%>">Izbrisi</a>
		</tr>
		<%
		}
		%>
	</table>
	<%
	} else {
	%>
	<p>
		<%=(String) request.getAttribute("odgovor")%>
	</p>
	<%
	}
	%>
</body>
</html>
