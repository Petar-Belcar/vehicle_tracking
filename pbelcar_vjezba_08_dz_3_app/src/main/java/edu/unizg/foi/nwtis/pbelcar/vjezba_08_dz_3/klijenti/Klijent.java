package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.klijenti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.OptionalInt;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa klijent
 */
public class Klijent {

  /** Mrezna vrata PosluziteljaKazni. */
  private int mreznaVrataKazne;

  /** Adresa PosluziteljaKazni. */
  private String adresaKazne;

  /** Id vozila. */
  private OptionalInt id;

  /** Vrijeme od. */
  private long vrijemeOd;

  /** Vrijeme do. */
  private long vrijemeDo;

  /**
   * Main metoda koja inicijalizira klijenta, preuzima arugmente i postavke te podnosi zahtjev prema
   * PosluziteljuKazni i stavlja njegov odgovor u STDOUT
   *
   * @param args array stringova koja predstavlja ulazu u program, ovisno o broju argumenta tip
   *        zahtjeva se mjenja
   */
  public static void main(String[] args) {
    Klijent klijent = new Klijent();
    if (!klijent.preuzmiArgumente(args)) {
      System.out.println("Problem u ulaznim argumentima");
      return;
    }

    try {
      klijent.preuzmiPostavke(args[0]);
    } catch (NeispravnaKonfiguracija | NumberFormatException e) {
      e.printStackTrace();
    }

    System.out.println(klijent.napravitiZahtjev());
  }


  /**
   * Metoda za preuzimanje postavka sa putanje
   *
   * @param string koji predstavlja putanju do konfiguracijske datoteke
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   */
  private void preuzmiPostavke(String putanja)
      throws NeispravnaKonfiguracija, NumberFormatException {
    var konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja);

    this.mreznaVrataKazne = Integer.valueOf(konfiguracija.dajPostavku("mreznaVrataKazne"));
    this.adresaKazne = konfiguracija.dajPostavku("adresaKazne");
  }


  /**
   * Metoda za preuzimanje vrijednosti iz argumenta
   *
   * @param args array strigova koje prima main metoda
   * @return ako se argumenti uspiju parsirati vraca true, ako ne false
   */
  private boolean preuzmiArgumente(String[] args) {
    if (args.length == 3) {
      this.id = OptionalInt.empty();
      this.vrijemeOd = Long.valueOf(args[1]);
      this.vrijemeDo = Long.valueOf(args[2]);
    } else if (args.length == 4) {
      this.id = OptionalInt.of(Integer.valueOf(args[1]));
      this.vrijemeOd = Long.valueOf(args[2]);
      this.vrijemeDo = Long.valueOf(args[3]);
    } else {
      return false;
    }

    return true;
  }


  /**
   * Metoda za podnoselje zahtjeva prema PosluziteljuKazni
   *
   * @return string koji predstavlja odgovor od Posluzitelja kazni ili poruku o gresci
   */
  private String napravitiZahtjev() {
    var komanda = this.generirajKomandu();

    try (Socket prikljucak = new Socket(this.adresaKazne, this.mreznaVrataKazne)) {
      OutputStream out = prikljucak.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      BufferedReader citac =
          new BufferedReader(new InputStreamReader(prikljucak.getInputStream(), "utf8"));

      pisac.println(komanda);
      prikljucak.shutdownOutput();

      var odgovor = citac.readLine();
      prikljucak.shutdownInput();

      return odgovor;
    } catch (IOException e) {
      return "Greska kod spajanja na PosluziteljKazni";
    }

  }

  /**
   * Metoda koja generira komandu iz parsiranih vrijednosti
   *
   * @return komanda
   */
  private String generirajKomandu() {
    if (this.id.isPresent()) {
      return "VOZILO " + this.id.getAsInt() + " " + this.vrijemeOd + " " + this.vrijemeDo;
    } else {
      return "STATISTIKA " + this.vrijemeOd + " " + this.vrijemeDo;
    }
  }
}
