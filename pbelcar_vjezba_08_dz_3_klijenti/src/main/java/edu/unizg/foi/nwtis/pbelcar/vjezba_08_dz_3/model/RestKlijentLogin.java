package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.model;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class RestKlijentLogin {

  public RestKlijentLogin() {}

  public Boolean getLogin(String base_uri, String korIme, String lozinka) {
    var rL = new RestLogin(base_uri);
    return rL.postLogin(korIme, lozinka);
  }

  static class RestLogin {
    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    public RestLogin(String base_uri) {
      this.client = ClientBuilder.newClient();
      this.webTarget = this.client.target(base_uri).path("nwtis/v1/api/login");
    }

    public Boolean postLogin(String korIme, String lozinka) throws ClientErrorException {
      WebTarget resource = webTarget;
      Form form = new Form();
      form.param("korIme", korIme);
      form.param("lozinka", lozinka);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor =
          request.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));

      if (restOdgovor.getStatus() == 202) {
        return true;
      } else if (restOdgovor.getStatus() == 403) {
        return false;
      } else {
        return null;
      }
    }
  }
}
