package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje_;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Vozila;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici.SimulacijeFacade.OdgovorString;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class SimulacijeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;


  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Pracenevoznje Voznje) {
    em.persist(Voznje);
  }

  public void edit(Pracenevoznje Voznje) {
    em.merge(Voznje);
  }

  public void remove(Pracenevoznje Voznje) {
    em.remove(em.merge(Voznje));
  }

  public Pracenevoznje find(Object id) {
    return em.find(Pracenevoznje.class, id);
  }

  public List<Pracenevoznje> vratiVoznjeOdDo(long vrijemeOd, long vrijemeDo) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVOZILA, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM VOZNJE WHERE VRIJEME >= ? AND VRIJEME <= ?";
     */
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> Voznje = cq.from(Pracenevoznje.class);
    cq.where(cb.between(Voznje.get(Pracenevoznje_.vrijeme), vrijemeOd, vrijemeDo));
    return em.createQuery(cq).getResultList();
  }

  public List<Pracenevoznje> vratiVoznjePoId(int id, VozilaFacade vF) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVOZILA, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM VOZNJE WHERE ID = ?";
     */
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> Voznje = cq.from(Pracenevoznje.class);
    cq.where(cb.equal(Voznje.get(Pracenevoznje_.vozila), vF.find(id)));
    return em.createQuery(cq).getResultList();
  }


  public List<Pracenevoznje> vratiVoznjePoId(int id, long vrijemeOd, long vrijemeDo,
      VozilaFacade vF) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVOZILA, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM VOZNJE WHERE ID = ? AND VRIJEME >= ? AND VRIJEME <= ?";
     */
    CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
    Root<Pracenevoznje> Voznje = cq.from(Pracenevoznje.class);
    cq.where(cb.and(cb.equal(Voznje.get(Pracenevoznje_.vozila), vF.find(id)),
        cb.between(Voznje.get(Pracenevoznje_.vrijeme), vrijemeOd, vrijemeDo)));
    return em.createQuery(cq).getResultList();
  }


  public class OdgovorString {
    String odgovor;

    public OdgovorString(String o) {
      this.odgovor = o;
    }

    public String getOdgovor() {
      return odgovor;
    }

    public void setOdgovor(String odgovor) {
      this.odgovor = odgovor;
    }
  }

  public String vratiDodavanjeVoznje(String adresa, int vrata, Vozilo v, VozilaFacade vF) {
    /*
     * "INSERT INTO VOZNJE (ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, " +
     * "GPSSIRINA, GPSDUZINA, TEMPVOZILA, POSTOTAKBATERIJA, NAPONBATERIJA, " +
     * "KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA) VALUES " +
     * "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
     */
    try {
      /*
       * Thread t = new Thread(() -> { try { MrezneOperacije.posaljiZahtjevPosluzitelju(adresa,
       * vrata, this.pretvotiVoziloUString(v)); } catch (Exception e) { } });
       * 
       * t.start(); // Stavljeno u vlastiti thread za testiranje
       */
      MrezneOperacije.posaljiZahtjevPosluzitelju(adresa, vrata, this.pretvotiVoziloUString(v));
      this.create(this.konvertiraj(v, vF));
      return "OK\n";
    } catch (Exception e) {
      this.create(this.konvertiraj(v, vF));
      return "OK\n";
    }
  }

  private String pretvotiVoziloUString(Vozilo pV) {
    return String.format("VOZILO %d %d %d %f %f %f %f %f %d %d %f %d %d %f %f %f %f\n", pV.getId(),
        pV.getBroj(), pV.getVrijeme(), pV.getBrzina(), pV.getSnaga(), pV.getStruja(),
        pV.getVisina(), pV.getGpsBrzina(), pV.getTempVozila(), pV.getPostotakBaterija(),
        pV.getNaponBaterija(), pV.getKapacitetBaterija(), pV.getTempBaterija(), pV.getPreostaloKm(),
        pV.getUkupnoKm(), pV.getGpsSirina(), pV.getGpsDuzina());
  }

  public Pracenevoznje konvertiraj(Vozilo vozilo, VozilaFacade vF) {
    Pracenevoznje pracenevoznje = new Pracenevoznje();
    pracenevoznje.setBroj(vozilo.getBroj());
    pracenevoznje.setBrzina(vozilo.getBrzina());
    pracenevoznje.setGpsbrzina(vozilo.getGpsBrzina());
    pracenevoznje.setGpsduzina(vozilo.getGpsDuzina());
    pracenevoznje.setGpssirina(vozilo.getGpsSirina());
    pracenevoznje.setKapacitetbaterija(vozilo.getKapacitetBaterija());
    pracenevoznje.setNaponbaterija(vozilo.getNaponBaterija());
    pracenevoznje.setPostotakbaterija(vozilo.getPostotakBaterija());
    pracenevoznje.setPreostalokm(vozilo.getPreostaloKm());
    pracenevoznje.setSnaga(vozilo.getSnaga());
    pracenevoznje.setStruja(vozilo.getStruja());
    pracenevoznje.setTempbaterija(vozilo.getTempBaterija());
    pracenevoznje.setTempvozila(vozilo.getTempVozila());
    pracenevoznje.setUkupnokm(vozilo.getUkupnoKm());
    pracenevoznje.setVisina(vozilo.getVisina());
    pracenevoznje.setVrijeme(vozilo.getVrijeme());
    pracenevoznje.setVozila(vF.find(vozilo.getId()));
    return pracenevoznje;
  }
}
