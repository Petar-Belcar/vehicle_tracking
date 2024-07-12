package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;

/**
 * Klasa PosluziteljZaRegistracijuRadara
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {
  // TODO: Add functionality for zad 2

  /** Mrezna vrata za posluziteljska mrezna vrata. */
  private int mreznaVrata;

  /** Objekt CentralniSustav. */
  private CentralniSustav centralniSustav;

  /** Predlozak za zahtjev za registraciju radara */
  private Pattern predlozakRegistracijaRadara = Pattern.compile(
      "^RADAR (?<id>\\d+) (?<adresa>[^\\s]+) (?<mreznaVrata>\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>\\d+?)$");

  /** Predlozak za zahtjev za brisanje registriranog radara prema idu. */
  private Pattern predlozakBrirsanjeRadaraPoIdu = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");

  /** Predlozak za zahtjev za brisanje svih registriranih radara. */
  private Pattern predlozakBrisanjeSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE$");

  private Pattern predlozakProvjereRadaraPoId = Pattern.compile("^RADAR (?<id>\\d+)$");

  private Pattern predlozakResetanjeRadara = Pattern.compile("^RADAR RESET$");

  private Pattern predlozakDohvacanjaSvihRadara = Pattern.compile("^RADAR SVI$");

  /** Matcer za registraciju radara. */
  private Matcher poklapanjeRegistracije;

  /** Matcher za brisanje radara prema idu. */
  private Matcher poklapanjeBrisanjeRadaraPoIdu;

  /** Matcher za brisanje svih radara. */
  private Matcher poklapanjeBrisanjeSvihRadara;

  private Matcher poklapanjeProvjereRadaraPoId;

  private Matcher poklapanjeResetanjeRadra;

  private Matcher poklapanjeDOhvacanjaSvihRadara;

  /**
   * Konstruktor za PosluziteljZaRegistracijuRadara
   *
   * @param mreznaVrata broj meznih vrata PosluziteljaZaRegistracijuRadara
   * @param centralniSustav objekt CentraliSustav
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }


  /**
   * Metoda iz sucelja Runnable koja je potrebna kako bi PosluziteljZaRegistracijuRadara mogao
   * raditi u zasebnoj dretvi
   */
  public void run() {
    this.pokreniPosluzitelja();
  }


  /**
   * Pokrece PosluziteljaZaRegistracijuRadara
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        BufferedReader citac =
            new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
        OutputStream out = mreznaUticnica.getOutputStream();
        PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
        var redak = citac.readLine();

        mreznaUticnica.shutdownInput();
        pisac.println(obradaZahtjeva(redak));

        pisac.flush();
        mreznaUticnica.shutdownOutput();
        mreznaUticnica.close();
      }
    } catch (NumberFormatException | IOException e) {
    }
  }


  /**
   * Metoda za obradu zahtjeva
   *
   * @param zahtjev string koji presdstavlja zahtjev
   * @return string koji predstavlja odgovor na zahtjev
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaRegistracijeRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaZahtjevaBrisanjeRadaraPoIdu(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaZahtjevaBrisanjaSvihRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obardaZahtjevaProvjereRadaraPoId(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obardaZahtjevaResetanjaRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaZahtjevaDohvataSvihRadara(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.";
  }



  /**
   * Obrada zahtjeva registracije radara.
   *
   * @param zahtjev string koji predstavlja zahtjev koji moze biti zahtjev za registraciju radara
   * @return string koji predstavlja odgovor na zahtjev za regisraciju ili null ako nije rijec o
   *         zahtjevu za registraciju
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    this.poklapanjeRegistracije = this.predlozakRegistracijaRadara.matcher(zahtjev);
    var statusRadara = poklapanjeRegistracije.matches();
    if (statusRadara) {
      var adresa = this.poklapanjeRegistracije.group("adresa");
      System.out.println("Addresa radara: " + adresa);
      // Iz nekog razloga kad je pocel dobivati adresu u obliku "<base_16>/<ipv4>"
      // Ovo ga ispravi
      var ip = adresa.split("/")[1];
      var radar = new PodaciRadara(Integer.valueOf(this.poklapanjeRegistracije.group("id")),
          ip,
          Integer.valueOf(this.poklapanjeRegistracije.group("mreznaVrata")), -1, -1,
          Integer.valueOf(this.poklapanjeRegistracije.group("maksUdaljenost")), null, -1, null, -1,
          null, Double.valueOf(this.poklapanjeRegistracije.group("gpsSirina")),
          Double.valueOf(this.poklapanjeRegistracije.group("gpsDuzina")));

      if (this.centralniSustav.sviRadari.containsKey(radar.id())) {
        return "ERROR 12 radar za cija je identifikacija " + radar.id() + " vec postoji";
      }
      this.centralniSustav.sviRadari.put(radar.id(), radar);

      return "OK";
    }
    return null;
  }


  /**
   * Klasa koja se koristi za parsiranje zahtjeva brisanja radara po idu
   *
   * @param id id od radra koji se zeli obrisati
   */
  private record BirsanjeRadaraPoIduPodaci(int id) {
  }

  /**
   * Obrada zahtjeva brisanje radara po idu.
   *
   * @param zahtjev strign koji predstavlja zahtjev koji moze biti zahtjev za brisanje radara po idu
   * @return string koji predstavlja odgovor na zahtjev za brisanje radara po idu ili null ako nije
   *         rijec o zahtjevu za brisanje radara po idu
   */
  public String obradaZahtjevaBrisanjeRadaraPoIdu(String zahtjev) {
    this.poklapanjeBrisanjeRadaraPoIdu = this.predlozakBrirsanjeRadaraPoIdu.matcher(zahtjev);
    var statusBrisanjaRadaraPoIdu = this.poklapanjeBrisanjeRadaraPoIdu.matches();
    if (statusBrisanjaRadaraPoIdu) {
      var birsanjeRadaraPoIdu = new BirsanjeRadaraPoIduPodaci(
          Integer.valueOf(this.poklapanjeBrisanjeRadaraPoIdu.group("id")));

      if (!this.centralniSustav.sviRadari.containsKey(birsanjeRadaraPoIdu.id())) {
        return "ERROR 12 radar za cija je identifikacija " + birsanjeRadaraPoIdu.id()
            + " ne postoji";
      }
      this.centralniSustav.sviRadari.remove(birsanjeRadaraPoIdu.id());
      return "OK";
    }
    return null;
  }


  /**
   * Obrada zahtjeva brisanja svih radara.
   *
   * @param zahtjev string koji predstavlja zahtjev koji moze biti zahtjev za brisanjem svih radara
   * @return string koji predstavlja odgovor na zahtjev za brisanjem svih radara ili null ako nije
   *         rijec o zahtjevu za brisanjem svih radara
   */
  public String obradaZahtjevaBrisanjaSvihRadara(String zahtjev) {
    this.poklapanjeBrisanjeSvihRadara = this.predlozakBrisanjeSvihRadara.matcher(zahtjev);
    var statusBrisanjeSvihRadara = this.poklapanjeBrisanjeSvihRadara.matches();
    if (statusBrisanjeSvihRadara) {
      this.centralniSustav.sviRadari.clear();
      return "OK";
    }

    return null;
  }


  // Zad 2 Funkcionalnost 1

  public String obardaZahtjevaProvjereRadaraPoId(String zah) {
    this.poklapanjeProvjereRadaraPoId = this.predlozakProvjereRadaraPoId.matcher(zah);
    var stat = this.poklapanjeProvjereRadaraPoId.matches();
    if (stat) {
      int id = Integer.valueOf(this.poklapanjeProvjereRadaraPoId.group("id"));
      return this.provjeraPostojanjaRadaraUKolekciji(id) ? "OK"
          : "ERROR 12 radar za cija je identifikacija " + id + " ne postoji";
    }
    return null;
  }

  public boolean provjeraPostojanjaRadaraUKolekciji(int id) {
    return this.centralniSustav.sviRadari.containsKey(id);
  }

  // Zad 2 Funkcionalnost 2

  public String obardaZahtjevaResetanjaRadara(String zah) {
    this.poklapanjeResetanjeRadra = this.predlozakResetanjeRadara.matcher(zah);
    var status = this.poklapanjeResetanjeRadra.matches();
    int radariPronadeni = 0;
    int radariIzbrisani = 0;
    if (status) {
      for (var radar : this.centralniSustav.sviRadari.values()) {
        radariPronadeni++;
        try {
          MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
              radar.mreznaVrataRadara(), "RADRA \n");
        } catch (IOException e) {
          radariIzbrisani++;
          this.centralniSustav.sviRadari.remove(radar.id());
        }
      }

      return "OK " + radariPronadeni + " " + radariIzbrisani;
    }

    return null;
  }


  // Zad 2 Funkcionalnost 3

  public String obradaZahtjevaDohvataSvihRadara(String zah) {
    this.poklapanjeDOhvacanjaSvihRadara = this.predlozakDohvacanjaSvihRadara.matcher(zah);
    var status = this.poklapanjeDOhvacanjaSvihRadara.matches();
    if (status) {
      StringBuilder odg = new StringBuilder("OK {");
      boolean first = true;

      for (var radar : this.centralniSustav.sviRadari.values()) {
        if (!first) {
          odg.append(", ");
        } else {
          first = false;
        }
        odg.append("[");
        odg.append(radar.id());
        odg.append(" ");
        odg.append(radar.adresaRadara());
        odg.append(" ");
        odg.append(radar.mreznaVrataRadara());
        odg.append(" ");
        odg.append(radar.gpsSirina());
        odg.append(" ");
        odg.append(radar.gpsDuzina());
        odg.append(" ");
        odg.append(radar.maksUdaljenost());
        odg.append("]");
      }
      odg.append("}");

      return odg.toString();
    }
    return null;
  }

}


