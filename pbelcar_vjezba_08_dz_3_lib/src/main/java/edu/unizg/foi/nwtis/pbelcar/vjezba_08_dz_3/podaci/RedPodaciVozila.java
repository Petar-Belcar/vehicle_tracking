package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa RedPodaciVozila.
 */
public class RedPodaciVozila {

  /** identifikator e-vozila. */
  protected int id;

  /** kolekcija s podacima e-vozila. */
  protected Queue<PodaciVozila> podaciVozila;

  /**
   * Instancira novi objekt.
   */
  private RedPodaciVozila() {}

  /**
   * Instancira novi objekt.
   *
   * @param id identifikator e-vozila
   */
  public RedPodaciVozila(int id) {
    super();
    this.id = id;
    this.podaciVozila = new ConcurrentLinkedQueue<>();
  }

  /**
   * Instancira novi objekt.
   *
   * @param id dentifikator e-vozila
   * @param podaci kolekcija s podacima e-vozila
   */
  public RedPodaciVozila(int id, Queue<PodaciVozila> podaci) {
    super();
    this.id = id;
    this.podaciVozila = podaci;
  }

  /**
   * Daj identifikator e-vozila.
   *
   * @return id identifikator e-vozila
   */
  public int dajId() {
    return this.id;
  }

  /**
   * Daj broj elemenata u kolekciji s podacima e-vozila.
   *
   * @return the int
   */
  public int dajBrojPodatakaVozila() {
    return podaciVozila.size();
  }

  /**
   * Dodaj podatak e-vozila u kolekciju s podacima e-vozila.
   *
   * @param podaciVozila podaci e-vozila
   * @return true, ako je uspješno
   */
  public boolean dodajPodatakVozila(PodaciVozila podaciVozila) {
    return this.podaciVozila.add(podaciVozila);
  }

  /**
   * Daj podatke svih e-vozila iz kolekcije s podacima e-vozila.
   *
   * @return kolekcija s podacima e-vozila
   */
  public Queue<PodaciVozila> dajSvePodatkeVozila() {
    return this.podaciVozila;
  }

}
