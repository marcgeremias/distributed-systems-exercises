package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port) {
        super(nodePorts, linkedNodes, id, port);
    }

    private Message waitForClient() {
        // Receive transaction from client
        return Message.getMessage(serverSocket);
    }

    boolean isTransactionTypeCorrect(String message) {
        return identifyMessageType(message).equals("notReadOnly");
    }

    private String identifyMessageType(String message) {
        String readOnlyRegex = "b<(0|1|2)>,\\s+r\\(\\d+\\),\\s+r\\(\\d+\\),\\s+r\\(\\d+\\),\\s+c";
        String notReadOnlyRegex = "b,\\s+r\\(\\d+\\),\\s+w\\(\\d+,\\d+\\),\\s+r\\(\\d+\\),\\s+c";

        if (message.matches(readOnlyRegex)) {
            return "readOnly";
        } else if (message.matches(notReadOnlyRegex)) {
            return "notReadOnly";
        } else {
            return "invalid";
        }
    }

    @Override
    public void run() {
        startNodeServer();
        while(true){
            Message message = waitForClient();
            if(isTransactionTypeCorrect(message.toString())){
                System.out.println("Message received: " + message.toString());
            } else {
                System.out.println("Error: Message type incorrect. This node does not accept this type of message.");
            }
        }
    }
}
