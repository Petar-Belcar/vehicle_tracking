<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Kazna"%>
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
	<h1>REST MVC - Pregled kazni</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna
				stranica</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>R.br.
			<th>Vozilo</th>
			<th>Vrijeme</th>
			<th>Brzina</th>
			<th>GDP širina</th>
			<th>GPS dužina</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Kazna> kazne = (List<Kazna>) request.getAttribute("kazne");
		for (Kazna k : kazne) {
		  i++;
		  Date vrijeme = new Date(k.getVrijemeKraj() * 1000);
		%>
		<tr>
			<td class="desno"><%=k.getRb()%></td>
			<td><%=k.getId()%></td>
			<td><%=sdf.format(vrijeme)%></td>
			<td><%=k.getBrzina()%></td>
			<td><%=k.getGpsSirina()%></td>
			<td><%=k.getGpsDuzinaRadar()%></td>
		</tr>
		<%
		}
		%>
	</table>
</body>
</html>
