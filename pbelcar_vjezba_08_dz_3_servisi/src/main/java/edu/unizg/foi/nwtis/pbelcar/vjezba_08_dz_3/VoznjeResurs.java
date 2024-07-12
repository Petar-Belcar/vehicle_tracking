/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3;

import java.io.IOException;
import java.util.List;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.VoznjeFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 * @author Dragutin Kermek
 */
@Path("nwtis/v1/api/vozila")
@RequestScoped
public class VoznjeResurs {
  @Inject
  VoznjeFacade voznjeFacade;

  @Inject
  VozilaFacade vozilaFacade;

  @Inject
  ServletContext context;

  String adresa;
  int vrata;

  public class OdgovorLista {
    List<Vozilo> Voznje;
    String odgovor;

    public List<Vozilo> getVoznje() {
      return Voznje;
    }

    public void setVoznje(List<Vozilo> Voznje) {
      this.Voznje = Voznje;
    }

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }

    public OdgovorLista(List<Vozilo> v) {
      this.Voznje = v;
      this.odgovor = "OK";
    }

    public OdgovorLista(String o) {
      this.odgovor = o;
      this.Voznje = null;
    }
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonVozilaOdDo(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {
    List<Voznje> vozila = this.voznjeFacade.vratiVoznjeOdDo(vrijemeOd, vrijemeDo);
    if (vozila != null) {
      return Response.status(Response.Status.OK).entity(new OdgovorLista(this.konvertiraj(vozila)))
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }


  @GET
  @Path("vozilo/{id}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonVratiVoziloPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {
    if (vrijemeOd > 0 && vrijemeDo > 0) {
      var odg = this.voznjeFacade.vratiVoznjePoId(id, vrijemeOd, vrijemeDo, this.vozilaFacade);
      if (odg != null) {
        return Response.status(Response.Status.OK)
            .entity(new OdgovorLista(this.konvertiraj(odg, id))).build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    } else {
      var odg = this.voznjeFacade.vratiVoznjePoId(id, this.vozilaFacade);
      if (odg != null) {
        return Response.status(Response.Status.OK)
            .entity(new OdgovorLista(this.konvertiraj(odg, id))).build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    }
  }


  @GET
  @Path("vozilo/{id}/start")
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonStartPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var odg = vratitiPokretanjePracenja(this.adresa, this.vrata, id);
    if (odg == null)
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    else
      return Response.status(Response.Status.OK).entity(odg).build();
  }


  @GET
  @Path("vozilo/{id}/stop")
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonStopPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id) {
    var odg = vratitZaustavljanjePracenja(this.adresa, this.vrata, id);
    if (odg == null)
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    else
      return Response.status(Response.Status.OK).entity(odg).build();
  }

  @POST // TODO: Nest ne valja tu
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonDodajVoznj(@HeaderParam("Accept") String tipOdgovora, PodaciVozila novaVoznja) {
    System.out.println("POST: Dodavanje voznje");
    Boolean odg = null;
    if (novaVoznja != null) {
      Voznje voznja = this.konvertiraj(novaVoznja);
      odg = this.voznjeFacade.vratiDodavanjeVoznje(voznja);
    } else
      System.out.println("ERROR: Vozilo je null");
    if (odg == null || odg == false)
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    else
      return Response.status(Response.Status.OK)
          .entity(new OdgovorString("Nova voznja uspjesno dodana")).build();
  }

  @PostConstruct
  private void ucitajKonfiguraciju() {
    try {
      var konfig = KonfiguracijaApstraktna
          .preuzmiKonfiguraciju(context.getRealPath("WEB-INF/konfiguracija.txt"));
      this.adresa = konfig.dajPostavku("app.vozila.adresa");
      this.vrata = Integer.valueOf(konfig.dajPostavku("app.vozila.mreznaVrata"));
    } catch (Exception e) {
    }

    System.out.println(adresa + '/' + vrata);
  }

  public class OdgovorString {
    String odgovor;

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }

    public OdgovorString(String o) {
      this.odgovor = o;
    }
  }

  public OdgovorString vratitiPokretanjePracenja(String adresa, int vrata, int id) {
    try {
      String msg = "VOZILO START " + id + "\n";
      System.out.println(msg);
      
      String odg =
          MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, msg);
      return new OdgovorString(odg);
    } catch (IOException e) {
      return null;
    }
  }

  public OdgovorString vratitZaustavljanjePracenja(String adresa, int vrata, int id) {
    try {
      String odg =
          MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "VOZILO STOP " + id + "\n");
      return new OdgovorString(odg);
    } catch (IOException e) {
      return null;
    }
  }

  // TODO: Transfer these changes to servisi

  private List<Vozilo> konvertiraj(List<Voznje> voznje) {
    return voznje.parallelStream().map(v -> this.konvertiraj(v)).toList();
  }

  private List<Vozilo> konvertiraj(List<Voznje> voznje, int id) {
    return voznje.parallelStream().map(v -> this.konvertiraj(v, id)).toList();
  }

  public Vozilo konvertiraj(Voznje voznje) {
    return new Vozilo(voznje.getVozila().getVozilo(), voznje.getBroj(), voznje.getVrijeme(),
        voznje.getBrzina(), voznje.getSnaga(), voznje.getStruja(), voznje.getVisina(),
        voznje.getGpsbrzina(), voznje.getTempvozila(), voznje.getPostotakbaterija(),
        voznje.getNaponbaterija(), voznje.getKapacitetbaterija(), voznje.getTempbaterija(),
        voznje.getPreostalokm(), voznje.getUkupnokm(), voznje.getGpssirina(),
        voznje.getGpsduzina());
  }

  public Vozilo konvertiraj(Voznje voznje, int id) {
    return new Vozilo(id, voznje.getBroj(), voznje.getVrijeme(), voznje.getBrzina(),
        voznje.getSnaga(), voznje.getStruja(), voznje.getVisina(), voznje.getGpsbrzina(),
        voznje.getTempvozila(), voznje.getPostotakbaterija(), voznje.getNaponbaterija(),
        voznje.getKapacitetbaterija(), voznje.getTempbaterija(), voznje.getPreostalokm(),
        voznje.getUkupnokm(), voznje.getGpssirina(), voznje.getGpsduzina());
  }

  public Voznje konvertiraj(PodaciVozila podaci) {
    Voznje voznje = new Voznje();
    voznje.setVozila(this.vozilaFacade.find(podaci.id()));
    voznje.setBroj(podaci.broj());
    voznje.setVrijeme(podaci.vrijeme());
    voznje.setBrzina(podaci.brzina());
    voznje.setSnaga(podaci.snaga());
    voznje.setStruja(podaci.struja());
    voznje.setVisina(podaci.visina());
    voznje.setGpsbrzina(podaci.gpsBrzina());
    voznje.setTempvozila(podaci.tempVozila());
    voznje.setPostotakbaterija(podaci.postotakBaterija());
    voznje.setNaponbaterija(podaci.naponBaterija());
    voznje.setKapacitetbaterija(podaci.kapacitetBaterija());
    voznje.setTempbaterija(podaci.tempBaterija());
    voznje.setPreostalokm(podaci.preostaloKm());
    voznje.setUkupnokm(podaci.ukupnoKm());
    voznje.setGpssirina(podaci.gpsSirina());
    voznje.setGpsduzina(podaci.gpsDuzina());

    return voznje;
  }

}
