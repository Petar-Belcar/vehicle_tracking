<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Početna stranica</title>
</head>
<body>
	<h1>REST MVC - Početna stranica</h1>
	<%
	if (request.getAttribute("dodavanjeVoznjaPoruka") != null) {
	  String p = (String) request.getAttribute("dodavanjeVoznjaPoruka");
	%>
	<div>
		<h2 style="color: red"><%=p%></h2>
	</div>
	<%
	}
	if (request.getAttribute("loginRez") != null) {
		  String p = (String) request.getAttribute("loginRez");
		%>
		<div>
			<h2 style="color: green"><%=p%></h2>
		</div>
		<%
		}
		
	if ((String) session.getAttribute("session") == "true") {
		%>
		<ul>
			<li><a href="${pageContext.servletContext.contextPath}/mvc/login/logout">Logout</a></li>
		</ul>
		<%
	}
	%>
	
	<h2>Kazne</h2>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/pocetak/pocetak">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
				svih kazni</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/kazneOdDoForm">Ispis
				svih kazni u vremenskom rasponu</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/kaznePoRB">Ispis
				kazne po rednom broju</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/kaznePoVoziloID">Ispis
				kazni po IDu vozila</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/kaznePoVoziloIDOdDo">Ispis
				kazni po IDu vozila u vremenskom rasonu</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/kazneTest">Test
				PosluziteljKazni</a></li>

	</ul>
	<h2>Radari</h2>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">
				Ispis svih radara</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/resetRadara">
				Resetanje radara</a></li>
	</ul>
	<h2>Vozila</h2>
	<ul>
		<li>
			<p>Ispis voznja u rasponu (ulazi moraju biti 1 ili vise)
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/odDoVoznja">
				<table>
					<tr>
						<td>Od:
						<td><input name="od" value="1" /><input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<td>Do:
						<td><input name="do" value="1" />
					<tr>
						<td><input type="submit" value="Dohvati voznje" />
				</table>
			</form>
		</li>

		<li>
			<p>Ispis voznja po idu vozila (bez rasona postaviti Od i Do na 0)</p>

			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/voznjaId">
				<table>
					<tr>
						<td>Id:
						<td><input name="id" />
					</tr>
					<tr>
						<td>Od:
						<td><input name="od" value="0" /><input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<td>Do:
						<td><input name="do" value="0" />
					<tr>
						<td><input type="submit" value="Dohvati voznje" />
				</table>
			</form>
		</li>

		<li>
			<p>Pracenje vozila po idu</p>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/startStop">
				<table>
					<tr>
						<td>Id:
						<td><input name="id" /><input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
					</tr>
					<td><select name="pratiti">
							<option value="true">Start</option>
							<option value="false">Stop</option>
					</select></td>
					<td><input type="submit" value="Posalji" />
				</table>
			</form>
		</li>
		<li>
			<p>Slanje novih pracenih voznji</p>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/noveVoznje">
				<table>
					<tr>
						<td>Id:
						<td><input name="id" />
					</tr>
					<tr>
						<td><select name="csv">
								<option value="NWTiS_DZ1_V1.csv">1. skup podataka</option>
								<option value="NWTiS_DZ1_V2.csv">2. skup podataka</option>
								<option value="NWTiS_DZ1_V3.csv">3. skup podataka</option>
						</select> <input type="hidden" name="${mvc.csrf.name}"
							value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<input type="submit" value="Posalji" />
					</tr>
				</table>
			</form>
		</li>
	</ul>
	<h2>Simulacija</h2>
	<ul>
		<li>
			<p>Ispis voznja u rasponu (ulazi moraju biti 1 ili vise)
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/sim/odDoVoznja">
				<table>
					<tr>
						<td>Od:
						<td><input name="od" value="1" /><input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<td>Do:
						<td><input name="do" value="1" />
					<tr>
						<td><input type="submit" value="Dohvati voznje" />
				</table>
			</form>
		</li>

		<li>
			<p>Ispis voznja po idu vozila (bez rasona postaviti Od i Do na 0)</p>

			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/sim/voznjaId">
				<table>
					<tr>
						<td>Id:
						<td><input name="id" />
					</tr>
					<tr>
						<td>Od:
						<td><input name="od" value="0" /><input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<td>Do:
						<td><input name="do" value="0" />
					<tr>
						<td><input type="submit" value="Dohvati voznje" />
				</table>
			</form>
		</li>
		<li>
			<p>Slanje novih pracenih voznji</p>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/sim/noveVoznje">
				<table>
					<td>Id:
					<td><input name="id" />
					</tr>
					<tr>
						<td><select name="csv">
								<option value="NWTiS_DZ1_V1.csv">1. skup podataka</option>
								<option value="NWTiS_DZ1_V2.csv">2. skup podataka</option>
								<option value="NWTiS_DZ1_V3.csv">3. skup podataka</option>
						</select> <input type="hidden" name="${mvc.csrf.name}"
							value="${mvc.csrf.token}" />
					</tr>
					<tr>
						<input type="submit" value="Posalji" />
					</tr>
				</table>
			</form>
		</li>
	</ul>
</body>
</html>
