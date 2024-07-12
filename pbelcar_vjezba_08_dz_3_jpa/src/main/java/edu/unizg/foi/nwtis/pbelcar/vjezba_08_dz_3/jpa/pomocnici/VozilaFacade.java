/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.pomocnici;



import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.jpa.entiteti.Vozila;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author Dragutin Kermek
 */
@Named
@Stateless
public class VozilaFacade {
  @PersistenceContext(unitName = "nwtis_pu")
  private EntityManager em;

  @PostConstruct
  private void init() {}

  public void create(Vozila vozila) {
    em.persist(vozila);
  }

  public void edit(Vozila vozila) {
    em.merge(vozila);
  }

  public void remove(Vozila vozila) {
    em.remove(em.merge(vozila));
  }

  public Vozila find(Object id) {
    return em.find(Vozila.class, id);
  }

}
