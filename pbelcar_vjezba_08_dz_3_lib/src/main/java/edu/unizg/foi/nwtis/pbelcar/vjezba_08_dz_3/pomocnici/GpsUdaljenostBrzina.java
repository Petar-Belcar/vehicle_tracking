package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.pomocnici;

/**
 * Klasa GpsUdaljenostBrzina.
 */
public class GpsUdaljenostBrzina {

  // implementacija Haversine formule preuzeta iz
  // https://introcs.cs.princeton.edu/java/12types/GreatCircle.java.html
  /**
   * Računa udaljenost između dvije GPS pozicije u nm.
   *
   * @param lat1 gps širina 1. pozicije
   * @param lon1 gps dužina 1. pozicije
   * @param lat2 gps širina 2. pozicije
   * @param lon2 gps dužina 2. pozicije
   * @return udaljenost u nm
   */

  private static double udaljenostNm(double lat1, double lon1, double lat2, double lon2) {
    double x1 = Math.toRadians(lat1);
    double y1 = Math.toRadians(lon1);
    double x2 = Math.toRadians(lat2);
    double y2 = Math.toRadians(lon2);

    double a = Math.pow(Math.sin((x2 - x1) / 2), 2)
        + Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin((y2 - y1) / 2), 2);

    // great circle distance in radians
    double angle = 2 * Math.asin(Math.min(1, Math.sqrt(a)));

    // convert back to degrees
    angle = Math.toDegrees(angle);

    // each degree on a great circle of Earth is 60 nautical miles
    double distance = 60 * angle;

    return distance;
  }

  /**
   * Računa udaljenost između dvije GPS pozicije u km.
   *
   * @param x1 gps širina 1. pozicije
   * @param x2 gps dužina 1. pozicije
   * @param y1 gps širina 2. pozicije
   * @param y2 gps dužina 2. pozicije
   * @return udaljenost u km
   */
  public static double udaljenostKm(double x1, double x2, double y1, double y2) {

    double distance = udaljenostNm(x1, x2, y1, y2) * 1.852;

    return distance;
  }
}
