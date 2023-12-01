package classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {
    private final int NOT_READONLY = 0;
    private final int READONLY = 1;
    private final int INVALID = -1;

    private ServerSocket coreServerSocket;

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        super(nodePorts, linkedNodes, id, port);
    }

    private Message waitForClient() {
        // Receive transaction from client
        return Message.getMessage(coreServerSocket);
    }

    private int identifyMsgType(Message message) {
        String readOnlyRegex = "b<(0|1|2)>,\\s+r\\(\\d+\\),\\s+r\\(\\d+\\),\\s+r\\(\\d+\\),\\s+c";
        String notReadOnlyRegex = "b,\\s+r\\(\\d+\\),\\s+w\\(\\d+,\\d+\\),\\s+r\\(\\d+\\),\\s+c";

        if (message.toString().matches(readOnlyRegex)) {
            return NOT_READONLY; // readOnly
        } else if (message.toString().matches(notReadOnlyRegex)) {
            return READONLY; // not readOnly
        } else {
            return INVALID; // invalid message
        }
    }

    private void startNodeServer() {
        try {
            coreServerSocket = new ServerSocket(nodePorts.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        startNodeServer();
        while(true){
            Message message = waitForClient();
            if (identifyMsgType(message) == 0){

            }
        }
    }
}
