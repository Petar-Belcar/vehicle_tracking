package edu.unizg.foi.nwtis.konfiguracije;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * Klasa KonfiguracijaApstraktnaTest.
 *
 * @author Dragutin Kermek
 */
@Order(1)
public class KonfiguracijaApstraktnaTest {

  String vrsta = ".txt";
  String nazivDatoteke_1 = "konfiguracijaTest_1.";
  String nazivDatoteke_2 = "konfiguracijaTest_2.";
  String nazivDatoteke_3 = "konfiguracijaTest_3.";
  String nazivDatoteke_4 = "konfiguracijaTest_4.";

  public KonfiguracijaApstraktnaTest() {
    super();
  }

  @BeforeAll
  public static void setUpClass() {}

  @AfterAll
  public static void tearDownClass() {}

  @BeforeEach
  public void setUp() {
    this.nazivDatoteke_1 += this.vrsta;
    this.nazivDatoteke_2 += this.vrsta;
    this.nazivDatoteke_3 += this.vrsta;
    this.nazivDatoteke_4 += this.vrsta;

    obrisiSveDatoteke();
  }

  @AfterEach
  public void tearDown() {
    obrisiSveDatoteke();
  }

  public KonfiguracijaApstraktna dajObjekt(String nazivDatoteke) {
    return new KonfiguracijaApstraktnaImpl(nazivDatoteke);
  }

  @Test
  public void testDajSvePostavke() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);
    Properties p1 = ka1.dajSvePostavke();

    Properties p2 = new Properties();

    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka3.postavke = p3;

    Properties p4 = ka3.dajSvePostavke();

    Properties p5 = new Properties();
    p5.setProperty("k1", "pero");
    p5.setProperty("k2", "mato");
    p5.setProperty("k3", "ivo");
    p5.setProperty("k4", "luka");

    assertEquals(p5, p4);
  }

  @Test
  public void testObrisiSvePostavke() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    boolean b1 = ka1.obrisiSvePostavke();

    assertFalse(b1);
    assertEquals(0, ka1.postavke.size());

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    boolean b2 = ka2.obrisiSvePostavke();

    assertTrue(b2);
    assertEquals(0, ka2.postavke.size());
  }

  @Test
  public void testDajPostavku() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    String s1 = ka1.dajPostavku("k1");

    assertNull(s1);

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    String s2 = ka2.dajPostavku("k1");

    assertNotNull(s2);
    assertEquals("pero", s2);
  }

  @Test
  public void testDajPostavkuOsnovno() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    String s1 = ka1.dajPostavkuOsnovno("k1", "pero");

    assertNotNull(s1);
    assertEquals("pero", s1);

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    String s2 = ka2.dajPostavkuOsnovno("k2", "");

    assertNotNull(s2);
    assertEquals("mato", s2);
  }

  @Test
  public void testAzurirajPostavku() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    boolean b1 = ka1.azurirajPostavku("k1", "pero");

    assertFalse(b1);
    assertEquals(0, ka1.postavke.size());

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    boolean b2 = ka2.azurirajPostavku("k5", "goga");

    assertFalse(b2);
    assertEquals(4, ka2.postavke.size());

    boolean b3 = ka2.azurirajPostavku("k1", "roko");

    assertTrue(b3);
    assertEquals(4, ka2.postavke.size());
    assertEquals("roko", ka2.postavke.get("k1"));
  }

  @Test
  public void testPostojiPostavka() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    boolean b1 = ka1.postojiPostavka("k1");

    assertFalse(b1);
    assertEquals(0, ka1.postavke.size());

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    boolean b2 = ka2.postojiPostavka("k5");

    assertFalse(b2);
    assertEquals(4, ka2.postavke.size());

    boolean b3 = ka2.postojiPostavka("k1");

    assertTrue(b3);
  }

  @Test
  public void testObrisiPostavku() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    boolean b1 = ka1.obrisiPostavku("k1");

    assertFalse(b1);
    assertEquals(0, ka1.postavke.size());

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    boolean b2 = ka2.obrisiPostavku("k5");

    assertFalse(b2);
    assertEquals(4, ka2.postavke.size());

    boolean b3 = ka2.obrisiPostavku("k1");

    assertTrue(b3);
    assertEquals(3, ka2.postavke.size());
    assertNull(ka2.postavke.get("k1"));
  }

  @Test
  public void testSpremiPostavku() {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    boolean b1 = ka1.spremiPostavku("k1", "pero");

    assertTrue(b1);
    assertEquals(1, ka1.postavke.size());
    assertEquals("pero", ka1.postavke.get("k1"));

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p2 = new Properties();
    p2.setProperty("k1", "pero");
    p2.setProperty("k2", "mato");
    p2.setProperty("k3", "ivo");
    p2.setProperty("k4", "luka");
    ka2.postavke = p2;

    boolean b2 = ka2.spremiPostavku("k5", "goga");

    assertTrue(b2);
    assertEquals(5, ka2.postavke.size());
    assertEquals("goga", ka2.postavke.get("k5"));

    boolean b3 = ka2.spremiPostavku("k1", "roko");

    assertFalse(b3);
    assertEquals(5, ka2.postavke.size());
    assertEquals("pero", ka2.postavke.get("k1"));
  }

  @Test
  public void testSpremiKonfiguraciju() throws Exception {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);
    ka1.spremiKonfiguraciju();

    KonfiguracijaApstraktna ka2 = this.dajObjekt(ka1.nazivDatoteke);
    Properties p1 = this.ucitajKonfiguracijuPomocna(ka2.nazivDatoteke);

    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka3.postavke = p3;

    ka3.spremiKonfiguraciju();

    Properties p4 = new Properties();
    p4.setProperty("k1", "pero");
    p4.setProperty("k2", "mato");
    p4.setProperty("k3", "ivo");
    p4.setProperty("k4", "luka");

    KonfiguracijaApstraktna ka4 = this.dajObjekt(ka3.nazivDatoteke);
    Properties p5 = ucitajKonfiguracijuPomocna(ka4.nazivDatoteke);

    assertEquals(p4.size(), p5.size());
    assertEquals(p4, p5);
  }

  @Test
  public void testSpremiKonfiguracijuSNazivomDatoteke() throws Exception {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);
    ka1.spremiKonfiguraciju(this.nazivDatoteke_2);

    Properties p1 = this.ucitajKonfiguracijuPomocna(this.nazivDatoteke_2);

    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka3.postavke = p3;

    ka3.spremiKonfiguraciju(this.nazivDatoteke_4);

    Properties p4 = new Properties();
    p4.setProperty("k1", "pero");
    p4.setProperty("k2", "mato");
    p4.setProperty("k3", "ivo");
    p4.setProperty("k4", "luka");

    Properties p5 = ucitajKonfiguracijuPomocna(this.nazivDatoteke_4);

    assertEquals(p4.size(), p5.size());
    assertEquals(p4, p5);
  }

  @Test
  public void testUcitajKonfiguraciju() throws Exception {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);
    this.spremiKonfiguracijuPomocna(ka1.postavke, ka1.nazivDatoteke);

    KonfiguracijaApstraktna ka2 = this.dajObjekt(ka1.nazivDatoteke);
    ka2.ucitajKonfiguraciju();

    Properties p1 = ka2.postavke;
    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka3.postavke = p3;

    this.spremiKonfiguracijuPomocna(ka3.postavke, ka3.nazivDatoteke);

    Properties p4 = new Properties();
    p4.setProperty("k1", "pero");
    p4.setProperty("k2", "mato");
    p4.setProperty("k3", "ivo");
    p4.setProperty("k4", "luka");

    KonfiguracijaApstraktna ka4 = this.dajObjekt(ka3.nazivDatoteke);
    ka4.ucitajKonfiguraciju();

    assertEquals(p4.size(), ka4.postavke.size());
    assertEquals(p4, ka4.postavke);
  }

  @Test
  public void testKreirajKonfiguraciju() throws Exception {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);

    Konfiguracija ka2 = KonfiguracijaApstraktna.kreirajKonfiguraciju(ka1.nazivDatoteke);

    Properties p1 = ka2.dajSvePostavke();
    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Konfiguracija ka4 = KonfiguracijaApstraktna.kreirajKonfiguraciju(ka3.nazivDatoteke);
    Properties p3 = ka4.dajSvePostavke();

    Properties p4 = new Properties();

    assertEquals(p4.size(), p3.size());
    assertEquals(p4, p3);

    assertThrows(NeispravnaKonfiguracija.class,
        () -> KonfiguracijaApstraktna.kreirajKonfiguraciju("pero.abc"));
  }

  @Test
  public void testPreuzmiKonfiguraciju() throws Exception {
    KonfiguracijaApstraktna ka1 = this.dajObjekt(this.nazivDatoteke_1);
    this.spremiKonfiguracijuPomocna(ka1.postavke, ka1.nazivDatoteke);

    Konfiguracija ka2 = KonfiguracijaApstraktna.preuzmiKonfiguraciju(ka1.nazivDatoteke);

    Properties p1 = ka2.dajSvePostavke();
    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka3 = this.dajObjekt(this.nazivDatoteke_3);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka3.postavke = p3;

    this.spremiKonfiguracijuPomocna(ka3.postavke, ka3.nazivDatoteke);

    Konfiguracija ka4 = KonfiguracijaApstraktna.preuzmiKonfiguraciju(ka3.nazivDatoteke);
    Properties p4 = ka4.dajSvePostavke();

    Properties p5 = new Properties();
    p5.setProperty("k1", "pero");
    p5.setProperty("k2", "mato");
    p5.setProperty("k3", "ivo");
    p5.setProperty("k4", "luka");

    assertEquals(p5.size(), p4.size());
    assertEquals(p5, p4);

    assertThrows(NeispravnaKonfiguracija.class,
        () -> KonfiguracijaApstraktna.preuzmiKonfiguraciju("pero.abc"));
  }


  @Test
  public void testPreuzmiKreirajKonfiguraciju() throws Exception {
    Konfiguracija ka1 = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(this.nazivDatoteke_1);

    Properties p1 = ka1.dajSvePostavke();
    Properties p2 = new Properties();

    assertEquals(0, p1.size());
    assertEquals(p2, p1);

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_2);

    Properties p3 = new Properties();
    p3.setProperty("k1", "pero");
    p3.setProperty("k2", "mato");
    p3.setProperty("k3", "ivo");
    p3.setProperty("k4", "luka");
    ka2.postavke = p3;

    ka2.spremiKonfiguraciju();

    Konfiguracija ka3 = KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju(this.nazivDatoteke_2);

    Properties p4 = ka3.dajSvePostavke();

    Properties p5 = new Properties();
    p5.setProperty("k1", "pero");
    p5.setProperty("k2", "mato");
    p5.setProperty("k3", "ivo");
    p5.setProperty("k4", "luka");

    assertEquals(p5.size(), p4.size());
    assertEquals(p5, p4);

    assertThrows(NeispravnaKonfiguracija.class,
        () -> KonfiguracijaApstraktna.preuzmiKreirajKonfiguraciju("pero.abc"));
  }

  @Test
  public void testDajKonfiguraciju() throws Exception {
    Konfiguracija ka1 = KonfiguracijaApstraktna.dajKonfiguraciju(this.nazivDatoteke_1);

    assertNotNull(ka1);

    KonfiguracijaApstraktna ka2 = this.dajObjekt(this.nazivDatoteke_1);

    assertThrows(NeispravnaKonfiguracija.class,
        () -> KonfiguracijaApstraktna.dajKonfiguraciju("pero.abc"));
  }

  public Properties ucitajKonfiguracijuPomocna(String nazivDatoteke)
      throws NeispravnaKonfiguracija {
    Properties p = new Properties();

    File f = new File(nazivDatoteke);

    if (f.exists() && f.isFile()) {
      try {
        p.load(new FileInputStream(f));
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

  public void spremiKonfiguracijuPomocna(Properties p, String nazivDatoteke)
      throws NeispravnaKonfiguracija {

    File f = new File(nazivDatoteke);

    if ((f.exists() && f.isFile()) || !f.exists()) {
      try {
        p.store(new FileOutputStream(f), "");
      } catch (IOException ex) {
        throw new NeispravnaKonfiguracija(
            "Problem kod učitavanja konfiguracije: '" + nazivDatoteke + "'");
      }
    }
  }

  private void obrisiSveDatoteke() {
    if (!(this.obrisiDatoteku(nazivDatoteke_1) && this.obrisiDatoteku(nazivDatoteke_2)
        && this.obrisiDatoteku(nazivDatoteke_3) && this.obrisiDatoteku(nazivDatoteke_4))) {
      fail("Brisanje datoteke nije bilo uspješno");
      return;
    }
  }

  private boolean obrisiDatoteku(String nazivDatoteke) {

    File f = new File(nazivDatoteke);

    if (!f.exists()) {
      return true;
    } else if (f.exists() && f.isFile()) {
      f.delete();
      if (!f.exists()) {
        return true;
      }
    }
    return false;
  }

}
