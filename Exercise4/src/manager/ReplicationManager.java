package manager;

import classes.Node;
import java.util.ArrayList;
import java.util.HashMap;

public class ReplicationManager {
    private ArrayList<Node> nodes;

    public ReplicationManager() {
        this.nodes = new ArrayList<>();
    }
    public void init() {
        nodes = FileManager.getNodes();
        nodes.forEach(System.out::println);

    }
}
