package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.poluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;

/**
 * Klasa PosluzitljRadara
 */
public class PosluziteljRadara {

  /** Podaci radara koji se posluzuje. */
  public PodaciRadara radar;

  /**
   * Regex predlozak za ERROR odgovor koji moze biti primljen kod registracije PosluziteljaRadara.
   */
  private Pattern predlozakError =
      Pattern.compile("^ERROR (?<errorNumber>\\d+) (?<errorTekst>.+$)");

  /** Matcher za prijasni Regex predlozak. */
  private Matcher poklapanjeError;

  /** Red svih redaka brz voznje. */
  public volatile Queue<BrzoVozilo> sviRetci = new ConcurrentLinkedQueue<>();

  /** Red svih kazna koje dretva temeljena na RadnikZaRadar koje PosluziteljRadara stvori. */
  public volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();


  /**
   * Main metoda provjerava ulazne argumente, preuzima postavke konfiguracije, registrira sebe i
   * pokrece dretvu temeljenu na klasi RadnikZaRadare
   *
   * @param args ulazni argumenti koji sadrze putanju do konfiguracijskoe datoteke
   */
  public static void main(String[] args) {
    if (args.length != 1 && args.length != 3) {
      System.out.println("Broj argumenata nije 1 ili 3.");
      return;
    }

    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    try {
      posluziteljRadara.preuzmiPostavke(args);

      if (!posluziteljRadara.registrirajPosluzitelja()) {
        return;
      }

      posluziteljRadara.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      e.printStackTrace();
      return;
    }
  }

  /**
   * Pokrece dretvu temeljenu na klasi RadnikZaRadare
   */
  public void pokreniPosluzitelja() {
    try (ServerSocket mreznaUticnicaZaRadar = new ServerSocket(this.radar.mreznaVrataRadara())) {
      Thread radarDretva = new Thread(new RadnikZaRadare(mreznaUticnicaZaRadar, this.radar, this));

      radarDretva.start();

      radarDretva.join();

    } catch (InterruptedException | IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * Klasa koja se koristi za parsiranje odgovora od PosluziteljaZaRegistracijuRadara
   *
   * @param errorNumber broj koji se koristi za idenifikaciju vrste errora
   * @param errorTekst tekst koji dodatno opisu error
   */
  private record ErrorPodaci(int errorNumber, String errorTekst) {
  }

  /**
   * Registrira PosluziteljRadara kod PosluziteljaZaRegistracijuRadara
   *
   * @return vraca ture ako se Posluzitelj uspije registrirati, false ako ne uspije
   */
  public boolean registrirajPosluzitelja() {
    try {
      String poruka = "RADAR " + this.radar.id() + " " + InetAddress.getLocalHost().toString() + " "
          + this.radar.mreznaVrataRadara() + " " + this.radar.gpsSirina() + " "
          + this.radar.gpsDuzina() + " " + this.radar.maksUdaljenost() + "\n";

      String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.radar.adresaRegistracije(),
          this.radar.mreznaVrataRegistracije(), poruka);
      this.poklapanjeError = this.predlozakError.matcher(odgovor);
      var statusError = poklapanjeError.matches();
      if (statusError) {
        var error = new ErrorPodaci(Integer.valueOf((this.poklapanjeError.group("errorNumber"))),
            this.poklapanjeError.group("errorTekst"));

        System.out.println("Primljen ERROR " + error.errorNumber + ": " + error.errorTekst);
        return false;
      }

      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }


  /**
   * Preuzima postavke za radar iz datoteke koja se nalazi na putanji koja je prosljedena
   *
   * @param args array stringova koji na nultom elementu mora sadrzavati putanju do datoteke koja
   *        sadrzi postavke za radar
   * @throws NeispravnaKonfiguracija the neispravna konfiguracija
   * @throws NumberFormatException the number format exception
   * @throws UnknownHostException the unknown host exception
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.radar = new PodaciRadara(Integer.parseInt(konfig.dajPostavku("id")),
        InetAddress.getLocalHost().getHostName(),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRadara")),
        Integer.parseInt(konfig.dajPostavku("maksBrzina")),
        Integer.parseInt(konfig.dajPostavku("maksTrajanje")),
        Integer.parseInt(konfig.dajPostavku("maksUdaljenost")),
        konfig.dajPostavku("adresaRegistracije"),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije")),
        konfig.dajPostavku("adresaKazne"), Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne")),
        konfig.dajPostavku("postanskaAdresaRadara"),
        Double.parseDouble(konfig.dajPostavku("gpsSirina")),
        Double.parseDouble(konfig.dajPostavku("gpsDuzina")));
  }
}
