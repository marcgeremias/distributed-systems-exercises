package business;

import business.FileManager;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/demoApp")
public class WebSocketServerEndpoint {
    private static final Set<Session> sessions = new HashSet<>();

    static {
        // Starting a separate thread to send logs to all sessions every second
        new Thread(() -> {
            while (true) {
                for (Session session : sessions) {
                    try {
                        session.getBasicRemote().sendText(FileManager.readAllLogs());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("[SERVER]: Handshake successful - Connected - Session ID: " + session.getId());
        sessions.add(session);
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        // Handle incoming messages (if any)
        return message;
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("[SERVER]: Session " + session.getId() + " closed");
        sessions.remove(session);
    }
}
