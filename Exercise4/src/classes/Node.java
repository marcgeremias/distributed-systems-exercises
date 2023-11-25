package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
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
}
