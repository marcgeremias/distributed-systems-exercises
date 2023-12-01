package classes;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeSecondLayer extends Node{

    public NodeSecondLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        super(nodePorts, linkedNodes, id, port);
    }

    @Override
    public void run() {
        while(true);
    }
}
