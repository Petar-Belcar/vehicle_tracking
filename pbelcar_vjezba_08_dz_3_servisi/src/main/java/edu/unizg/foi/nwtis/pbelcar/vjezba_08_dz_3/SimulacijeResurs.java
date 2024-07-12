package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3;

import java.util.List;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.SimulacijeFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/simulacije")
@RequestScoped
public class SimulacijeResurs {
  @Inject
  SimulacijeFacade simulacijeFacade;

  @Inject
  VozilaFacade vozilaFacade;

  @Inject
  ServletContext context;

  String adresa;
  int vrata;

  public class OdgovorLista {
    List<Vozilo> voznje;
    String odgovor;

    public List<Vozilo> getVoznje() {
      return voznje;
    }

    public void setVoznje(List<Vozilo> Voznje) {
      this.voznje = Voznje;
    }

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }

    public OdgovorLista(List<Vozilo> v) {
      this.voznje = v;
      this.odgovor = "OK";
    }

    public OdgovorLista(String o) {
      this.odgovor = o;
      this.voznje = null;
    }
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  public Response getJsonOdDo(@HeaderParam("Accept") String tipOdgovora,
      @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {
    if (vrijemeOd > 0 && vrijemeDo > 0)
      return Response.status(Response.Status.OK)
          .entity(new OdgovorLista(
              this.konvertiraj(this.simulacijeFacade.vratiVoznjeOdDo(vrijemeOd, vrijemeDo))))
          .build();
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
  }


  @GET
  @Path("vozilo/{id}")
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonVratiVoziloPoId(@HeaderParam("Accept") String tipOdgovora,
      @PathParam("id") int id, @QueryParam("od") long vrijemeOd, @QueryParam("do") long vrijemeDo) {
    if (vrijemeOd > 0 && vrijemeDo > 0) {
      var odg = this.simulacijeFacade.vratiVoznjePoId(id, vrijemeOd, vrijemeDo, this.vozilaFacade);
      if (odg != null) {
        return Response.status(Response.Status.OK)
            .entity(new OdgovorLista(this.konvertiraj(odg, id))).build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    } else {
      var odg = this.simulacijeFacade.vratiVoznjePoId(id, this.vozilaFacade);
      if (odg != null) {
        return Response.status(Response.Status.OK)
            .entity(new OdgovorLista(this.konvertiraj(odg, id))).build();
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    }
  }


  @POST
  @Transactional(TxType.REQUIRED)
  @Produces({MediaType.APPLICATION_JSON})
  public Response postJsonDodajVoznj(@HeaderParam("Accept") String tipOdgovora, Vozilo novaVoznja) {
    System.out.println("POST: Dodavanje nove voznje");
    String odg = null;
    if (novaVoznja != null)
      odg = this.simulacijeFacade.vratiDodavanjeVoznje(this.adresa, this.vrata, novaVoznja,
          this.vozilaFacade);

    if (odg == null)
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    else
      return Response.status(Response.Status.OK).entity(new OdgovorString(odg)).build();
  }

  public class OdgovorString {
    String odgovor;

    public OdgovorString(String o) {
      this.odgovor = o;
    }

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }
  }

  @PostConstruct
  private void ucitajKonfiguraciju() {
    try {
      var konfig = KonfiguracijaApstraktna
          .preuzmiKonfiguraciju(this.context.getRealPath("WEB-INF/konfiguracija.txt"));
      this.adresa = konfig.dajPostavku("app.vozila.adresa");
      this.vrata = Integer.valueOf(konfig.dajPostavku("app.vozila.mreznaVrata"));
    } catch (NeispravnaKonfiguracija e) {
    }
  }

  private List<Vozilo> konvertiraj(List<Pracenevoznje> voznje) {
    return voznje.parallelStream().map(v -> this.konvertiraj(v)).toList();
  }

  private List<Vozilo> konvertiraj(List<Pracenevoznje> voznje, int id) {
    return voznje.parallelStream().map(v -> this.konvertiraj(v, id)).toList();
  }

  public Vozilo konvertiraj(Pracenevoznje voznje) {
    return new Vozilo(voznje.getVozila().getVozilo(), voznje.getBroj(), voznje.getVrijeme(),
        voznje.getBrzina(), voznje.getSnaga(), voznje.getStruja(), voznje.getVisina(),
        voznje.getGpsbrzina(), voznje.getTempvozila(), voznje.getPostotakbaterija(),
        voznje.getNaponbaterija(), voznje.getKapacitetbaterija(), voznje.getTempbaterija(),
        voznje.getPreostalokm(), voznje.getUkupnokm(), voznje.getGpssirina(),
        voznje.getGpsduzina());
  }

  public Vozilo konvertiraj(Pracenevoznje voznje, int id) {
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
