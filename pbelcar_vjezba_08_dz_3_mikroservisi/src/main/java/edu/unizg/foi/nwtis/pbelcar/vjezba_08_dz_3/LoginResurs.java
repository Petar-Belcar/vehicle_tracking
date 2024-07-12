package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.DnevnikRada;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Korisnici;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.DnevnikRadaFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.GrupeFacade;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.KorisniciFacade;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("nwtis/v1/api/login")
@RequestScoped
public class LoginResurs {
  @Inject
  GrupeFacade gF;

  @Inject
  KorisniciFacade kF;

  @Inject
  DnevnikRadaFacade drF;

  @POST
  @Transactional(TxType.REQUIRED)
  @Produces({MediaType.APPLICATION_JSON})
  public Response postAuthAdmin(@HeaderParam("Accept") String tipOdgovora,
      @FormParam("korIme") String korIme, @FormParam("lozinka") String lozinka) {
    var serverProps = System.getProperties();
    List<Korisnici> k = this.kF.dohvatiSveKorisnikeSaKorImeILoz(korIme, lozinka);
    if (k.size() == 1) {
      var korisnik = k.getFirst();
      var g = this.gF.dohvatiSveGrupeKorisnika(korisnik.getKorisnik(), this.kF).parallelStream()
          .map(grupa -> grupa.getGrupa()).toList();

      // Vjerojatno je ok da je hardkodirano za zadacu, ali bi trebalo iz konfig citati uloge i
      // ubaciti ih dinamicki
      if (g.contains("admin")) {
        this.kreirajNoviDnevnikrada("", "", korIme, serverProps.getProperty("os.name"),
            "Uspjesna autentikacija", serverProps.getProperty("java-version"));


        return Response.status(Response.Status.ACCEPTED).build();
      }
    }
    this.kreirajNoviDnevnikrada("", "", korIme, serverProps.getProperty("os.name"),
        "Neuspjesna autentikacija", serverProps.getProperty("java-version"));
    return Response.status(Response.Status.FORBIDDEN).build();
  }

  private DnevnikRada kreirajNoviDnevnikrada(String adresaracunala, String ipadresaracunala,
      String korisnickoime, String nazivos, String opisrada, String verzijavm) {
    DnevnikRada dnevnikRada = new DnevnikRada();
    dnevnikRada.setAdresaracunala(adresaracunala);
    dnevnikRada.setIpadresaracunala(ipadresaracunala);
    dnevnikRada.setKorisnickoime(korisnickoime);
    dnevnikRada.setNazivos(nazivos);
    dnevnikRada.setOpisrada(opisrada);
    dnevnikRada.setVerzijavm(verzijavm);
    dnevnikRada.setVrijeme(Timestamp.from(Instant.now()));

    this.drF.create(dnevnikRada);
    return dnevnikRada;
  }
}
