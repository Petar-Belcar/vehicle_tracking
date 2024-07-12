package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.RedPodaciVozila;

/**
 * Klasa za CentralniSustav.
 */
public class CentralniSustav {

  /** Broju mreznih vrata za radar. */
  public int mreznaVrataRadara;

  /** Borju mreznih vrata za vozila. */
  public int mreznaVrataVozila;

  /** Maksimaln broj vozila u sutavu. */
  public int maksVozila;
  
  public String webServisVozilaBaseURI;

  /**
   * Tvornica dretva za dretve temenlejne na klasama PosluziteljZaRegistracijuRadra i
   * PosluziteljZaVozila.
   */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Ted koji ce sadrzavati podatke o svim radarima u sutavu. */
  public Map<Integer, PodaciRadara> sviRadari = new ConcurrentHashMap<Integer, PodaciRadara>();

  /** Red koji ce sadrzavati podatke o svim vozilima u sutavu. */
  public Map<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

  public Map<Integer, Boolean> vozilaSlatiNaRestful = new ConcurrentHashMap<>();

  /**
   * Main metoda Provjerava ulazne argumente, poziva preuzimanje preuzimanje postvka i pokretanje
   * posluzitelja.
   *
   * @param args prima jedan argument koji je putanja do datoteke koja ce se koristiti za postavke
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);

      centralniSustav.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Pokrece posluzitelje na temelju klasa PosluziteljZaRegistracijuRadara i PosluziteljZaVozila
   */
  public void pokreniPosluzitelja() {
    Thread posluziteljRadaraDretva = this.tvornicaDretvi
        .newThread(new PosluziteljZaRegistracijuRadara(this.mreznaVrataRadara, this));
    Thread posluzuteljVozilaDretva = this.tvornicaDretvi.newThread(new PosluziteljZaVozila(this));

    posluziteljRadaraDretva.start();
    posluzuteljVozilaDretva.start();

    try {
      posluziteljRadaraDretva.join();
      posluzuteljVozilaDretva.join();
    } catch (InterruptedException e) {
    }
  }

  /**
   * Preuzmi postavke koje se nalaze na putanji koja joj je prosljedena
   *
   * @param args array stringova koja na nultoj poziciji ima putanju do fila za konfiguraciju
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   * @throws UnknownHostException
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
    this.webServisVozilaBaseURI = konfig.dajPostavku("webservis.vozila.baseuri");
    
  }
}
