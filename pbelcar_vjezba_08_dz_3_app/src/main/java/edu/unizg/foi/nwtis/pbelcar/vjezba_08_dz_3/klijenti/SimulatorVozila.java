package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.klijenti;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.PodaciVozila;

public class SimulatorVozila {
  private Pattern predlozakRed =
      Pattern.compile("^(?<vrijeme>\\d+),(?<brzina>-?\\d+([.]\\d+)?),(?<snaga>-?\\d+([.]\\d+)?)"
          + ",(?<struja>-?\\d+([.]\\d+)?),(?<visina>-?\\d+([.]\\d+)?),(?<gpsBrzina>-?\\d+([.]\\d+)?),(?<tempVozila>\\d+)"
          + ",(?<postotakBaterije>\\d+),(?<naponBaterije>-?\\d+([.]\\d+)?),(?<kapacitetBaterija>\\d+),(?<tempBaterija>\\d+)"
          + ",(?<preostaloKm>-?\\d+([.]\\d+)?),(?<ukupnoKm>-?\\d+([.]\\d+)?),(?<gpsSirina>-?\\d+([.]\\d+)?)"
          + ",(?<gpsDuzina>-?\\d+([.]\\d+)?)$");
  private Matcher poklapanjeRed;

  private String adresaVozila;
  private int mreznaVrataVozila;
  private int trajanjeSek;
  private int trajanjePauze;

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Broj ulaza mora biti jednak 3");
      return;
    }

    var sim = new SimulatorVozila();
    try {
      sim.preuzetiKonfiguraciju(args[0]);
    } catch (NeispravnaKonfiguracija e) {
      return;
    }

    sim.citatiFileRedPoRed(args[1], Integer.valueOf(args[2]));

  }

  private void citatiFileRedPoRed(String filePath, int id) {
    InetSocketAddress adresa = new InetSocketAddress(this.adresaVozila, this.mreznaVrataVozila);
    try {
      AsynchronousSocketChannel socket = AsynchronousSocketChannel.open();
      socket.connect(adresa);

      try (BufferedReader citac = new BufferedReader(new FileReader(filePath))) {
        int brojRetka = 0;
        String red;
        while ((red = citac.readLine()) != null) {
          this.poslatiZahtjev(red, id, brojRetka++, socket);
        }
        socket.close();
      } catch (IOException e) {
      }
    } catch (Exception e) {
      return;
    }
  }



  private boolean poslatiZahtjev(String red, int id, int brojRetka,
      AsynchronousSocketChannel socket) {
    try {
      // Ocito krivo ali kak je tak je
      Thread.currentThread().sleep(this.trajanjeSek);
    } catch (InterruptedException e) {
    }

    var podaciVozila = this.parsiratiRed(red, id, brojRetka);
    if (podaciVozila != null) {
      var zahtjev = this.formiratiZahtjev(podaciVozila);
      ByteBuffer buffer = ByteBuffer.wrap(zahtjev.getBytes(StandardCharsets.UTF_8));
      try {
        socket.write(buffer).get();
      } catch (Exception e) {

      }
    }

    try {
      Thread.currentThread().sleep(this.trajanjePauze);
    } catch (InterruptedException e) {
    }

    return false;
  }


  private PodaciVozila parsiratiRed(String red, int idVozila, int brojReda) {
    this.poklapanjeRed = this.predlozakRed.matcher(red);
    var statusRed = this.poklapanjeRed.matches();
    if (statusRed) {
      return new PodaciVozila(idVozila, brojReda, Long.valueOf(this.poklapanjeRed.group("vrijeme")),
          Double.valueOf(this.poklapanjeRed.group("brzina")),
          Double.valueOf(this.poklapanjeRed.group("snaga")),
          Double.valueOf(this.poklapanjeRed.group("struja")),
          Double.valueOf(this.poklapanjeRed.group("visina")),
          Double.valueOf(this.poklapanjeRed.group("gpsBrzina")),
          Integer.valueOf(this.poklapanjeRed.group("tempVozila")),
          Integer.valueOf(this.poklapanjeRed.group("postotakBaterije")),
          Double.valueOf(this.poklapanjeRed.group("naponBaterije")),
          Integer.valueOf(this.poklapanjeRed.group("kapacitetBaterija")),
          Integer.valueOf(this.poklapanjeRed.group("tempBaterija")),
          Double.valueOf(this.poklapanjeRed.group("preostaloKm")),
          Double.valueOf(this.poklapanjeRed.group("ukupnoKm")),
          Double.valueOf(this.poklapanjeRed.group("gpsSirina")),
          Double.valueOf(this.poklapanjeRed.group("gpsDuzina")));
    }
    return null;
  }


  private void preuzetiKonfiguraciju(String filePath) throws NeispravnaKonfiguracija {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(filePath);

    this.adresaVozila = konfig.dajPostavku("adresaVozila");
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.trajanjeSek = Integer.valueOf(konfig.dajPostavku("trajanjeSek"));
    this.trajanjePauze = Integer.valueOf(konfig.dajPostavku("trajanjePauze"));
  }


  private String formiratiZahtjev(PodaciVozila pV) {
    return String.format("VOZILO %d %d %d %f %f %f %f %f %d %d %f %d %d %f %f %f %f\n", pV.id(),
        pV.broj(), pV.vrijeme(), pV.brzina(), pV.snaga(), pV.struja(), pV.visina(), pV.gpsBrzina(),
        pV.tempVozila(), pV.postotakBaterija(), pV.naponBaterija(), pV.kapacitetBaterija(),
        pV.tempBaterija(), pV.preostaloKm(), pV.ukupnoKm(), pV.gpsSirina(), pV.gpsDuzina());
  }
}
