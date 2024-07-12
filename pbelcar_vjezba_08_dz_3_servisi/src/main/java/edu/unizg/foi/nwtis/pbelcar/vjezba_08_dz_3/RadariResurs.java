package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3;

import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.RadariFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.RadariFacade.OdgovorLista;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.RadariFacade.OdgovorString;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/radari")
@RequestScoped
public class RadariResurs {

  @Inject
  RadariFacade radariFacade;
  
  @Inject
  private ServletContext context;
  
  String adresa;
  int vrata;
  
  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJson(@HeaderParam("Accept") String tipOdgovora) {
    OdgovorLista radari = this.radariFacade.vratiSveRadareList(this.adresa, this.vrata);

    if (radari == null) {
      // Radari su null samo ako se nemoze do posluzitelja ili se dobije error
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      return Response.status(Response.Status.OK).entity(radari).build();
    }
  }


  @GET
  @Path("/reset")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonReset(@HeaderParam("Accept") String tipOdgovora) {
    OdgovorString odg = this.radariFacade.vratiResetajRadate(this.adresa, this.vrata);
    if (odg == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      return Response.status(Response.Status.OK).entity(odg).build();
    }
  }


  @GET
  @Path("{id}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadarPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var odgovor = this.radariFacade.vratiSveRadareList(this.adresa, this.vrata);

    if (odgovor == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      if (odgovor.listaRadara != null)
        odgovor.listaRadara =
            odgovor.listaRadara.stream().filter(radar -> radar.getId() == id).toList();
      return Response.status(Response.Status.OK).entity(odgovor).build();
    }
  }


  @GET
  @Path("{id}/provjeri")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonRadarProvjerPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var odg = this.radariFacade.vratiProvjeraRadaraPoId(this.adresa, this.vrata, id);

    if (odg == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      return Response.status(Response.Status.OK).entity(odg).build();
    }
  }


  @DELETE
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonIzbrisiSveRadare(@HeaderParam("Accept") String tipOdgovora) {
    var odg = this.radariFacade.vratiIzbrisiSveRadare(this.adresa, this.vrata);

    if (odg == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      // Ovo ce uvijek vracati true neovisno o broju izbrisanih radara
      return Response.status(Response.Status.OK).entity(odg).build();
    }
  }


  @DELETE
  @Path("{id}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonIzbrisiRadarPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var odg = this.radariFacade.vratiIzbrisiRadarPoId(this.adresa, this.vrata, id);

    if (odg == null) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    } else {
      return Response.status(Response.Status.OK).entity(odg).build();
    }
  }

 @PostConstruct
  private void ucitajKonfiguraciju() {
    try {
      var konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(this.context.getRealPath("WEB-INF/konfiguracija.txt"));
      this.adresa = konfig.dajPostavku("app.radari.adresa");
      this.vrata = Integer.valueOf(konfig.dajPostavku("app.radari.mreznaVrata"));
    } catch (NeispravnaKonfiguracija e) {
    }
  }

}
