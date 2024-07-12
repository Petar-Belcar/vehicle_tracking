package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.kontroler;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Controller
@Path("pocetak")
@RequestScoped
public class Kontroler {
  @Inject
  private Models model;

  @Inject
  private BindingResult bindingResult;

  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void json() {}
}
