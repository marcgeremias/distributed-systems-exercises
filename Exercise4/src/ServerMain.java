import business.NodesManager;
import business.WebSocketServerEndpoint;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Starting WebSocket server
            Server server = new Server("localhost", 8080, "/", WebSocketServerEndpoint.class);
            server.start();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }

        // Starting nodes hierarchy
        NodesManager nodesManager = new NodesManager();
        nodesManager.init();
    }
}