package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti;

import java.io.Serializable;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


/**
 * The persistent class for the VOZNJE database table.
 * 
 */
@Entity
@Table(name = "VOZNJE")
@NamedQuery(name = "Voznje.findAll", query = "SELECT v FROM Voznje v")
public class Voznje implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "VOZNJE_RB_GENERATOR", sequenceName = "VOZNJE_RB_GENERATOR",
      initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VOZNJE_RB_GENERATOR")
  private int rb;

  @Column(nullable = false)
  private int broj;

  @Column(nullable = false)
  private double brzina;

  @Column(nullable = false)
  private double gpsbrzina;

  @Column(nullable = false)
  private double gpsduzina;

  @Column(nullable = false)
  private double gpssirina;

  @Column(nullable = false)
  private int kapacitetbaterija;

  @Column(nullable = false)
  private double naponbaterija;

  @Column(nullable = false)
  private int postotakbaterija;

  @Column(nullable = false)
  private double preostalokm;

  @Column(nullable = false)
  private double snaga;

  @Column(nullable = false)
  private double struja;

  @Column(nullable = false)
  private int tempbaterija;

  @Column(nullable = false)
  private int tempvozila;

  @Column(nullable = false)
  private double ukupnokm;

  @Column(nullable = false)
  private double visina;

  @Column(nullable = false)
  private long vrijeme;

  // bi-directional many-to-one association to Vozila
  @ManyToOne
  @JsonbTransient
  @JoinColumn(name = "ID", nullable = false)
  private Vozila vozila;

  public Voznje() {}

  public int getRb() {
    return this.rb;
  }

  public void setRb(int rb) {
    this.rb = rb;
  }

  public int getBroj() {
    return this.broj;
  }

  public void setBroj(int broj) {
    this.broj = broj;
  }

  public double getBrzina() {
    return this.brzina;
  }

  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  public double getGpsbrzina() {
    return this.gpsbrzina;
  }

  public void setGpsbrzina(double gpsbrzina) {
    this.gpsbrzina = gpsbrzina;
  }

  public double getGpsduzina() {
    return this.gpsduzina;
  }

  public void setGpsduzina(double gpsduzina) {
    this.gpsduzina = gpsduzina;
  }

  public double getGpssirina() {
    return this.gpssirina;
  }

  public void setGpssirina(double gpssirina) {
    this.gpssirina = gpssirina;
  }

  public int getKapacitetbaterija() {
    return this.kapacitetbaterija;
  }

  public void setKapacitetbaterija(int kapacitetbaterija) {
    this.kapacitetbaterija = kapacitetbaterija;
  }

  public double getNaponbaterija() {
    return this.naponbaterija;
  }

  public void setNaponbaterija(double naponbaterija) {
    this.naponbaterija = naponbaterija;
  }

  public int getPostotakbaterija() {
    return this.postotakbaterija;
  }

  public void setPostotakbaterija(int postotakbaterija) {
    this.postotakbaterija = postotakbaterija;
  }

  public double getPreostalokm() {
    return this.preostalokm;
  }

  public void setPreostalokm(double preostalokm) {
    this.preostalokm = preostalokm;
  }

  public double getSnaga() {
    return this.snaga;
  }

  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }

  public double getStruja() {
    return this.struja;
  }

  public void setStruja(double struja) {
    this.struja = struja;
  }

  public int getTempbaterija() {
    return this.tempbaterija;
  }

  public void setTempbaterija(int tempbaterija) {
    this.tempbaterija = tempbaterija;
  }

  public int getTempvozila() {
    return this.tempvozila;
  }

  public void setTempvozila(int tempvozila) {
    this.tempvozila = tempvozila;
  }

  public double getUkupnokm() {
    return this.ukupnokm;
  }

  public void setUkupnokm(double ukupnokm) {
    this.ukupnokm = ukupnokm;
  }

  public double getVisina() {
    return this.visina;
  }

  public void setVisina(double visina) {
    this.visina = visina;
  }

  public long getVrijeme() {
    return this.vrijeme;
  }

  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }

  public Vozila getVozila() {
    return this.vozila;
  }

  public void setVozila(Vozila vozila) {
    this.vozila = vozila;
  }
}
