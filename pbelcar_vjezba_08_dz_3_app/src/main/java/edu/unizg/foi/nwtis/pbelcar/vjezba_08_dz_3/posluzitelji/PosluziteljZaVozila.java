package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.posluzitelji;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.poluzitelji.radnici.RadnikZaVozilo;

public class PosluziteljZaVozila implements Runnable {
  CentralniSustav centralniSustav;

  private InetSocketAddress adresa;
  private static ExecutorService executor;
  private volatile List<Future<Boolean>> odgovori = new ArrayList<Future<Boolean>>();


  public PosluziteljZaVozila(CentralniSustav centSus) {
    this.centralniSustav = centSus;
    this.adresa = new InetSocketAddress(this.centralniSustav.mreznaVrataVozila);
  }

  public void run() {
    try {
      this.pokreniServer();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }


  private void pokreniServer() throws IOException {
    var server = AsynchronousServerSocketChannel.open().bind(this.adresa);

    this.executor = Executors.newVirtualThreadPerTaskExecutor();

    try {
      while (true) {
        Future<AsynchronousSocketChannel> kanalPrihvacanja = server.accept();
        AsynchronousSocketChannel kanalKlijenta = kanalPrihvacanja.get();
        odgovori.add(this.executor.submit(() -> pokreniRadnika(kanalKlijenta)));
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    server.close();
  }


  private boolean pokreniRadnika(AsynchronousSocketChannel kanalKlijenta) {
    try {
      var radnik = new RadnikZaVozilo(kanalKlijenta, this.centralniSustav);
      var dretva = Thread.startVirtualThread(radnik);
      dretva.join();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
