package edu.unizg.foi.nwtis.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.Order;
import com.google.gson.Gson;

/**
 * Klasa KonfiguracijaApstraktnaTest.
 *
 * @author Dragutin Kermek
 */
@Order(5)
public class KonfiguracijaJsonTest extends KonfiguracijaApstraktnaTest {

  public KonfiguracijaJsonTest() {
    super();
    vrsta = KonfiguracijaJson.TIP;
  }

  public KonfiguracijaApstraktna dajObjekt(String nazivDatoteke) {
    return new KonfiguracijaJson(nazivDatoteke);
  }

  @Override
  public Properties ucitajKonfiguracijuPomocna(String nazivDatoteke)
      throws NeispravnaKonfiguracija {
    Properties p = new Properties();

    File f = new File(nazivDatoteke);

    if (f.exists() && f.isFile()) {
      try {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(f));
        p = gson.fromJson(br, Properties.class);
        br.close();
      } catch (IOException ex) {
        throw new NeispravnaKonfiguracija(
            "Problem kod učitavanja konfiguracije: '" + nazivDatoteke + "'");
      }

    } else {
      throw new NeispravnaKonfiguracija(
          "Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji ili nije datoteka!");
    }
    return p;
  }

  @Override
  public void spremiKonfiguracijuPomocna(Properties p, String nazivDatoteke)
      throws NeispravnaKonfiguracija {

    File f = new File(nazivDatoteke);

    if ((f.exists() && f.isFile()) || !f.exists()) {
      try {
        Gson gson = new Gson();
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        gson.toJson(p, bw);
        bw.close();
      } catch (IOException ex) {
        throw new NeispravnaKonfiguracija(
            "Problem kod učitavanja konfiguracije: '" + nazivDatoteke + "'");
      }

    } else {
      throw new NeispravnaKonfiguracija(
          "Datoteka pod nazivom: '" + nazivDatoteke + "' ne postoji ili nije datoteka!");
    }
  }


}
