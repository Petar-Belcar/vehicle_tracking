/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;



import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Kazne;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Kazne_;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

/**
 *
 * @author Dragutin Kermek
 */
@Named
@Stateless
public class KazneFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Kazne kazne) {
    em.persist(kazne);
  }

  public void edit(Kazne kazne) {
    em.merge(kazne);
  }

  public void remove(Kazne kazne) {
    em.remove(em.merge(kazne));
  }

  public Kazne find(Object id) {
    return em.find(Kazne.class, id);
  }

  public List<Kazne> dohvatiSveKazne() {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    cq.select(cq.from(Kazne.class));
    return em.createQuery(cq).getResultList();
  }

  public List<Kazne> findRange(int[] range) {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    cq.select(cq.from(Kazne.class));
    TypedQuery<Kazne> q = em.createQuery(cq);
    q.setMaxResults(range[1] - range[0]);
    q.setFirstResult(range[0]);
    return q.getResultList();
  }

  public List<Kazne> dohvatiKazne(long odVremena, long doVremena) {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    Root<Kazne> kazne = cq.from(Kazne.class);
    cq.where(cb.between(kazne.get(Kazne_.vrijemekraj), odVremena, doVremena));
    TypedQuery<Kazne> q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Kazne> dohvatiKaznu(long rb) {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    Root<Kazne> kazne = cq.from(Kazne.class);
    cq.where(cb.equal(kazne.get(Kazne_.rb), rb));
    TypedQuery<Kazne> q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Kazne> dohvatiKazneVozila(int id, VozilaFacade vF) {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    Root<Kazne> kazne = cq.from(Kazne.class);
    cq.where(cb.equal(kazne.get(Kazne_.vozila), vF.find(id)));
    TypedQuery<Kazne> q = em.createQuery(cq);
    return q.getResultList();
  }

  public List<Kazne> dohvatiKazneVozila(int id, long odVremena, long doVremena, VozilaFacade vF) {
    CriteriaQuery<Kazne> cq = cb.createQuery(Kazne.class);
    Root<Kazne> kazne = cq.from(Kazne.class);
    cq.where(cb.and(cb.equal(kazne.get(Kazne_.vozila), vF.find(id)),
        cb.between(kazne.get(Kazne_.vrijemekraj), odVremena, doVremena)));
    TypedQuery<Kazne> q = em.createQuery(cq);
    return q.getResultList();
  }

  public boolean dodajKaznu(Kazne kazne) {
    try {
      this.create(kazne);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}