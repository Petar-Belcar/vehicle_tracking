package edu.unizg.foi.nwtis.konfiguracije;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.Order;

/**
 * Klasa KonfiguracijaApstraktnaTest.
 *
 * @author Dragutin Kermek
 */
@Order(3)
public class KonfiguracijaXmlTest extends KonfiguracijaApstraktnaTest {

  public KonfiguracijaXmlTest() {
    super();
    vrsta = KonfiguracijaXml.TIP;
  }

  public KonfiguracijaApstraktna dajObjekt(String nazivDatoteke) {
    return new KonfiguracijaXml(nazivDatoteke);
  }

  @Override
  public Properties ucitajKonfiguracijuPomocna(String nazivDatoteke)
      throws NeispravnaKonfiguracija {
    Properties p = new Properties();

    File f = new File(nazivDatoteke);

    if (f.exists() && f.isFile()) {
      try {
        p.loadFromXML(new FileInputStream(f));
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
        p.storeToXML(new FileOutputStream(f), "");
      } catch (IOException ex) {
        throw new NeispravnaKonfiguracija(
            "Problem kod učitavanja konfiguracije: '" + nazivDatoteke + "'");
      }
    }
  }

}
