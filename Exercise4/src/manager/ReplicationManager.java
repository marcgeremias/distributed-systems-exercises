package manager;

import classes.Node;
import java.util.ArrayList;
import java.util.HashMap;

public class ReplicationManager {
    private HashMap<String,Integer> nodePorts;
    private ArrayList<Node> nodes;
    public ReplicationManager() {
        this.nodePorts = new HashMap<>();
        this.nodes = new ArrayList<>();
    }
    public void init() {
        nodes = FileManager.getNodes();

        for (Node node: nodes){
            System.out.println(node);
        }
    }
}
