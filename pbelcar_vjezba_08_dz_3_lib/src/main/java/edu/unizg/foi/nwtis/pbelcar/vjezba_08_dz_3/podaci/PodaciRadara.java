package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

/**
 * Zapis PodaciRadara.
 *
 * @param id identifikator radara
 * @param adresaRadara adresa poslužitelja radara (logička ili IP)
 * @param mreznaVrataRadara mrežna vrata poslužitelja radara
 * @param maksBrzina maksimalna dopuštena brzina
 * @param maksTrajanje maksimalno trajanje maksBrzina
 * @param maksUdaljenost maksimalna udaljenost do koje radar mjeri brzinu
 * @param adresaRegistracije adresa poslužitelja za registraciju radara
 * @param mreznaVrataRegistracije mrezna vrata poslužitelja za registraciju radara
 * @param adresaKazne adresa poslužitelja za kazne
 * @param mreznaVrataKazne mrezna vrata poslužitelja za kazne
 * @param postanskaAdresaRadara poštanska adresa radara
 * @param gpsSirina gps širina pozicije radara
 * @param gpsDuzina gps dužina pozicije radara
 */
public record PodaciRadara(int id, String adresaRadara, int mreznaVrataRadara, int maksBrzina,
    int maksTrajanje, int maksUdaljenost, String adresaRegistracije, int mreznaVrataRegistracije,
    String adresaKazne, int mreznaVrataKazne, String postanskaAdresaRadara, double gpsSirina,
    double gpsDuzina) {

}
