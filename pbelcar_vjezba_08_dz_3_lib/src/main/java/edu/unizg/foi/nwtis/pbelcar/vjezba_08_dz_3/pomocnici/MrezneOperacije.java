package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa MrezneOperacije.
 */
public class MrezneOperacije {


  /**
   * Šalje zahtjev poslužitelju.
   *
   * @param adresa adresa poslužitelja
   * @param mreznaVrata mrežna vrata poslužitelja
   * @param poruka tekst poruke koja se šalje
   * @return odgovor. Ako nije u redu vraća se null, inače primljeni odgovor od poslužitelja
   */
  public static String posaljiZahtjevPosluzitelju(String adresa, int mreznaVrata, String poruka)
      throws IOException {
    try (Socket mreznaUticnica = new Socket(adresa, mreznaVrata)) {
      BufferedReader citac =
          new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
      OutputStream out = mreznaUticnica.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      pisac.print(poruka);
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      var odgovor = citac.readLine();
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
      return odgovor;
    } catch (IOException e) {
      throw e;
    }
  }
}
