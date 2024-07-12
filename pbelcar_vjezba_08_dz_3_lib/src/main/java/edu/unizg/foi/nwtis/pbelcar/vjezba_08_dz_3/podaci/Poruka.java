package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Dragutin Kermek
 */
public class Poruka implements Serializable {

  private static final long serialVersionUID = 2842524179988344403L;
  private String naslov;
  private String sadrzaj;
  private Date vrijeme;
  private Kazna kazna;

  public String getNaslov() {
    return naslov;
  }

  public void setNaslov(String naslov) {
    this.naslov = naslov;
  }

  public String getSadrzaj() {
    return sadrzaj;
  }

  public void setSadrzaj(String sadrzaj) {
    this.sadrzaj = sadrzaj;
  }

  public Date getVrijeme() {
    return vrijeme;
  }

  public void setVrijeme(Date vrijeme) {
    this.vrijeme = vrijeme;
  }

  public Kazna getKazna() {
    return kazna;
  }

  public void setKazna(Kazna kazna) {
    this.kazna = kazna;
  }

}
