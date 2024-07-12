package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciKazne;

/**
 * Klasa PosluziteljKazni.
 */
public class PosluziteljKazni {

  /** Podatak o formatiranju vremena. */
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

  /** Broj mreznih vrata posluzitelja kazni. */
  public int mreznaVrata;

  public String base_uri;

  /** Regex predlozak za zahtjev stvaranja kazne. */
  private Pattern predlozakKazna = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");

  /**
   * Regex predlozak za zahtjev dohvacanja najsvjezije kazne u prosljedenom vremenskom intervalu.
   */
  private Pattern predlozakZadnjaKaznaURazdoblju =
      Pattern.compile("VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+)$");

  /**
   * Regex predlozak za zahtjev dohvacanja broja kazni po vozilu u prosljedenom vremenskom
   * intervalu.
   */
  private Pattern predlozakStatistikaKazna =
      Pattern.compile("STATISTIKA (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+)$");

  private Pattern predlozakTest = Pattern.compile("^TEST$");

  /** Matcher za Regex predlozak za upisivanje nove kazne. */
  private Matcher poklapanjeKazna;

  /** Matcher za Regex predlozak za dohvacanje najsvjezije kazne u vremenskom intervalu. */
  private Matcher poklapanjeZadnjaKazna;

  /** Matcher za Regex predlozak za dohvacanje broja kazni po vozilu u vremenskom intervalu. */
  private Matcher poklapanjeStatistika;

  private Matcher poklapanjeTest;

  /** Red koji sadrzi sve kaze koje je posluzitelj generirao. */
  public volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();


  /**
   * Main metoda koja provjerava ulazne argumente, poziva preuzimanje postavki i pokrece
   * posluzitelja kazna
   *
   * @param args array stringova koji bi trebao sadrzavati putanju do datoteke konfiguracije
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
    try {
      posluziteljKazni.preuzmiPostavke(args);

      posluziteljKazni.pokreniPosluzitelja();

    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Metoda kokja pokrece posluzitelj kazni
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
      e.printStackTrace();
    }
  }

  /**
   * Metoda za obradu primljenih zahtjeva
   *
   * @param zahtjev string koji predstavlja zahtjev
   * @return string koji predstavlja odgovor na zahtjev
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 10 Neispravna sintaksa komande.";
    }
    var odgovor = obradaZahtjevaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaZahtjevaZadnjaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = obradaStatistikaKazna(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }
    odgovor = this.obradaTest(zahtjev);
    if (odgovor != null) {
      return odgovor;
    }

    return "ERROR 10 Neispravna sintaksa komande.";
  }


  /**
   * Metoda za obrdau zajtjeva unosenja nove kazne
   *
   * @param zahtjev string koji predstavlja zahtjev koji moze biti zahtjev unosenja nove kazne
   * @return string koji prestavlja odgovor na zahtjev unosenja novoe kazne ili null ako nije rijec
   *         o zahtjevu unosa nove kazne
   */
  public String obradaZahtjevaKazna(String zahtjev) {
    this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
    var statusKazna = poklapanjeKazna.matches();
    if (statusKazna) {
      var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
          Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
          Double.valueOf(this.poklapanjeKazna.group("brzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
          Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
          Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

      this.sveKazne.add(kazna);

      if (!this.posaljiKaznuREST(kazna)) {
        return "ERROR 42 slanje kazne e-vozila nije uspjesno obavljeno";
      }
      return "OK\n";
    }
    return null;
  }


  private boolean posaljiKaznuREST(PodaciKazne kazna) {
    HttpClient httpKlijent = HttpClient.newHttpClient();
    try {
      HttpRequest httpZahtjev =
          HttpRequest.newBuilder().uri(new URI(this.base_uri + "nwtis/v1/api/kazne"))
              .POST(HttpRequest.BodyPublishers.ofString(this.pretvoriPodaciKazneUJSON(kazna)))
              .header("Content-Type", "application/json").build();

      httpKlijent.send(httpZahtjev, HttpResponse.BodyHandlers.ofString());

      return true;
    } catch (URISyntaxException e) {
      return false;
    } catch (IOException e) {
      return false;
    } catch (InterruptedException e) {
      return false;
    }
  }

  private String pretvoriPodaciKazneUJSON(PodaciKazne kazna) {
    StringBuilder jsonBuilder = new StringBuilder("{");
    jsonBuilder.append("\"id\": \"").append(kazna.id()).append("\", ");
    jsonBuilder.append("\"vrijemePocetak\": \"").append(kazna.vrijemePocetak()).append("\", ");
    jsonBuilder.append("\"vrijemeKraj\": \"").append(kazna.vrijemeKraj()).append("\", ");
    jsonBuilder.append("\"brzina\": \"").append(kazna.brzina()).append("\", ");
    jsonBuilder.append("\"gpsSirina\": \"").append(kazna.gpsSirina()).append("\", ");
    jsonBuilder.append("\"gpsDuzina\": \"").append(kazna.gpsDuzina()).append("\", ");
    jsonBuilder.append("\"gpsSirinaRadar\": \"").append(kazna.gpsSirinaRadar()).append("\", ");
    jsonBuilder.append("\"gpsDuzinaRadar\": \"").append(kazna.gpsDuzinaRadar()).append("\"");
    jsonBuilder.append("}");
    return jsonBuilder.toString();
  }


  /**
   * Klasa koja sluzi za parsiranje podatak zahtjeva za najsvjezoj kazni u danom vremenskom
   * intervalu
   *
   * @param id id vozila
   * @param vrijemeOd vrijeme od
   * @param vrijemeDo vrijeme do
   */
  // Looks done
  private record ZadnjaKaznaPodaci(int id, long vrijemeOd, long vrijemeDo) {
  };

  /**
   * Metoda za obradu zahtjeva koji moze biti zahtjev za dohvacanjem najsvjezije kazne u danom
   * vremenskom intervalu
   *
   * @param zahtjev string koji predstavlja zahtjev koji moze biti zahtjev za dohvacanjem
   *        najsvjezije kazne u danom vremenskom intervalu
   * @return string koji predstvalj odgovor na zahtjev za dohvacanjem najsvjezije kazne u danom
   *         vrmeneskom intervalu ili null ako nije
   */
  public String obradaZahtjevaZadnjaKazna(String zahtjev) {
    this.poklapanjeZadnjaKazna = this.predlozakZadnjaKaznaURazdoblju.matcher(zahtjev);
    var statusZadnjaKazna = poklapanjeZadnjaKazna.matches();
    if (statusZadnjaKazna) {
      var zadnjaKaznaUlaz =
          new ZadnjaKaznaPodaci(Integer.valueOf(this.poklapanjeZadnjaKazna.group("id")),
              Long.valueOf(this.poklapanjeZadnjaKazna.group("vrijemePocetak")),
              Long.valueOf(this.poklapanjeZadnjaKazna.group("vrijemeKraj")));

      try {
        PodaciKazne zadnjaKazna = this.sveKazne.stream()
            .filter(kazna -> kazna.id() == zadnjaKaznaUlaz.id()
                && kazna.vrijemeKraj() <= zadnjaKaznaUlaz.vrijemeDo())
            .collect(Collectors.toList()).getLast();
        return "OK " + zadnjaKazna.vrijemeKraj() + " " + zadnjaKazna.brzina() + " "
            + zadnjaKazna.gpsSirinaRadar() + " " + zadnjaKazna.gpsDuzinaRadar() + "\n";
      } catch (Exception e) {
        return "ERROR 41 vozilo nema evidentirane kazne u danom razdoblju";
      }
    }
    return null;
  }


  /**
   * Klasa koja sluzi za parsiranje podtaka kod zahtjeva za dohvacanjem broja kazni po vozilu u
   * danom vremenskom intervalu
   *
   * @param vrijemeOd vrijeme od
   * @param vrijemeDo vrijeme do
   */
  private record StatistikaPodaci(long vrijemeOd, long vrijemeDo) {
  };

  /**
   * Metoda za obradu zahtjeva koji mogu biti zahtjevi za dohvacanjem broja kazni po vozilu u danom
   * vremenskom intervalu
   *
   * @param zahtjev string koji predstavlja zahtjev koji moze biti zahtjeva za dohvacanjem kazni po
   *        vozilu u danom vremenskom intervalu
   * @return string koji predstavlja odgovor na zahtjev broja vozila po vozilu u danom vremenskom
   *         intervalu ili null ako nije
   */
  public String obradaStatistikaKazna(String zahtjev) {
    this.poklapanjeStatistika = this.predlozakStatistikaKazna.matcher(zahtjev);
    var statusStatistika = poklapanjeStatistika.matches();
    if (statusStatistika) {
      var statistikaUlaz =
          new StatistikaPodaci(Long.valueOf(this.poklapanjeStatistika.group("vrijemePocetak")),
              Long.valueOf(this.poklapanjeStatistika.group("vrijemeKraj")));

      Map<Integer, Integer> kaznaMap = new ConcurrentHashMap<Integer, Integer>();
      this.sveKazne.stream().filter(kazna -> kazna.vrijemeKraj() >= statistikaUlaz.vrijemeOd()
          && kazna.vrijemeKraj() <= statistikaUlaz.vrijemeDo()).forEach(kazna -> {
            if (kaznaMap.containsKey(kazna.id())) {
              kaznaMap.put(kazna.id(), kaznaMap.get(kazna.id()) + 1);
            } else {
              kaznaMap.put(kazna.id(), 1);
            }
          });

      final StringBuilder returnStringBuilder = new StringBuilder();
      kaznaMap.forEach(
          (key, value) -> returnStringBuilder.append(key).append(" ").append(value).append("; "));
      String returnString = returnStringBuilder.toString();
      if (returnString.isBlank()) {
        return "ERROR 49 nema kazni u zadanom vremenu";
      }
      return "OK " + returnString;
    }
    return null;
  }


  /**
   * Metoda za preuzimanje postavki.
   *
   * @param args arrays atringova koji na nultoj poziciji mora imati putanju do konfiguracijske
   *        datoteke
   * @throws NeispravnaKonfiguracija
   * @throws NumberFormatException
   * @throws UnknownHostException
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
    this.base_uri = konfig.dajPostavku("webservis.kazne.baseuri");
  }


  // Zad 2 Funckonalnost 1
  private String obradaTest(String zah) {
    this.poklapanjeTest = this.predlozakTest.matcher(zah);
    if (this.poklapanjeTest.matches()) {
      return "OK";
    }
    return null;
  }
}
