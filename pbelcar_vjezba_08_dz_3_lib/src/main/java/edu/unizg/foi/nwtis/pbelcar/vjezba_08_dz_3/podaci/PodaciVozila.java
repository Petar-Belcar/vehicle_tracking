package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

/**
 * Zapis PodaciVozila.
 *
 * @param id identifikator e-vozila
 * @param broj broj retka iz datoteke podataka koju SimulatorVozila koristi u radu
 * @param vrijeme vrijeme nastanka podatka u retku
 * @param brzina brzina e-vozila u vrijeme
 * @param snaga snaga koju troši e-vozilo u vrijeme
 * @param struja napon koji troši e-vozilo u vrijeme
 * @param visina nadmorska visina e-vozilo u vrijeme
 * @param gpsBrzina gps brzina e-vozilo u vrijeme
 * @param tempVozila temperatura e-vozila u vrijeme
 * @param postotakBaterija postotak baterija e-vozila u vrijeme
 * @param naponBaterija napon baterija e-vozila u vrijeme
 * @param kapacitetBaterija kapacitet baterija e-vozila u vrijeme
 * @param tempBaterija temperatura baterija e-vozila u vrijeme
 * @param preostaloKm procjena preostalih km e-vozila u vrijeme
 * @param ukupnoKm ukupno prijeđeno km e-vozila u vrijeme
 * @param gpsSirina gps širina pozicije e-vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije e-vozila u vrijemezina
 */
public record PodaciVozila(int id, int broj, long vrijeme, double brzina, double snaga,
    double struja, double visina, double gpsBrzina, int tempVozila, int postotakBaterija,
    double naponBaterija, int kapacitetBaterija, int tempBaterija, double preostaloKm,
    double ukupnoKm, double gpsSirina, double gpsDuzina) {

}
