package classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node implements Runnable{
    protected HashMap<Integer,Integer> replicatedHashmap;
    protected HashMap<String,Integer> nodePorts;
    protected ArrayList<String> linkedNodes;
    protected ServerSocket nodeServerSocket;
    protected String id;
    protected Integer port;
    protected Integer clientPort;

    public Node(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort) {
        this.replicatedHashmap = new HashMap<>();
        this.nodePorts = nodePorts;
        this.linkedNodes = linkedNodes;
        this.clientPort = clientPort;
        this.id = id;
        this.port = port;

        startNodeServer();
    }

    @Override
    public String toString() {
        return "Node {" +
                "nodePorts=" + nodePorts +
                ", linkedNodes=" + linkedNodes +
                ", id='" + id + '\'' +
                ", port=" + port +
                ", type=" + this.getClass().getSimpleName() +
                '}';
    }

    private void startNodeServer() {
        try {
            this.nodeServerSocket = new ServerSocket(nodePorts.get(id));
            System.out.println("Node " + id + " started server on port " + nodePorts.get(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(true){
            Message msg = Message.getMessage(nodeServerSocket);
            processMessage(msg);
        }
    }

    protected void layerBroadcast(Message msg){
        for (String linkedNode : linkedNodes) {
            Message.sendMessage(msg, nodePorts.get(linkedNode));
        }
    }

    protected abstract void processMessage(Message msg);

}
