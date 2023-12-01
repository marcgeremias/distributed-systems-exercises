package business;

import classes.Node;
import java.util.ArrayList;

public class NodesManager {
    public void init() {
        ArrayList<Node> nodes = FileManager.getNodes();

        nodes.forEach(System.out::println);
        for (Node node: nodes) {
            Thread thread = new Thread(node);
            thread.start();
        }
    }
}
