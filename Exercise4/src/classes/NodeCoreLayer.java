package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        super(nodePorts, linkedNodes, id, port);
    }

    private void waitForClient() {
        Message message = Message.getMessage(nodeServerSocket);

    }

    @Override
    public void run() {
        startNodeServer();
        while(true){
            waitForClient();
        }
    }
}
