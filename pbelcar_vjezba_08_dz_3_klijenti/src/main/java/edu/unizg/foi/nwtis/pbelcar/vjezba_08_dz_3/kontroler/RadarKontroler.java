package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.kontroler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model.RestKlijentRadari;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Controller
@Path("radari")
@RequestScoped
public class RadarKontroler {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;
  
  @Inject
  private ServletContext context;

  private String base_uri;

  @GET
  @Path("ispisRadara")
  @View("radari.jsp")
  public void json() {
    RestKlijentRadari r = new RestKlijentRadari();
    var odgovor = r.getRadariJSON(this.base_uri);
    model.put("radari", odgovor.getListaRadara());
    model.put("odgovor", odgovor.getOdgovor());
  }

  Pattern predlozakReset = Pattern.compile("OK (?<brojRadara>\\d+) (?<brojIzbrisanih>\\d+)");
  Matcher poklapanjeReset;

  @GET
  @Path("resetRadara")
  @View("radariReset.jsp")
  public void json_Reset() {
    RestKlijentRadari r = new RestKlijentRadari();
    var odg = r.getRadariResetJSON(this.base_uri);
    this.poklapanjeReset = this.predlozakReset.matcher(odg);
    if (this.poklapanjeReset.find()) {
      model.put("brojRadara", this.poklapanjeReset.group("brojRadara"));
      model.put("brojIzbrisanih", this.poklapanjeReset.group("brojIzbrisanih"));
    }
  }

  @GET
  @Path("ispisRadarPoId/{id}")
  @View("radar.jsp")
  public void json_RadarPoId(@PathParam("id") int id) {
    RestKlijentRadari r = new RestKlijentRadari();
    model.put("radari", r.getRadarPoIDJSON(id, this.base_uri).getListaRadara());
  }

  @GET
  @Path("provjeraRadarPoId/{id}")
  @View("radarProvjera.jsp")
  public void json_RadarProvjeraPoId(@PathParam("id") int id) {
    RestKlijentRadari r = new RestKlijentRadari();
    model.put("odg", r.getRadarProvjeraPoIDJSON(id, this.base_uri));
  }

  @GET
  @Path("izbirisSveRadare")
  @View("radari.jsp")
  public void json_izbirisiSveRadare() {
    RestKlijentRadari r = new RestKlijentRadari();
    r.deleteIzbrisiSveRadarae(this.base_uri);
    var odgovor = r.getRadariJSON(this.base_uri);
    model.put("radari", odgovor.getListaRadara());
    model.put("odgovor", odgovor.getOdgovor());
  }

  @GET
  @Path("izbrisi/{id}")
  @View("radari.jsp")
  public void json_izbrisiRadarPoId(@PathParam("id") int id) {
    RestKlijentRadari r = new RestKlijentRadari();
    r.deleteIzbrisiRadarPoId(id, this.base_uri);
    r = new RestKlijentRadari();
    var odgovor = r.getRadariJSON(this.base_uri);
    model.put("radari", odgovor.getListaRadara());
    model.put("odgovor", odgovor.getOdgovor());
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
