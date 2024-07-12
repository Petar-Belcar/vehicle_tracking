<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>REST MVC - Pregled voznji</title>
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
        #details {
            float: right;
            width: 45%;
            margin-left: 20px;
        }
        #list {
            float: left;
            width: 45%;
        }
    </style>
    <script>
        function showDetails(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila, postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina, gpsDuzina) {
            document.getElementById('details').innerHTML = 
                '<table>' +
                '<tr><td>ID vozila</td><td>' + id + '</td></tr>' +
                '<tr><td>Broj</td><td>' + broj + '</td></tr>' +
                '<tr><td>Vrijeme</td><td>' + new Date(vrijeme * 1000).toLocaleString() + '</td></tr>' +
                '<tr><td>Brzina</td><td>' + brzina + '</td></tr>' +
                '<tr><td>Snaga</td><td>' + snaga + '</td></tr>' +
                '<tr><td>Struja</td><td>' + struja + '</td></tr>' +
                '<tr><td>Visina</td><td>' + visina + '</td></tr>' +
                '<tr><td>GPS Brzina</td><td>' + gpsBrzina + '</td></tr>' +
                '<tr><td>Temperatura Vozila</td><td>' + tempVozila + '</td></tr>' +
                '<tr><td>Postotak Baterija</td><td>' + postotakBaterija + '</td></tr>' +
                '<tr><td>Napon Baterija</td><td>' + naponBaterija + '</td></tr>' +
                '<tr><td>Kapacitet Baterija</td><td>' + kapacitetBaterija + '</td></tr>' +
                '<tr><td>Temperatura Baterija</td><td>' + tempBaterija + '</td></tr>' +
                '<tr><td>Preostalo Km</td><td>' + preostaloKm + '</td></tr>' +
                '<tr><td>Ukupno Km</td><td>' + ukupnoKm + '</td></tr>' +
                '<tr><td>GPS Širina</td><td>' + gpsSirina + '</td></tr>' +
                '<tr><td>GPS Dužina</td><td>' + gpsDuzina + '</td></tr>' +
                '</table>';
        }
    </script>
</head>
<body>
    <h1>REST MVC - Pregled voznji</h1>
    <ul>
        <li><a href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna stranica</a></li>
    </ul>
    <br />
    <% if (((String) request.getAttribute("odgovor")).contains("OK")) { %>
    <div id="list">
        <table>
            <%
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
            List<Vozilo> vozila = (List<Vozilo>) request.getAttribute("listaVozila");
			if (vozila.size() == 0) {
				%> <p> Lista vozila je przna </p> <%
			} else {
				%>
				<tr>
					<th>ID vozila</th>
					<th>Broj</th>
					<th>Vrijeme</th>
				</tr>
				<%
				for (Vozilo r : vozila) {
					Date vrijeme = new Date(r.getVrijeme() * 1000);
				%>
				<tr onclick="showDetails('<%=r.getId()%>', '<%=r.getBroj()%>', '<%=r.getVrijeme()%>', '<%=r.getBrzina()%>', '<%=r.getSnaga()%>', '<%=r.getStruja()%>', '<%=r.getVisina()%>', '<%=r.getGpsBrzina()%>', '<%=r.getTempVozila()%>', '<%=r.getPostotakBaterija()%>', '<%=r.getNaponBaterija()%>', '<%=r.getKapacitetBaterija()%>', '<%=r.getTempBaterija()%>', '<%=r.getPreostaloKm()%>', '<%=r.getUkupnoKm()%>', '<%=r.getGpsSirina()%>', '<%=r.getGpsDuzina()%>')">
					<td><%=r.getId()%></td>
					<td><%=r.getBroj()%></td>
					<td><%=sdf.format(vrijeme)%></td>
				</tr>
            <% }} %>
        </table>
    </div>
    <div id="details">
        <h2>Detaljni pogled</h2>
        <p>Klikni na voznju za popunjavanje</p>
    </div>
    <% } else { %>
    <p><%= (String) request.getAttribute("odgovor") %></p>
    <% } %>
</body>
</html>
