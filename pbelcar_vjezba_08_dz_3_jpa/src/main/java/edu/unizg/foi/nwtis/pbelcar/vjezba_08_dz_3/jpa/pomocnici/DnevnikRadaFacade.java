package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;

import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.DnevnikRada;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;

@RequestScoped
public class DnevnikRadaFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;
  private CriteriaBuilder cb;

  @PostConstruct
  private void init() {
    cb = em.getCriteriaBuilder();
  }

  public void create(DnevnikRada dR) {
    em.persist(dR);
  }

  public void edit(DnevnikRada dR) {
    em.merge(dR);
  }

  public void remove(DnevnikRada dR) {
    em.remove(em.merge(dR));
  }

  public DnevnikRada find(Object id) {
    return em.find(DnevnikRada.class, id);
  }

  public boolean dodajDnevnikRada(DnevnikRada dR) {
    try {
      this.create(dR);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
