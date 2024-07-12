package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;


/**
 * The persistent class for the DNEVNIK_RADA database table.
 * 
 */
@Entity
@Table(name = "DNEVNIK_RADA")
@NamedQuery(name = "DnevnikRada.findAll", query = "SELECT d FROM DnevnikRada d")
public class DnevnikRada implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "DNEVNIK_RADA_ID_GENERATOR", sequenceName = "DNEVNIK_RADA_ID_GENERATOR",
      initialValue = 1, allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DNEVNIK_RADA_ID_GENERATOR")
  private int id;

  @Column(length = 99)
  private String adresaracunala;

  @Column(length = 50)
  private String ipadresaracunala;

  @Column(length = 20)
  private String korisnickoime;

  @Column(length = 30)
  private String nazivos;

  @Column(length = 512)
  private String opisrada;

  @Column(length = 20)
  private String verzijavm;

  private Timestamp vrijeme;

  public DnevnikRada() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getAdresaracunala() {
    return this.adresaracunala;
  }

  public void setAdresaracunala(String adresaracunala) {
    this.adresaracunala = adresaracunala;
  }

  public String getIpadresaracunala() {
    return this.ipadresaracunala;
  }

  public void setIpadresaracunala(String ipadresaracunala) {
    this.ipadresaracunala = ipadresaracunala;
  }

  public String getKorisnickoime() {
    return this.korisnickoime;
  }

  public void setKorisnickoime(String korisnickoime) {
    this.korisnickoime = korisnickoime;
  }

  public String getNazivos() {
    return this.nazivos;
  }

  public void setNazivos(String nazivos) {
    this.nazivos = nazivos;
  }

  public String getOpisrada() {
    return this.opisrada;
  }

  public void setOpisrada(String opisrada) {
    this.opisrada = opisrada;
  }

  public String getVerzijavm() {
    return this.verzijavm;
  }

  public void setVerzijavm(String verzijavm) {
    this.verzijavm = verzijavm;
  }

  public Timestamp getVrijeme() {
    return this.vrijeme;
  }

  public void setVrijeme(Timestamp vrijeme) {
    this.vrijeme = vrijeme;
  }

}
