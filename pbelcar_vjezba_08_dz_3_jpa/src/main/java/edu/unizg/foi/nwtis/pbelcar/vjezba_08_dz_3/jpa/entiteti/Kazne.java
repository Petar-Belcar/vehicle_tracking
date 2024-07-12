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
 * The persistent class for the KAZNE database table.
 * 
 */
@Entity
@Table(name = "KAZNE")
@NamedQuery(name = "Kazne.findAll", query = "SELECT k FROM Kazne k")
public class Kazne implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "KAZNE_RB_GENERATOR", sequenceName = "KAZNE_RB_GENERATOR",
      initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KAZNE_RB_GENERATOR")
  private int rb;

  @Column(nullable = false)
  private double brzina;

  @Column(nullable = false)
  private double gpsduzina;

  @Column(nullable = false)
  private double gpsduzinaradar;

  @Column(nullable = false)
  private double gpssirina;

  @Column(nullable = false)
  private double gpssirinaradar;

  @Column(nullable = false)
  private long vrijemekraj;

  @Column(nullable = false)
  private long vrijemepocetak;

  // bi-directional many-to-one association to Vozila
  @ManyToOne
  @JsonbTransient
  @JoinColumn(name = "ID", nullable = false)
  private Vozila vozila;

  public Kazne() {}

  public int getRb() {
    return this.rb;
  }

  public void setRb(int rb) {
    this.rb = rb;
  }

  public double getBrzina() {
    return this.brzina;
  }

  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  public double getGpsduzina() {
    return this.gpsduzina;
  }

  public void setGpsduzina(double gpsduzina) {
    this.gpsduzina = gpsduzina;
  }

  public double getGpsduzinaradar() {
    return this.gpsduzinaradar;
  }

  public void setGpsduzinaradar(double gpsduzinaradar) {
    this.gpsduzinaradar = gpsduzinaradar;
  }

  public double getGpssirina() {
    return this.gpssirina;
  }

  public void setGpssirina(double gpssirina) {
    this.gpssirina = gpssirina;
  }

  public double getGpssirinaradar() {
    return this.gpssirinaradar;
  }

  public void setGpssirinaradar(double gpssirinaradar) {
    this.gpssirinaradar = gpssirinaradar;
  }

  public long getVrijemekraj() {
    return this.vrijemekraj;
  }

  public void setVrijemekraj(long vrijemekraj) {
    this.vrijemekraj = vrijemekraj;
  }

  public long getVrijemepocetak() {
    return this.vrijemepocetak;
  }

  public void setVrijemepocetak(long vrijemepocetak) {
    this.vrijemepocetak = vrijemepocetak;
  }

  public Vozila getVozila() {
    return this.vozila;
  }

  public void setVozila(Vozila vozila) {
    this.vozila = vozila;
  }

}
