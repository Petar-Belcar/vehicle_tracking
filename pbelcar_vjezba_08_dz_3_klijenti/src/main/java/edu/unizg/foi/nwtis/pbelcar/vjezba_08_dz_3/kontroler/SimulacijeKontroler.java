package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.kontroler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model.RestKlijentSimulacija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model.RestKlijentVozila;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno.OdgovorListaVozila;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Controller
@Path("sim")
@RequestScoped
public class SimulacijeKontroler {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;
  @Inject
  private ServletContext context;

  private String base_uri;

  @POST
  @Path("odDoVoznja")
  @View("vozila.jsp")
  public void json(@FormParam("od") long vrijemeOd, @FormParam("do") long vrijemeDo) {
    RestKlijentSimulacija r = new RestKlijentSimulacija();
    var odg = r.getVozilaOdDo(vrijemeOd, vrijemeDo, this.base_uri);
    model.put("odgovor", odg.getOdgovor());
    model.put("listaVozila", odg.getVoznje());
  }

  @POST
  @Path("voznjaId")
  @View("vozila.jsp")
  public void json(@FormParam("id") int id, @FormParam("od") long vrijemeOd,
      @FormParam("do") long vrijemeDo) {
    RestKlijentSimulacija r = new RestKlijentSimulacija();
    OdgovorListaVozila odg;
    if (vrijemeOd <= 0 || vrijemeDo <= 0) {
      odg = r.getVozilaId(id, this.base_uri);
    } else {
      odg = r.getVozilaId(id, vrijemeOd, vrijemeDo, this.base_uri);
    }
    model.put("odgovor", odg.getOdgovor());
    model.put("listaVozila", odg.getVoznje());
  }


  @POST
  @Path("noveVoznje")
  @View("index.jsp")
  public void json(@FormParam("csv") String nazivDatoteke, @FormParam("id") int id) {
    RestKlijentSimulacija r = new RestKlijentSimulacija();

    String path = "/WEB-INF/" + nazivDatoteke;
    try (InputStream strem = context.getResourceAsStream(path)) {
      BufferedReader citac = new BufferedReader(new InputStreamReader(strem));

      int i = 0;
      String linija;
      while ((linija = citac.readLine()) != null) {
        r.postVoznja(linija, id, i++, this.base_uri);
      }

      model.put("dodavanjeVoznjaPoruka", "Dodavanje voznje je uspjesno");
    } catch (IOException e) {
      model.put("dodavanjeVoznjaPoruka", "Doslo je do greske kod unosa voznje: " + e.getMessage());
    }
  }

  @PostConstruct
  private void ucitajKonfiguraciju() {
    Konfiguracija konfig;
    try {
      konfig = KonfiguracijaApstraktna
          .preuzmiKonfiguraciju(context.getRealPath("WEB-INF/konfiguracija.txt"));
      this.base_uri = konfig.dajPostavku("webservis.simulacije.baseuri");
    } catch (NeispravnaKonfiguracija e) {
    }
  }
}
