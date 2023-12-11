import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

public class WebSocketServer {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8080, "/", WebSocketServerEndpoint.class);

        try {
            server.start();
            while(true);
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }
}