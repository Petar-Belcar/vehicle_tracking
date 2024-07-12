package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno.OdgovorListaVozila;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentSimulacija {
  public RestKlijentSimulacija() {

  }

  public OdgovorListaVozila getVozilaOdDo(long vrijemeOd, long vrijemeDo, String base_uri) {
    RestVozila r = new RestVozila(base_uri);
    return r.getVozila(vrijemeOd, vrijemeDo);
  }

  public OdgovorListaVozila getVozilaId(int id, String base_uri) {
    RestVozila r = new RestVozila(base_uri);
    return r.getVozilaID(id);
  }

  public OdgovorListaVozila getVozilaId(int id, long vrijemeOd, long vrijemeDo, String base_uri) {
    RestVozila r = new RestVozila(base_uri);
    return r.getVozilaID(id, vrijemeOd, vrijemeDo);
  }

  public void getVoziloStartStop(int id, boolean startStop, String base_uri) {
    RestVozila r = new RestVozila(base_uri);
    r.getVoziloPracenje(id, startStop);
  }

  public void postVoznja(String red, int id, int broj, String base_uri) {
    RestVozila r = new RestVozila(base_uri);
    r.postNovoVoznje(red, id, broj);
  }

  static class RestVozila {
    private final WebTarget webTarget;
    private final Client client;


    public RestVozila(String base_uri) {
      this.client = ClientBuilder.newClient();
      this.webTarget = this.client.target(base_uri).path("nwtis/v1/api/simulacije");
    }

    public OdgovorListaVozila getVozila(long vrijemeOd, long vrijemeDo) {
      WebTarget resource = webTarget;

      resource = resource.queryParam("od", vrijemeOd);
      resource = resource.queryParam("do", vrijemeDo);
      Response odg = resource.request().get();
      if (odg.getStatus() == 200) {
        String odgovor = odg.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pVozila = jb.fromJson(odgovor, OdgovorListaVozila.class);
        if (pVozila.getVoznje() == null)
          pVozila.setVoznje(new ArrayList<>());
        return pVozila;
      }
      return new OdgovorListaVozila("ERROR " + odg.getStatus());
    }

    public OdgovorListaVozila getVozilaID(int id) {
      WebTarget resource = webTarget;

      resource = resource.path("vozilo/" + id);
      Response odg = resource.request().get();
      if (odg.getStatus() == 200) {
        String odgovor = odg.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pVozila = jb.fromJson(odgovor, OdgovorListaVozila.class);
        if (pVozila.getVoznje() == null)
          pVozila.setVoznje(new ArrayList<>());
        return pVozila;
      }
      return new OdgovorListaVozila("ERROR " + odg.getStatus());
    }

    public OdgovorListaVozila getVozilaID(int id, long vrijemeOd, long vrijemeDo) {
      WebTarget resource = webTarget;

      resource = resource.path("vozilo/" + id);
      resource = resource.queryParam("od", vrijemeOd);
      resource = resource.queryParam("do", vrijemeDo);
      Response odg = resource.request().get();
      if (odg.getStatus() == 200) {
        String odgovor = odg.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pVozila = jb.fromJson(odgovor, OdgovorListaVozila.class);
        if (pVozila.getVoznje() == null)
          pVozila.setVoznje(new ArrayList<>());
        return pVozila;
      }
      return new OdgovorListaVozila("ERROR " + odg.getStatus());
    }

    public void getVoziloPracenje(int id, boolean startStop) {
      WebTarget resource = webTarget;
      resource = resource.path("vozilo/" + id + (startStop ? "start" : "stop"));
      resource.request().get();
    }

    public void postNovoVoznje(String linijaCsv, int id, int brojReda) {
      WebTarget resource = webTarget;
      Vozilo vozilo = this.parsiratiRed(linijaCsv, id, brojReda);
      resource.request(MediaType.APPLICATION_JSON)
          .post(Entity.entity(vozilo, MediaType.APPLICATION_JSON));
    }

    private Pattern predlozakRed =
        Pattern.compile("^(?<vrijeme>\\d+),(?<brzina>-?\\d+([.]\\d+)?),(?<snaga>-?\\d+([.]\\d+)?)"
            + ",(?<struja>-?\\d+([.]\\d+)?),(?<visina>-?\\d+([.]\\d+)?),(?<gpsBrzina>-?\\d+([.]\\d+)?),(?<tempVozila>\\d+)"
            + ",(?<postotakBaterije>\\d+),(?<naponBaterije>-?\\d+([.]\\d+)?),(?<kapacitetBaterija>\\d+),(?<tempBaterija>\\d+)"
            + ",(?<preostaloKm>-?\\d+([.]\\d+)?),(?<ukupnoKm>-?\\d+([.]\\d+)?),(?<gpsSirina>-?\\d+([.]\\d+)?)"
            + ",(?<gpsDuzina>-?\\d+([.]\\d+)?)$");
    private Matcher poklapanjeRed;

    private Vozilo parsiratiRed(String red, int idVozila, int brojReda) {
      this.poklapanjeRed = this.predlozakRed.matcher(red);
      var statusRed = this.poklapanjeRed.matches();
      if (statusRed) {
        return new Vozilo(idVozila, brojReda, Long.valueOf(this.poklapanjeRed.group("vrijeme")),
            Double.valueOf(this.poklapanjeRed.group("brzina")),
            Double.valueOf(this.poklapanjeRed.group("snaga")),
            Double.valueOf(this.poklapanjeRed.group("struja")),
            Double.valueOf(this.poklapanjeRed.group("visina")),
            Double.valueOf(this.poklapanjeRed.group("gpsBrzina")),
            Integer.valueOf(this.poklapanjeRed.group("tempVozila")),
            Integer.valueOf(this.poklapanjeRed.group("postotakBaterije")),
            Double.valueOf(this.poklapanjeRed.group("naponBaterije")),
            Integer.valueOf(this.poklapanjeRed.group("kapacitetBaterija")),
            Integer.valueOf(this.poklapanjeRed.group("tempBaterija")),
            Double.valueOf(this.poklapanjeRed.group("preostaloKm")),
            Double.valueOf(this.poklapanjeRed.group("ukupnoKm")),
            Double.valueOf(this.poklapanjeRed.group("gpsSirina")),
            Double.valueOf(this.poklapanjeRed.group("gpsDuzina")));
      }
      return null;
    }
  }
}
