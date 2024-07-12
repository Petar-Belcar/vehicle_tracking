package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti;

import java.io.Serializable;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


/**
 * The persistent class for the KORISNICI database table.
 * 
 */
@Entity
@Table(name = "KORISNICI")
@NamedQuery(name = "Korisnici.findAll", query = "SELECT k FROM Korisnici k")
public class Korisnici implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 20)
  private String korisnik;

  @Column(length = 100)
  private String email;

  @Column(length = 25)
  private String ime;

  @Column(length = 20)
  private String lozinka;

  @Column(length = 25)
  private String prezime;

  // bi-directional many-to-many association to Grupe
  @ManyToMany(fetch = FetchType.EAGER)
  @JsonbTransient
  @JoinTable(name = "ULOGE", joinColumns = {@JoinColumn(name = "KORISNIK", nullable = false)},
      inverseJoinColumns = {@JoinColumn(name = "GRUPA", nullable = false)})
  private List<Grupe> grupes;

  // bi-directional many-to-one association to Vozila
  @JsonbTransient
  @OneToMany(mappedBy = "korisnici", fetch = FetchType.EAGER)
  private List<Vozila> vozilas;

  public Korisnici() {}

  public String getKorisnik() {
    return this.korisnik;
  }

  public void setKorisnik(String korisnik) {
    this.korisnik = korisnik;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIme() {
    return this.ime;
  }

  public void setIme(String ime) {
    this.ime = ime;
  }

  public String getLozinka() {
    return this.lozinka;
  }

  public void setLozinka(String lozinka) {
    this.lozinka = lozinka;
  }

  public String getPrezime() {
    return this.prezime;
  }

  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }

  public List<Grupe> getGrupes() {
    return this.grupes;
  }

  public void setGrupes(List<Grupe> grupes) {
    this.grupes = grupes;
  }

  public List<Vozila> getVozilas() {
    return this.vozilas;
  }

  public void setVozilas(List<Vozila> vozilas) {
    this.vozilas = vozilas;
  }

  public Vozila addVozila(Vozila vozila) {
    getVozilas().add(vozila);
    vozila.setKorisnici(this);

    return vozila;
  }

  public Vozila removeVozila(Vozila vozila) {
    getVozilas().remove(vozila);
    vozila.setKorisnici(null);

    return vozila;
  }

}
