package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model;

import java.util.ArrayList;
import java.util.List;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.podaci.Radar;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno.Odgovor;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocno.OdgovorListaRadari;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentRadari {
  public RestKlijentRadari() {

  }

  public OdgovorListaRadari getRadariJSON(String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    return r.getJSON();
  }


  public String getRadariResetJSON(String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    return r.getJSONReset().getOdgovor();
  }


  public OdgovorListaRadari getRadarPoIDJSON(int id, String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    return r.getJSONPoId(id);
  }


  public String getRadarProvjeraPoIDJSON(int id, String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    return r.getJSONProvjera(id).getOdgovor();
  }


  public void deleteIzbrisiSveRadarae(String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    r.deleteJSONBrisiSveRadare();
  }


  public void deleteIzbrisiRadarPoId(int id, String base_uri) {
    RestRadari r = new RestRadari(base_uri);
    r.deleteJSONBrisiRadarPoId(id);
  }


  static class RestRadari {
    private final WebTarget webTarget;
    private final Client client;


    public RestRadari(String base_uri) {
      this.client = ClientBuilder.newClient();
      this.webTarget = this.client.target(base_uri).path("nwtis/v1/api/radari");
    }

    public OdgovorListaRadari getJSON() {
      WebTarget resource = webTarget;
      // TODO: Why do I have these?
      List<Radar> radari = new ArrayList<>();

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pRadari = jb.fromJson(odgovor, OdgovorListaRadari.class);
        if (pRadari.getListaRadara() == null) {
          pRadari.setListaRadara(new ArrayList<Radar>());
        }
        return pRadari;
      }
      return new OdgovorListaRadari("ERROR " + restOdgovor.getStatus());
    }

    public Odgovor getJSONReset() {
      WebTarget resource = webTarget;
      resource = resource.path("reset");
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var odg = jb.fromJson(odgovor, Odgovor.class);
        return odg;
      }
      return new Odgovor("Error " + restOdgovor.getStatus());
    }

    public OdgovorListaRadari getJSONPoId(int id) {
      WebTarget resource = webTarget;
      List<Radar> radari = new ArrayList<>();

      resource = resource.path(String.valueOf(id));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pRadari = jb.fromJson(odgovor, OdgovorListaRadari.class);
        if (pRadari.getListaRadara() == null) {
          pRadari.setListaRadara(new ArrayList<Radar>());
        }
        return pRadari;
      }
      return new OdgovorListaRadari("ERROR " + restOdgovor.getStatus());
    }

    public Odgovor getJSONProvjera(int id) {
      WebTarget resource = webTarget;
      resource = resource.path(String.valueOf(id) + "/provjeri");

      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pRadari = jb.fromJson(odgovor, Odgovor.class);
        return pRadari;
      }
      return new Odgovor("ERROR " + restOdgovor.getStatus());
    }


    public void deleteJSONBrisiSveRadare() {
      WebTarget resource = webTarget;

      resource.request().delete();
    }


    public void deleteJSONBrisiRadarPoId(int id) {
      WebTarget resource = webTarget;
      resource = resource.path(String.valueOf(id));

      resource.request().delete();
    }
  }
}
