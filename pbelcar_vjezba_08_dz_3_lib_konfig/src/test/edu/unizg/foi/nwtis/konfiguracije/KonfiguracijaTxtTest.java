package edu.unizg.foi.nwtis.konfiguracije;

import org.junit.jupiter.api.Order;

/**
 * Klasa KonfiguracijaApstraktnaTest.
 *
 * @author Dragutin Kermek
 */
@Order(2)
public class KonfiguracijaTxtTest extends KonfiguracijaApstraktnaTest {

  public KonfiguracijaTxtTest() {
    super();
    vrsta = KonfiguracijaTxt.TIP;
  }

  public KonfiguracijaApstraktna dajObjekt(String nazivDatoteke) {
    return new KonfiguracijaTxt(nazivDatoteke);
  }
}
