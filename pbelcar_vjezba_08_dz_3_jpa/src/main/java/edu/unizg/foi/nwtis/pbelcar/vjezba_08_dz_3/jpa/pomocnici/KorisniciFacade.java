package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Korisnici;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Korisnici_;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class KorisniciFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(Korisnici k) {
    em.persist(k);
  }

  public void edit(Korisnici k) {
    em.merge(k);
  }

  public void remove(Korisnici k) {
    em.remove(em.merge(k));
  }

  public Korisnici find(Object id) {
    return em.find(Korisnici.class, id);
  }

  public List<Korisnici> dohvatiSveKorisnikeSaKorImeILoz(String korisnik, String lozinka) {
    CriteriaQuery<Korisnici> cq = cb.createQuery(Korisnici.class);
    Root<Korisnici> korisniki = cq.from(Korisnici.class);
    cq.where(cb.and(cb.equal(korisniki.get(Korisnici_.korisnik), korisnik),
        cb.equal(korisniki.get(Korisnici_.lozinka), lozinka)));
    TypedQuery<Korisnici> q = em.createQuery(cq);
    return q.getResultList();
  }
}
