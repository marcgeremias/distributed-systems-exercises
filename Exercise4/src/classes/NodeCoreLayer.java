package classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {
    private final int NOT_READONLY = 0;
    private final int READONLY = 1;
    private final int INVALID = -1;

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        super(nodePorts, linkedNodes, id, port);
    }

    @Override
    public void run() {
        while(true){
            Message line = Message.getMessage(nodeServerSocket);

        }
    }
}
