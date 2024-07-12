package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * The persistent class for the VOZILA database table.
 * 
 */
@Entity
@Table(name = "VOZILA")
@NamedQuery(name = "Vozila.findAll", query = "SELECT v FROM Vozila v")
public class Vozila implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false)
  private int vozilo;

  @Column(nullable = false, length = 20)
  private String model;

  @Column(nullable = false, length = 50)
  private String proizvodac;

  // bi-directional many-to-one association to Kazne
  @OneToMany(mappedBy = "vozila", fetch = FetchType.EAGER)
  private List<Kazne> kaznes;

  // bi-directional many-to-one association to Pracenevoznje
  @OneToMany(mappedBy = "vozila", fetch = FetchType.EAGER)
  private List<Pracenevoznje> pracenevoznjes;

  // bi-directional many-to-one association to Korisnici
  @ManyToOne
  @JoinColumn(name = "VLASNIK", nullable = false)
  private Korisnici korisnici;

  public Vozila() {}

  public int getVozilo() {
    return this.vozilo;
  }

  public void setVozilo(int vozilo) {
    this.vozilo = vozilo;
  }

  public String getModel() {
    return this.model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getProizvodac() {
    return this.proizvodac;
  }

  public void setProizvodac(String proizvodac) {
    this.proizvodac = proizvodac;
  }

  public List<Kazne> getKaznes() {
    return this.kaznes;
  }

  public void setKaznes(List<Kazne> kaznes) {
    this.kaznes = kaznes;
  }

  public Kazne addKazne(Kazne kazne) {
    getKaznes().add(kazne);
    kazne.setVozila(this);

    return kazne;
  }

  public Kazne removeKazne(Kazne kazne) {
    getKaznes().remove(kazne);
    kazne.setVozila(null);

    return kazne;
  }

  public List<Pracenevoznje> getPracenevoznjes() {
    return this.pracenevoznjes;
  }

  public void setPracenevoznjes(List<Pracenevoznje> pracenevoznjes) {
    this.pracenevoznjes = pracenevoznjes;
  }

  public Pracenevoznje addPracenevoznje(Pracenevoznje pracenevoznje) {
    getPracenevoznjes().add(pracenevoznje);
    pracenevoznje.setVozila(this);

    return pracenevoznje;
  }

  public Pracenevoznje removePracenevoznje(Pracenevoznje pracenevoznje) {
    getPracenevoznjes().remove(pracenevoznje);
    pracenevoznje.setVozila(null);

    return pracenevoznje;
  }

  public Korisnici getKorisnici() {
    return this.korisnici;
  }

  public void setKorisnici(Korisnici korisnici) {
    this.korisnici = korisnici;
  }

}
