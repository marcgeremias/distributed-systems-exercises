package classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node implements Runnable{
    protected HashMap<String,Integer> nodePorts;
    protected ArrayList<String> linkedNodes;
    protected ServerSocket nodeServerSocket;
    protected String id;
    protected Integer port;

    public Node(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        try {
            this.nodeServerSocket = new ServerSocket(nodePorts.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.nodePorts = nodePorts;
        this.linkedNodes = linkedNodes;
        this.id = id;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Node {" +
            "nodePorts=" + nodePorts +
            ", linkedNodes=" + linkedNodes +
            ", id='" + id + '\'' +
            ", port=" + port +
            ", type=" + this.getClass().getSimpleName() +
            '}';
    }
}
