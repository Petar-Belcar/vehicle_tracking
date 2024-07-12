package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.poluzitelji.radnici;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji.CentralniSustav;

public class RadnikZaVozilo implements Runnable {
  private Pattern predlozakKomanda = Pattern.compile(
      "VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<snaga>-?\\d+([.]\\d+)?)"
          + " (?<struja>-?\\d+([.]\\d+)?) (?<visina>-?\\d+([.]\\d+)?) (?<gpsBrzina>-?\\d+([.]\\d+)?) (?<tempVozila>\\d+)"
          + " (?<postotakBaterije>\\d+) (?<naponBaterije>-?\\d+([.]\\d+)?) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+)"
          + " (?<preostaloKm>-?\\d+([.]\\d+)?) (?<ukupnoKm>-?\\d+([.]\\d+)?) (?<gpsSirina>-?\\d+([.]\\d+)?)"
          + " (?<gpsDuzina>-?\\d+([.]\\d+)?)");
  private Pattern predlozakOdgovorOK = Pattern.compile("^OK$");
  private Pattern predlozakOdgovorError =
      Pattern.compile("^ERROR (?<errorBroj>\\d+) (?<errorTekst>.+$)");

  private Pattern predlozadStart = Pattern.compile("^VOZILO START (?<id>\\d+)$");
  private Matcher poklapanjeStart;

  private Pattern predlozadStop = Pattern.compile("^VOZILO STOP (?<id>\\d+)$");
  private Matcher poklapanjeStop;

  private Matcher poklapanjeKomanda;
  private Matcher poklapanjeOdgovorOK;
  private Matcher poklapanjeOdgovorError;

  private AsynchronousSocketChannel kanal;
  private CentralniSustav centralniSustav;

  public RadnikZaVozilo(AsynchronousSocketChannel kanal, CentralniSustav cs) {
    this.kanal = kanal;
    this.centralniSustav = cs;
  }

  public void run() {
    try {
      while (this.kanal.isOpen()) {
        if (this.kanal != null) {
          var buffer = ByteBuffer.allocate(2048);
          Future<Integer> citajBuffer = this.kanal.read(buffer);
          citajBuffer.get();

          var zahtjev = new String(buffer.array()).trim();
          if (zahtjev == "") {
            this.kanal.close();
          }
          var podaciVozila = this.parsirajZahtjev(zahtjev);
          if (podaciVozila != null) {
            this.obradiZahtjev(podaciVozila);
          }

          Integer startStop = this.parsirajStart(zahtjev);
          if (startStop != null) {
            this.obradiStart(startStop);
            this.kanal.close();
            return;
          }
          startStop = this.parsirajStop(zahtjev);
          if (startStop != null) {
            this.obradiStop(startStop);
            this.kanal.close();
            return;
          }

          buffer.clear();
          buffer.flip();
        }
      }
    } catch (Exception e) {
    }
  }


  private void obradiZahtjev(PodaciVozila podaciVozila) {
    if (this.centralniSustav.vozilaSlatiNaRestful.keySet().contains(podaciVozila.id())) {
      boolean status = this.poslatiREST(podaciVozila);

      if (status) {
        for (var radar : this.centralniSustav.sviRadari.values()) {
          this.posaljiZahtjevPosluziteljuRadara(radar, podaciVozila);
        }
      }

      return;
    }
    for (var radar : this.centralniSustav.sviRadari.values()) {
      this.posaljiZahtjevPosluziteljuRadara(radar, podaciVozila);
    }
  }


  private void posaljiZahtjevPosluziteljuRadara(PodaciRadara pR, PodaciVozila pV) {
    String zahtjev = "VOZILO " + pV.id() + " " + pV.vrijeme() + " " + pV.brzina() + " "
        + pV.gpsSirina() + " " + pV.gpsDuzina() + "\n";

    this.posaljiRadaru(pR, pV, zahtjev);
  }

  private boolean poslatiREST(PodaciVozila pV) {
    HttpClient httpKlijent = HttpClient.newHttpClient();
    try {
      HttpRequest httpZahtjev = HttpRequest.newBuilder()
          .uri(new URI(this.centralniSustav.webServisVozilaBaseURI + "nwtis/v1/api/vozila"))
          .POST(HttpRequest.BodyPublishers.ofString(this.pretoviPodaciVozilaUJSON(pV)))
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


  private String pretoviPodaciVozilaUJSON(PodaciVozila podaciVozila) {
    StringBuilder jsonBuilder = new StringBuilder("{");
    jsonBuilder.append("\"id\": ").append(podaciVozila.id()).append(",");
    jsonBuilder.append("\"broj\": ").append(podaciVozila.broj()).append(",");
    jsonBuilder.append("\"vrijeme\": ").append(podaciVozila.vrijeme()).append(",");
    jsonBuilder.append("\"brzina\": ").append(podaciVozila.brzina()).append(",");
    jsonBuilder.append("\"snaga\": ").append(podaciVozila.snaga()).append(",");
    jsonBuilder.append("\"struja\": ").append(podaciVozila.struja()).append(",");
    jsonBuilder.append("\"visina\": ").append(podaciVozila.visina()).append(",");
    jsonBuilder.append("\"gpsBrzina\": ").append(podaciVozila.gpsBrzina()).append(",");
    jsonBuilder.append("\"tempVozila\": ").append(podaciVozila.tempVozila()).append(",");
    jsonBuilder.append("\"postotakBaterija\": ").append(podaciVozila.postotakBaterija())
        .append(",");
    jsonBuilder.append("\"naponBaterija\": ").append(podaciVozila.naponBaterija()).append(",");
    jsonBuilder.append("\"kapacitetBaterija\": ").append(podaciVozila.kapacitetBaterija())
        .append(",");
    jsonBuilder.append("\"tempBaterija\": ").append(podaciVozila.tempBaterija()).append(",");
    jsonBuilder.append("\"preostaloKm\": ").append(podaciVozila.preostaloKm()).append(",");
    jsonBuilder.append("\"ukupnoKm\": ").append(podaciVozila.ukupnoKm()).append(",");
    jsonBuilder.append("\"gpsSirina\": ").append(podaciVozila.gpsSirina()).append(",");
    jsonBuilder.append("\"gpsDuzina\": ").append(podaciVozila.gpsDuzina());
    jsonBuilder.append("}");

    return jsonBuilder.toString();
  }

  private void posaljiRadaru(PodaciRadara pR, PodaciVozila pV, String zahtjev) {
    try {
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(pR.adresaRadara(),
          pR.mreznaVrataRadara(), zahtjev);
      if (!this.obradiOdgovorOK(odgovor)) {
        Error error = this.obradiOdgovorError(odgovor);
        if (error != null) {
          System.out.println(error.errorBroj + ":" + error.errorTekst);
        }
      }
    } catch (IOException e) {
      System.out.println("Greska kod slanja radaru na adresi: " + pR.adresaRadara() + ":"
          + pR.mreznaVrataRadara());
    }
  }

  private boolean obradiOdgovorOK(String odg) {
    this.poklapanjeOdgovorOK = this.predlozakOdgovorOK.matcher(odg);
    var statusOdgovorOK = this.poklapanjeOdgovorOK.matches();
    if (statusOdgovorOK) {
      return true;
    }
    return false;
  }


  private record Error(int errorBroj, String errorTekst) {
  }

  private Error obradiOdgovorError(String odg) {
    this.poklapanjeOdgovorError = this.predlozakOdgovorError.matcher(odg);
    var statusError = this.poklapanjeOdgovorError.matches();
    if (statusError) {
      return new Error(Integer.valueOf(this.poklapanjeOdgovorError.group("errorBroj")),
          this.poklapanjeOdgovorError.group("errorTekst"));
    }
    return null;
  }


  private PodaciVozila parsirajZahtjev(String zahtjev) {
    this.poklapanjeKomanda = this.predlozakKomanda.matcher(zahtjev);
    var statusKomande = this.poklapanjeKomanda.matches();
    if (statusKomande) {
      PodaciVozila noviPodaci =
          new PodaciVozila(Integer.valueOf(this.poklapanjeKomanda.group("id")),
              Integer.valueOf(this.poklapanjeKomanda.group("broj")),
              Long.valueOf(this.poklapanjeKomanda.group("vrijeme")),
              Double.valueOf(this.poklapanjeKomanda.group("brzina")),
              Double.valueOf(this.poklapanjeKomanda.group("snaga")),
              Double.valueOf(this.poklapanjeKomanda.group("struja")),
              Double.valueOf(this.poklapanjeKomanda.group("visina")),
              Double.valueOf(this.poklapanjeKomanda.group("gpsBrzina")),
              Integer.valueOf(this.poklapanjeKomanda.group("tempVozila")),
              Integer.valueOf(this.poklapanjeKomanda.group("postotakBaterije")),
              Double.valueOf(this.poklapanjeKomanda.group("naponBaterije")),
              Integer.valueOf(this.poklapanjeKomanda.group("kapacitetBaterija")),
              Integer.valueOf(this.poklapanjeKomanda.group("tempBaterija")),
              Double.valueOf(this.poklapanjeKomanda.group("preostaloKm")),
              Double.valueOf(this.poklapanjeKomanda.group("ukupnoKm")),
              Double.valueOf(this.poklapanjeKomanda.group("gpsSirina")),
              Double.valueOf(this.poklapanjeKomanda.group("gpsDuzina")));

      return noviPodaci;
    }

    return null;
  }


  // Zad 2 Funkcionalnost 1
  private Integer parsirajStart(String zah) {
    this.poklapanjeStart = this.predlozadStart.matcher(zah);
    var status = this.poklapanjeStart.matches();
    if (status) {
      return Integer.valueOf(this.poklapanjeStart.group("id"));
    }
    return null;
  }

  private void obradiStart(Integer odg) {
    if (!this.centralniSustav.vozilaSlatiNaRestful.keySet().contains(odg)) {
      this.centralniSustav.vozilaSlatiNaRestful.put(odg, true);
    }
    ByteBuffer buf = ByteBuffer.wrap("OK\n".getBytes(StandardCharsets.UTF_8));
    this.kanal.write(buf);
  }


  // Zad 2 Funkcionalnost 2
  private Integer parsirajStop(String zah) {
    this.poklapanjeStop = this.predlozadStop.matcher(zah);
    var status = this.poklapanjeStop.matches();
    if (status) {
      return Integer.valueOf(this.poklapanjeStop.group("id"));
    }
    return null;
  }

  private void obradiStop(Integer odg) {
    if (this.centralniSustav.vozilaSlatiNaRestful.keySet().contains(odg)) {
      this.centralniSustav.vozilaSlatiNaRestful.remove(odg);
    }
    ByteBuffer buf = ByteBuffer.wrap("OK\n".getBytes(StandardCharsets.UTF_8));
    this.kanal.write(buf);
  }
}


