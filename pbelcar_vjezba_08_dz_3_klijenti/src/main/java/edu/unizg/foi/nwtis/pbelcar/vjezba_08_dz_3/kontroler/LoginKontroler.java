package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.kontroler;

import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model.RestKlijentLogin;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Controller
@Path("login")
@RequestScoped
public class LoginKontroler {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @Inject
  private ServletContext context;

  private String base_uri;

  @GET
  @View("login.jsp")
  public void login() {}

  @GET
  @Path("info")
  @View("nedovoljnoPrava.jsp")
  public void info() {}

  @POST
  @View("index.jsp")
  public void loginResult(@FormParam("korIme") String korIme, @FormParam("lozinka") String lozinka,
      @Context HttpServletRequest zahtjev) {
    var l = new RestKlijentLogin();
    Boolean result = l.getLogin(this.base_uri, korIme, lozinka);

    HttpSession sesija = zahtjev.getSession(true);
    if (result != null && result) {
      sesija.setAttribute("session", "true");
      this.model.put("loginRez", "Login uspjesan");
    } else {
      sesija.setAttribute("session", "false");
      this.model.put("loginRez", "Login neuspjesan");
    }
  }

  @GET
  @Path("logout")
  @View("index.jsp")
  public void logout(@Context HttpServletRequest zahtjev) {
    var sesija = zahtjev.getSession(true);

    sesija.setAttribute("session", "false");
  }

  @PostConstruct
  private void ucitajKonfiguraciju() {
    try {
      var konfig = KonfiguracijaApstraktna
          .preuzmiKonfiguraciju(context.getRealPath("WEB-INF/konfiguracija.txt"));
      this.base_uri = konfig.dajPostavku("webservis.radari.baseuri");
    } catch (NeispravnaKonfiguracija e) {
    }
  }
}
