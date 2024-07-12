package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Radar;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

@RequestScoped
public class RadariFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    this.cb = this.em.getCriteriaBuilder();
  }
  
  // Nema interakciju sa bazom

  Pattern predlozakSviRadari = Pattern.compile("^OK .*$");
  Matcher poklapanjeSviRadari;

  Pattern predlozakRadarLista = Pattern.compile(
      "\\[(?<id>\\d+) (?<adresaRadara>\\S+) (?<mreznaVrataRadara>\\d+) (?<gpsSirina>\\d+\\.\\d+) (?<gpsDuzina>\\d+\\.\\d+) (?<maksUdaljenost>\\d+)\\]");
  Matcher poklapanjeRadarLista;

  Pattern predlozakReset =
      Pattern.compile("^OK (?<ukupanBrojRadara>\\d+) (?<brojNeaktivnih>\\d+)$");
  Matcher poklapanjeReset;

  Pattern predloazkRadarPoId = Pattern.compile("^OK$");
  Matcher poklapanjeRadarPoId;

  public RadariFacade() {
  }

  public class OdgovorLista {
    public List<Radar> getListaRadara() {
      return listaRadara;
    }

    public void setListaRadara(List<Radar> listaRadara) {
      this.listaRadara = listaRadara;
    }

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }

    public List<Radar> listaRadara;
    public String odgovor;

    public OdgovorLista(List<Radar> lista) {
      this.listaRadara = lista;
      this.odgovor = "OK";
    }

    public OdgovorLista(String odgovor) {
      this.odgovor = odgovor;
      this.listaRadara = null;
    }
  }

  public OdgovorLista vratiSveRadareList(String adresa, int vrata) {
    var odgovorPos = this.dohvatiSveRadareOfPosRegRadara(adresa, vrata);

    if (odgovorPos == null) {
      return null;
    }

    List<Radar> radarList = new ArrayList<>();

    this.poklapanjeRadarLista = this.predlozakRadarLista.matcher(odgovorPos);

    while (this.poklapanjeRadarLista.find()) {
      int id = Integer.parseInt(this.poklapanjeRadarLista.group("id"));
      String adresaRadara = this.poklapanjeRadarLista.group("adresaRadara");
      int mreznaVrataRadara =
          Integer.parseInt(this.poklapanjeRadarLista.group("mreznaVrataRadara"));
      double gpsSirina = Double.parseDouble(this.poklapanjeRadarLista.group("gpsSirina"));
      double gpsDuzina = Double.parseDouble(this.poklapanjeRadarLista.group("gpsDuzina"));
      int maksUdaljenost = Integer.parseInt(this.poklapanjeRadarLista.group("maksUdaljenost"));

      radarList.add(new Radar(id, adresaRadara, mreznaVrataRadara, 0, 0, maksUdaljenost, null, 0,
          null, 0, null, gpsSirina, gpsDuzina));
    }

    if (radarList.isEmpty()) {
      return new OdgovorLista("OK");
    } else {
      return new OdgovorLista(radarList);
    }
  }

  private String dohvatiSveRadareOfPosRegRadara(String adresa, int vrata) {
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "RADAR SVI\n");
    } catch (IOException e) {
      return null;
    }
  }


  public class OdgovorString {
    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }

    String odgovor;

    public OdgovorString(String odg) {
      this.odgovor = odg;
    }
  }

  public OdgovorString vratiResetajRadate(String adresa, int vrata) {
    var odgovorPos = this.dohvatiResetajRadare(adresa, vrata);
    if (odgovorPos == null) {
      return null;
    }

    return new OdgovorString(odgovorPos);
  }

  private String dohvatiResetajRadare(String adresa, int vrata) {
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "RADAR RESET\n");
    } catch (IOException e) {
      return null;
    }
  }


  public OdgovorString vratiProvjeraRadaraPoId(String adresan, int vrata, int id) {
    var odgovorPos = this.dohvatiProvjeraRadraPoId(adresan, vrata, id);
    if (odgovorPos == null) {
      return null;
    }

    return new OdgovorString(odgovorPos);
  }

  private String dohvatiProvjeraRadraPoId(String adresa, int vrata, int id) {
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "RADAR " + id + "\n");
    } catch (IOException e) {
      return null;
    }
  }


  public OdgovorString vratiIzbrisiRadarPoId(String adresa, int vrata, int id) {
    var odogovorPos = this.dohvatiIzbrisiRadarPoId(adresa, vrata, id);
    if (odogovorPos == null) {
      return null;
    }

    return new OdgovorString(odogovorPos);
  }

  private String dohvatiIzbrisiRadarPoId(String adresa, int vrata, int id) {
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "RADAR OBRIŠI " + id + "\n");
    } catch (IOException e) {
      return null;
    }
  }


  public OdgovorString vratiIzbrisiSveRadare(String adresa, int vrata) {
    var odgovorPos = this.dohvatiIzbrisiSveRadare(adresa, vrata);
    if (odgovorPos == null) {
      return null;
    }

    return new OdgovorString(odgovorPos);
  }

  private String dohvatiIzbrisiSveRadare(String adresa, int vrata) {
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, "RADAR OBRIŠI SVE\n");
    } catch (IOException e) {
      return null;
    }
  }
}
