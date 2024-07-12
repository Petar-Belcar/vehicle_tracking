package edu.unizg.foi.nwtis.pbelcar.vjezba_08_dz_3.websocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/kazne")
public class WebSocketPosluzitelj {

  static Queue<Session> queue = new ConcurrentLinkedQueue<>();

  public static void send(String poruka) {

    try {
      for (Session session : queue) {
        if (session.isOpen()) {
          session.getBasicRemote().sendText(poruka);
        }
      }
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  @OnOpen
  public void openConnection(Session session, EndpointConfig conf) {
    queue.add(session);
    System.out.println("Otvorena veza.");
  }

  @OnClose
  public void closedConnection(Session session, CloseReason reason) {
    queue.remove(session);
    System.out.println("Zatvorena veza.");
  }

  @OnMessage
  public void Message(Session session, String poruka) {
    System.out.println("Primljena poruka: " + poruka);
    WebSocketPosluzitelj.send(poruka);
  }

  @OnError
  public void error(Session session, Throwable t) {
    queue.remove(session);
    System.out.println("Zatvorena veza zbog pogreške.");
  }
}
