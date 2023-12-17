package classes;

import business.FileManager;
import business.WebSocketServerEndpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class NodeFirstLayer extends Node {
    private static final int MILISECONDS_TIME_TO_WAIT = 10000;

    public NodeFirstLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        super(nodePorts, linkedNodes, id, port, clientPort, nodesPerLayer);
        Thread listenLightweights = new Thread(this::waitToReplicate);
        listenLightweights.start();
    }

        private void waitToReplicate() {
            while(true){
                try {
                    sleep(MILISECONDS_TIME_TO_WAIT);
                    secondLayerBroadcast(new Message(replicatedHashmap, port, Message.MESSAGE_TYPE_REPLICATED_HASHMAP));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    @Override
    protected void processMessage() {
        while (true) {
            Message msg = Message.getMessage(nodeServerSocket);

            switch (msg.getMessageType()) {
                case Message.MESSAGE_TYPE_REPLICATED_HASHMAP:
                    System.out.println("Node " + id + " received replicated hashmap " + msg.getReplicatedHashmap().toString() + " from core layer");
                    // 1. Update replicatedHashmap
                    replicatedHashmap = msg.getReplicatedHashmap();
                    // 2. Write new log
                    FileManager.writeNewLog(this.id, replicatedHashmap);
                    WebSocketServerEndpoint.sentToAllSessions(id + ":" + replicatedHashmap.toString());
                    break;
                case Message.MESSAGE_TYPE_TRANSACTION:
                    System.out.println("Node " + id + " received transaction " + msg.getPayloadTransaction().toString() + " from client");
                    // 1. Execute transaction operations
                    executeTransaction(msg.getPayloadTransaction());
                    // 2. Send OK to client
                    Message.sendMessage(new Message(replicatedHashmap, port, Message.MESSAGE_TYPE_OK), clientPort);
                    break;
                default:
                    throw new RuntimeException("Unknown message type incoming to the first layer " + msg.getMessageType());
            }
        }
    }
}
