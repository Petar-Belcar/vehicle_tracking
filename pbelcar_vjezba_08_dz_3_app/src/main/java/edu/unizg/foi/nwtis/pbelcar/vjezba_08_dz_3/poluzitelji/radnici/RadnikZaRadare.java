package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.poluzitelji.radnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji.PosluziteljRadara;

/**
 * Klasa RadnikZaRadare
 */
public class RadnikZaRadare implements Runnable {

  /** Broj mrezne uticnine za radniak. */
  public ServerSocket mreznaUticnica;

  /** Podaci o radru kojeg radnik predstavlja. */
  public PodaciRadara radar;

  /** Referenca na posluzitelj radara koji je stvorio radnika. */
  public PosluziteljRadara posluziteljRadara;

  /** Regex predlozak za zahtjev za odredivanje brze voznje. */
  private Pattern predlozakVozilo = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>\\d+[.]\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  private Pattern predlozakReset = Pattern.compile("^RADAR RESET$");

  private Pattern predlozakId = Pattern.compile("^RADAR (?<id>\\d+)$");

  private Pattern predlozakOdgovoraPosRadaraOK = Pattern.compile("^OK$");

  private Pattern predlozakOdgovoraPosRadaraError12 = Pattern.compile("^ERROR 12 .*$");

  /** Matcher za prijasni Regex. */
  private Matcher poklapanjeVozilo;

  private Matcher poklapanjeReset;

  private Matcher poklapanjeId;

  private Matcher poklapanjeOdgPosRadaraOk;

  private Matcher poklapanjeOdgPosRadaraError12;


  /**
   * Konstruktor RadnikZaRadare
   *
   * @param mreznaUticnica broj mrezne uticnice za radnika
   * @param podac podaci radara kojeg radnik predstavlja
   * @param poslu referenca na PosluziteljRadara koji je stvorio radnika
   */
  public RadnikZaRadare(ServerSocket mreznaUticnica, PodaciRadara podac, PosluziteljRadara poslu) {
    this.mreznaUticnica = mreznaUticnica;
    this.radar = podac;
    this.posluziteljRadara = poslu;
  }

  /**
   * Metoda koja se poziva kada se na dretvi koja sadrzi objekt RadnikZaRadare pozove start()
   * metoda. Implementacija je posluzitelj.
   */
  public void run() {
    boolean kraj = false;

    try {
      while (!kraj) {
        var mreznaUticnica = this.mreznaUticnica.accept();
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
      e.printStackTrace();
    }
  }

  /**
   * Metoda koja poziva metode za obradu zahtjeva.
   *
   * @param zahtjev string koji predstvalj primjeni zahtjev
   * @return string koji predstavlja odgovor na primljeni zahtjev
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 30 Neispravna sintaksa komande.\n";
    }
    var odgovor = this.obradaVoznje(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaReset(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaId(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 30 Neispravna sintaksa komande.\n";
  }

  /**
   * Metoda za obradu zahtjeva koji moze biti zahtjev za evidentiranje brze voznje
   *
   * @param zahtjev string koji predstavlaj zahtjev
   * @return string koji predstvalja odgovor na zahtjev za evidentiranje brze voznje ili null ako
   *         nije
   */
  public String obradaVoznje(String zahtjev) {
    var voznja = parsiranjeVoznje(zahtjev);
    if (voznja != null) {
      List<BrzoVozilo> brzeVoznje = this.posluziteljRadara.sviRetci.stream()
          .filter(redak -> redak.status() == true && redak.id() == voznja.id())
          .collect(Collectors.toList());
      if (voznja.brzina() > this.radar.maksBrzina()
          && GpsUdaljenostBrzina.udaljenostKm(voznja.gpsSirina(), voznja.gpsDuzina(),
              this.radar.gpsSirina(), this.radar.gpsDuzina())
              * 1000 <= this.radar.maksUdaljenost()) {
        this.posluziteljRadara.sviRetci.add(voznja);
        return "OK\n";
      } // Ako je rijec o brzoj voznji

      if (GpsUdaljenostBrzina.udaljenostKm(voznja.gpsSirina(), voznja.gpsDuzina(),
          this.radar.gpsSirina(), this.radar.gpsDuzina()) * 1000 <= this.radar.maksUdaljenost()) {
        // Ako nije rijec o brzoj voznji - moguci prestanak brze voznje
        if (brzeVoznje.size() >= 2
            && (brzeVoznje.getLast().vrijeme() - brzeVoznje.get(0).vrijeme()) / 1000 > this.radar
                .maksTrajanje()
            && (brzeVoznje.getLast().vrijeme() - brzeVoznje.get(0).vrijeme()) / 1000 < 2
                * this.radar.maksTrajanje()) {
          // Prestanak brze voznje - "brisu" se podaci o brzoj voznji i salje se zahtjev za kaznu
          brzeVoznje.stream().forEach(redak -> redak.postaviStatus(false));
          return this.posaljiKaznu(voznja.id(), brzeVoznje.get(0).vrijeme(),
              brzeVoznje.getLast().vrijeme(), brzeVoznje.getLast().brzina(),
              brzeVoznje.getLast().gpsSirina(), brzeVoznje.getLast().gpsDuzina(),
              this.radar.gpsSirina(), this.radar.gpsDuzina()) + "\n";
        }
        // Nije rijec o prestanku brze voznje (prekratka ili duga) - "brisu se podaci o brzoj voznji
        brzeVoznje.stream().forEach(redak -> redak.postaviStatus(false));
        return "OK\n";
      }
      
      return "OK\n"; // Dodano jer se nest mora vracati ako se voznja parsisra
    }

    return null;
  }

  /**
   * Metoda za slanje zahtjeva za stvaranjem nove kaze kod PosluziteljaKazni
   *
   * @param id id vozila
   * @param vrijemePocetka vrijeme pocetka brze voznje
   * @param vrijemeKraja vrijeme kraja brze voznje
   * @param brzina brzina kojem se vozilo kretalo
   * @param gpsVozilaSirina gps sirina vozila
   * @param gpsVozilaDuzina gps duzina vozila
   * @param gpsRadaraSirina gps sirina radara
   * @param gpsRadaraDuzina gps duzina radara
   * @return string koji predstavlja odogovor od PosluziteljaKazni ili da posluzitelj nije aktivan
   */
  public String posaljiKaznu(int id, long vrijemePocetka, long vrijemeKraja, double brzina,
      double gpsVozilaSirina, double gpsVozilaDuzina, double gpsRadaraSirina,
      double gpsRadaraDuzina) {
    String kaznaKomanda = "VOZILO " + id + " " + vrijemePocetka + " " + vrijemeKraja + " " + brzina
        + " " + gpsVozilaSirina + " " + gpsVozilaDuzina + " " + gpsRadaraSirina + " "
        + gpsRadaraDuzina + "\n";
    try {
      return MrezneOperacije.posaljiZahtjevPosluzitelju(this.radar.adresaKazne(),
          this.radar.mreznaVrataKazne(), kaznaKomanda);
    } catch (NumberFormatException | IOException e) {
      return "ERROR 31 PosluziteljKazni nije aktivan";
    }
  }

  /**
   * Metoda za parsiranje zahtjeva brze voznje
   *
   * @param zahtjev string koji predstavlja zahtjev
   * @return podatak u kojem se nalaze prosljedeni podaci
   */
  public BrzoVozilo parsiranjeVoznje(String zahtjev) {
    this.poklapanjeVozilo = this.predlozakVozilo.matcher(zahtjev);
    var statusVozilo = this.poklapanjeVozilo.matches();
    if (statusVozilo) {
      var voziloZahtjev = new BrzoVozilo(Integer.valueOf(this.poklapanjeVozilo.group("id")),
          this.posluziteljRadara.sviRetci.size(),
          Long.valueOf(this.poklapanjeVozilo.group("vrijeme")),
          Double.valueOf(this.poklapanjeVozilo.group("brzina")),
          Double.valueOf(this.poklapanjeVozilo.group("gpsSirina")),
          Double.valueOf(this.poklapanjeVozilo.group("gpsDuzina")), true);

      return voziloZahtjev;
    }

    return null;
  }


  // Zad 2 Funkcionalnost 1
  private String obradaReset(String zah) {
    this.poklapanjeReset = this.predlozakReset.matcher(zah);
    var status = this.poklapanjeReset.matches();
    if (status) {
      try {
        String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.radar.adresaRegistracije(),
            this.radar.mreznaVrataRegistracije(), "RADAR " + this.radar.id() + "\n");
        this.poklapanjeOdgPosRadaraOk = this.predlozakOdgovoraPosRadaraOK.matcher(odgovor);
        var statusOK = this.poklapanjeOdgPosRadaraOk.matches();
        if (!statusOK) {
          this.poklapanjeOdgPosRadaraError12 =
              this.predlozakOdgovoraPosRadaraError12.matcher(odgovor);
          var statusError12 = this.poklapanjeOdgPosRadaraError12.matches();
          if (statusError12) {
            if (this.posluziteljRadara.registrirajPosluzitelja()) {
              return "OK";
            } else {
              return "ERROR ? neuspjesna registracija";
            }
          } else {
            return "ERROR ? neocekivana greska";
          }
        } else {
          return "OK";
        }
      } catch (IOException e) {
        return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan";
      }
    }
    return null;
  }


  // Zad 2 Funkcionalsnot 2
  private String obradaId(String zah) {
    this.poklapanjeId = this.predlozakId.matcher(zah);
    var status = this.poklapanjeId.matches();
    if (status) {
      int id = Integer.valueOf(this.poklapanjeId.group("id"));
      if (id == this.radar.id()) {
        try {
          String odg = MrezneOperacije.posaljiZahtjevPosluzitelju(this.radar.adresaKazne(),
              this.radar.mreznaVrataKazne(), "TEST\n");
          this.poklapanjeOdgPosRadaraOk = this.predlozakOdgovoraPosRadaraOK.matcher(odg);
          if (this.poklapanjeOdgPosRadaraOk.matches()) {
            return "OK";
          } else {
            return "ERROR ? neocekivana greska";
          }
        } catch (IOException e) {
          return "ERROR 34 PosluziteljKazni nije aktivan";
        }
      } else {
        return "ERROR 33 prosljedeni identifikator nije jednak identifikatoru radara";
      }
    }
    return null;
  }
}


