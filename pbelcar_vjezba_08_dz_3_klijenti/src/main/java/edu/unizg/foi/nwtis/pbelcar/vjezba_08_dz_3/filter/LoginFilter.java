package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/mvc/kazne/*")
public class LoginFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    HttpSession session = req.getSession(false);

    if (session == null || !"true".equals(session.getAttribute("session"))) {
      System.out.println("NO VALID SESSION FOUND");
      res.sendRedirect(req.getContextPath() + "/mvc/login/info");
    } else {
      System.out.println("VALID SESSION FOUND");
      chain.doFilter(request, response);
    }
  }
}
