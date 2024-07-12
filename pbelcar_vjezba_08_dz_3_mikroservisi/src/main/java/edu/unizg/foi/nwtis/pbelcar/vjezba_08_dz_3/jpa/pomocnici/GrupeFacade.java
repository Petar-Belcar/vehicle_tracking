package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Grupe;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Grupe_;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class GrupeFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Grupe grupe) {
    em.persist(grupe);
  }

  public void edit(Grupe grupe) {
    em.merge(grupe);
  }

  public void remove(Grupe grupe) {
    em.remove(em.merge(grupe));
  }

  public Grupe find(Object id) {
    return em.find(Grupe.class, id);
  }

  public List<Grupe> dohvatiSveGrupeKorisnika(String pk, KorisniciFacade kF) {
    CriteriaQuery<Grupe> cq = cb.createQuery(Grupe.class);
    Root<Grupe> grupe = cq.from(Grupe.class);
    cq.where(cb.equal(grupe.get(Grupe_.korisnicis), kF.find(pk)));
    TypedQuery<Grupe> q = em.createQuery(cq);
    return q.getResultList();
  }
}
