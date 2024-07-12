/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.kontroler;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Kazna;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author NWTiS
 */
@Controller
@Path("kazne")
@RequestScoped
public class KazneKontroler {

  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @Inject
  private ServletContext context;

  private String base_uri;

  @GET
  @Path("ispisKazni")
  @View("kazne.jsp")
  public void json() {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON(this.base_uri);
    model.put("kazne", kazne);
  }

  @GET
  @Path("kazneOdDoForm")
  @View("kazneOdDo.jsp")
  public void json_OdDo() {}

  @GET
  @Path("kaznePoRB")
  @View("kaznePoRB.jsp")
  public void json_PoRB() {}

  @GET
  @Path("kaznePoVoziloID")
  @View("kaznePoVoziloID.jsp")
  public void json_PoID() {}

  @GET
  @Path("kaznePoVoziloIDOdDo")
  @View("kaznePoVoziloIDOdDo.jsp")
  public void json_PoIDOdDo() {}

  @GET
  @Path("kazneTest")
  @View("kazneTest.jsp")
  public void json_Test() {
    RestKlijentKazne k = new RestKlijentKazne();
    if (k.getKaznaJSON_Test(this.base_uri)) {
      model.put("test", "PosluziteljKazni je aktivan");
    } else {
      model.put("test", "PosluziteljKazni nije aktivan");
    }
  }


  @POST
  @Path("ispisKazniOdDo")
  @View("kazne.jsp")
  public void json_pi(@FormParam("odVremena") long odVremena,
      @FormParam("doVremena") long doVremena) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_od_do(odVremena, doVremena, this.base_uri);
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazneRB")
  @View("kazne.jsp")
  public void json_rb(@FormParam("redniBroj") String rb) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = new ArrayList<Kazna>();
    Kazna kazna = k.getKaznaJSON_rb(rb, this.base_uri);
    if (kazna != null)
      kazne.add(k.getKaznaJSON_rb(rb, this.base_uri));
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazneVoziloID")
  @View("kazne.jsp")
  public void json_id(@FormParam("id") String id) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo(id, this.base_uri);
    model.put("kazne", kazne);
  }

  @POST
  @Path("ispisKazneVoziloIDOdDo")
  @View("kazne.jsp")
  public void json_idOdDo(@FormParam("id") String id, @FormParam("od") long vrijemeOd,
      @FormParam("do") long vrijemeDo) {
    RestKlijentKazne k = new RestKlijentKazne();
    List<Kazna> kazne = k.getKazneJSON_vozilo_od_do(id, vrijemeOd, vrijemeDo, this.base_uri);
    model.put("kazne", kazne);
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
