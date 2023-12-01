package classes;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node implements Runnable{
    protected HashMap<String,Integer> nodePorts;
    protected ArrayList<String> linkedNodes;
    protected String id;
    protected Integer port;

    public Node(HashMap<String,Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
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

    public String showNodeData() {
        StringBuilder result = new StringBuilder();

        result.append("id: ").append(id).append("\n");
        result.append("port: ").append(port).append("\n");

        result.append("nodePorts:\n");
        for (String key : nodePorts.keySet()) {
            result.append("\t").append(key).append(": ").append(nodePorts.get(key)).append("\n");
        }

        result.append("linkedNodes:\n");
        for (String linkedNode : linkedNodes) {
            result.append("\t").append(linkedNode).append("\n");
        }

        return result.toString();
    }

}
