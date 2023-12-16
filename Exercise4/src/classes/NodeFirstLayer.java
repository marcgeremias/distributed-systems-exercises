package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class NodeFirstLayer extends Node {
    private Timer timer;

    public NodeFirstLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        super(nodePorts, linkedNodes, id, port, clientPort, nodesPerLayer);
        timer = new Timer();

        startTimer();
    }

    private void startTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondLayerBroadcast(new Message(replicatedHashmap, port, Message.MESSAGE_TYPE_REPLICATED_HASHMAP));
                System.out.println("Replicated hashmap sent to the second layer");
            }
        }, 0, 10000);
    }

    public void stopTimer() {
        timer.cancel();
    }

    @Override
    protected void processMessage() {
        while (true) {
            Message msg = Message.getMessage(nodeServerSocket);

            switch (msg.getMessageType()) {
                case Message.MESSAGE_TYPE_REPLICATED_HASHMAP:
                    System.out.println("Received transaction " + msg.getPayloadTransaction().toString() + " from node port" + msg.getSrcPort());
                    // 1. Update replicatedHashmap
                    replicatedHashmap = msg.getReplicatedHashmap();
                    // 2. Lazy -> Execute transaction operations
                    executeTransaction(msg.getPayloadTransaction());
                    break;
                default:
                    throw new RuntimeException("Unknown message type incoming to the first layer");
            }
        }
    }
}
