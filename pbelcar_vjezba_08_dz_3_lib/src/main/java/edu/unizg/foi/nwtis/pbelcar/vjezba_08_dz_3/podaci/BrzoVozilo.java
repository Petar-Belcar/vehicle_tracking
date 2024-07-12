package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

/**
 * Zapis BrzoVozilo.
 *
 * @param id identifikator e-vozila
 * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
 * @param vrijeme vrijeme nastanka podatka u retku
 * @param brzina brzina e-vozila u vrijeme
 * @param gpsSirina gps širina pozicije e-vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije e-vozila u vrijeme
 * @param status status zapisa. true početak brze vožnje, false nije brza vožnja
 */
public record BrzoVozilo(int id, int broj, long vrijeme, double brzina, double gpsSirina,
    double gpsDuzina, boolean status) {

  /**
   * Postavi status.
   *
   * @param status status zapisa
   * @return novo brzo vozilo
   */
  public BrzoVozilo postaviStatus(Boolean status) {
    return new BrzoVozilo(id(), broj(), vrijeme(), brzina(), gpsSirina(), gpsDuzina(), status);
  }

}
