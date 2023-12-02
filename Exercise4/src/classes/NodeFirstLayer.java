package classes;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeFirstLayer extends Node {

    public NodeFirstLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort) {
        super(nodePorts, linkedNodes, id, port, clientPort);
    }

    @Override
    protected void processMessage(Message msg) {
        Transaction transaction = msg.getPayloadTransaction();
        System.out.println("Node " + id + " from layer " + this.getClass().getSimpleName() + " received transaction " + transaction.toString());
    }
}
