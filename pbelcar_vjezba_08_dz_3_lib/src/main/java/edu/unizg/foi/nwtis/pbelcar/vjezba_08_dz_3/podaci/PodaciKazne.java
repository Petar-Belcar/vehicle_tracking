package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

/**
 * Zapis PodaciKazne.
 *
 * @param id identifikator e-vozila
 * @param vrijemePocetak vrijeme kada je radar uočio početak brze vožnju
 * @param vrijemeKraj vrijeme kada je radar uočio brzu vožnju dulju od dozvoljenog vremena
 * @param brzina brzina e-vozila u vrijemeKraj
 * @param gpsSirina gps širina pozicije e-vozila u vrijeme
 * @param gpsDuzina gps dužina pozicije e-vozila u vrijeme
 * @param gpsSirinaRadar gps širina pozicije radara
 * @param gpsDuzinaRadar gps dužina pozicije radara
 */
public record PodaciKazne(int id, long vrijemePocetak, long vrijemeKraj, double brzina,
    double gpsSirina, double gpsDuzina, double gpsSirinaRadar, double gpsDuzinaRadar) {

}
