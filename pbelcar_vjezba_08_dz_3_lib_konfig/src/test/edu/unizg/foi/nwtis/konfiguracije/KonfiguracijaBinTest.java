package edu.unizg.foi.nwtis.konfiguracije;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.junit.jupiter.api.Order;

/**
 * Klasa KonfiguracijaApstraktnaTest.
 *
 * @author Dragutin Kermek
 */
@Order(4)
public class KonfiguracijaBinTest extends KonfiguracijaApstraktnaTest {

  public KonfiguracijaBinTest() {
    super();
    vrsta = KonfiguracijaBin.TIP;
  }

  public KonfiguracijaApstraktna dajObjekt(String nazivDatoteke) {
    return new KonfiguracijaBin(nazivDatoteke);
  }

  @Override
  public Properties ucitajKonfiguracijuPomocna(String nazivDatoteke)
      throws NeispravnaKonfiguracija {
    Properties p = new Properties();

    File f = new File(nazivDatoteke);

    if (f.exists() && f.isFile()) {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
        Object o = ois.readObject();
        if (o instanceof Properties) {
          p = (Properties) o;
        }
        ois.close();
      } catch (Exception ex) {
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
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
        oos.writeObject(p);
        oos.close();
      } catch (IOException ex) {
        throw new NeispravnaKonfiguracija(
            "Problem kod učitavanja konfiguracije: '" + nazivDatoteke + "'");
      }
    }
  }
}
