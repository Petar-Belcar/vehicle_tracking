package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo;

public class OdgovorListaVozila {
  List<Vozilo> voznje;
  String odgovor;

  public OdgovorListaVozila(String odg) {
    this.odgovor = odg;
  }

  public OdgovorListaVozila() {

  }

  public String getOdgovor() {
    return odgovor;
  }

  public List<Vozilo> getVoznje() {
    return voznje;
  }

  public void setVoznje(List<Vozilo> voznje) {
    this.voznje = voznje;
  }

  public void setOdgovor(String odgovor) {
    this.odgovor = odgovor;
    this.voznje = new ArrayList<>();
  }
}
