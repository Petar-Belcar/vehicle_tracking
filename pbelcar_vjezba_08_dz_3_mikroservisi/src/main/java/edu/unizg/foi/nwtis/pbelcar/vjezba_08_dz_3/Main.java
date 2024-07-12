package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3;

import static io.helidon.config.ConfigSources.classpath;
import io.helidon.config.Config;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Main class.
 *
 */
@ApplicationPath("")
@ApplicationScoped
public class Main extends Application {

  private static Config konfiguracija;

  @PostConstruct
  public void buildConfig() {
    konfiguracija = Config.builder().disableEnvironmentVariablesSource()
        .sources(classpath("application.yaml")).build();
  }

  public static Config getKonfiguracija() {
    return konfiguracija;
  }
}
