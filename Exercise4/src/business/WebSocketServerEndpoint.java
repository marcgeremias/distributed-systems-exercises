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

    // TODO OPERATIVE: Only send logs that have been modified since last time, not all of them
    public static void sendAllLogs() {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(FileManager.readAllLogs());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("[SERVER]: Handshake successful - Connected - Session ID: " + session.getId());
        // Send the current logs to the new client
        try {
            session.getBasicRemote().sendText(FileManager.readAllLogs());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
