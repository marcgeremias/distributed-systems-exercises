package classes;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {
    private HashMap<String,Integer> nodePorts;
    private ArrayList<String> linkedNodes;
    private String id;
    private Integer port;

    public Node(HashMap<String,Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        this.nodePorts = nodePorts;
        this.linkedNodes = linkedNodes;
        this.id = id;
        this.port = port;
    }


    @Override
    public String toString() {
        return  "Node{" +
                "nodePorts=" + nodePorts +
                ", linkedNodes=" + linkedNodes +
                ", id='" + id + '\'' +
                ", port=" + port +
                ", type=" + this.getClass().getSimpleName() +
                '}';
    }
}
