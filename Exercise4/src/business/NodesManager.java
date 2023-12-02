package business;

import classes.Node;
import java.util.ArrayList;

public class NodesManager {
    public static final Integer MAX_NUMBER_OF_LAYERS = 3;
    public void init() {
        ArrayList<Node> nodes = FileManager.readNodes();
        //nodes.forEach(System.out::println);
        for (Node node: nodes) new Thread(node).start();
    }
}
