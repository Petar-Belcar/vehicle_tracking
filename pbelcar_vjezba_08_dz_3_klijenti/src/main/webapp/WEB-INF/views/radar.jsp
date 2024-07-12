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
	<h1>REST MVC - Radar po ID</h1>
	<%
	List<Radar> radari = (List<Radar>) request.getAttribute("radari");
	if (radari.size() != 0) {
	  Radar radar = radari.get(0);
	%>
	<table>
		<tr>
			<td>Id</td>
			<td><%=radar.getId()%></td>
		</tr>
		<tr>
			<td>Adresa</td>
			<td><%=radar.getAdresaRadara()%></td>
		</tr>
		<tr>
			<td>Mrezna vrata</td>
			<td><%=radar.getMreznaVrataRadara()%></td>
		</tr>
		<tr>
			<td>Geografska sirina</td>
			<td><%=radar.getGpsSirina()%></td>
		</tr>
		<tr>
			<td>Geografska duzina</td>
			<td><%=radar.getGpsDuzina()%></td>
		</tr>

	</table>
	<%} else {%>
	<p>
		Radar sa trazenim idem ne postoji
		<%
	}
	%>
	
</body>
</html>
