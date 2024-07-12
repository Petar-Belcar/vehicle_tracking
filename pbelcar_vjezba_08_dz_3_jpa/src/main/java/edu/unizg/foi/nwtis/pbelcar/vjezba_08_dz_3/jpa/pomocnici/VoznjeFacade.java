/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;



import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Voznje_;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 *
 * @author Dragutin Kermek
 */
@Named
@Stateless
public class VoznjeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Voznje Voznje) {
    em.persist(Voznje);
  }

  public void edit(Voznje Voznje) {
    em.merge(Voznje);
  }

  public void remove(Voznje Voznje) {
    em.remove(em.merge(Voznje));
  }

  public Voznje find(Object id) {
    return em.find(Voznje.class, id);
  }

  public List<Voznje> vratiVoznjeOdDo(long vrijemeOd, long vrijemeDo) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVoznje, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM PRACENEVOZNJE WHERE VRIJEME >= ? AND VRIJEME <= ?";
     */
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> Voznje = cq.from(Voznje.class);
    cq.where(cb.between(Voznje.get(Voznje_.vrijeme), vrijemeOd, vrijemeDo));
    return em.createQuery(cq).getResultList();
  }

  public List<Voznje> vratiVoznjePoId(int id, VozilaFacade vF) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVoznje, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM PRACENEVOZNJE WHERE ID = ?";
     */
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> Voznje = cq.from(Voznje.class);
    cq.where(cb.equal(Voznje.get(Voznje_.vozila), vF.find(id)));
    return em.createQuery(cq).getResultList();
  }

  public List<Voznje> vratiVoznjePoId(int id, long vrijemeOd, long vrijemeDo, VozilaFacade vF) {
    /*
     * "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSSIRINA, GPSDUZINA, TEMPVoznje, POSTOTAKBATERIJA,"
     * + " NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSBRZINA " +
     * "FROM PRACENEVOZNJE WHERE ID = ? AND VRIJEME >= ? AND VRIJEME <= ?";
     */
    CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
    Root<Voznje> Voznje = cq.from(Voznje.class);
    cq.where(cb.and(cb.between(Voznje.get(Voznje_.vrijeme), vrijemeOd, vrijemeDo),
        cb.equal(Voznje.get(Voznje_.vozila), vF.find(id))));
    return em.createQuery(cq).getResultList();
  }

  public boolean vratiDodavanjeVoznje(Voznje v) {
    try {
      this.create(v);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
