package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Radar;

public class OdgovorListaRadari {
  public String getOdgovor() {
    return odgovor;
  }

  public void setOdgovor(String odgovor) {
    this.odgovor = odgovor;
  }

  public List<Radar> getListaRadara() {
    return listaRadara;
  }

  public void setListaRadara(List<Radar> listaRadara) {
    this.listaRadara = listaRadara;
  }

  private String odgovor;
  private List<Radar> listaRadara;

  public OdgovorListaRadari(String odg) {
    this.odgovor = odg;
    this.listaRadara = new ArrayList<Radar>();
  }

  public OdgovorListaRadari() {

  }
}
