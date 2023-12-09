package classes;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class Node implements Runnable{
    protected HashMap<Integer,Integer> replicatedHashmap;
    protected HashMap<String,Integer> nodePorts;
    protected ArrayList<String>[] nodesPerLayer;
    protected ArrayList<String> linkedNodes;
    protected ServerSocket nodeServerSocket;
    protected String id;
    protected Integer port;
    protected Integer clientPort;

    public Node(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        this.replicatedHashmap = new HashMap<>();
        this.nodesPerLayer = nodesPerLayer;
        this.nodePorts = nodePorts;
        this.linkedNodes = linkedNodes;
        this.clientPort = clientPort;
        this.id = id;
        this.port = port;

        startNodeServer();
    }

    @Override
    public void run() {
        while(true){
            Message msg = Message.getMessage(nodeServerSocket);
            processMessage(msg);
        }
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


    protected LinkedList<String> getSameLayerLinkedNodes(){
        LinkedList<String> sameLayerLinkedNodes = new LinkedList<>();
        int layer = switch (this.getClass().getSimpleName()) {
            case "NodeCoreLayer" -> 0;
            case "NodeFirstLayer" -> 1;
            case "NodeSecondLayer" -> 2;
            default -> throw new RuntimeException("Unknown layer when getting same layer linked nodes");
        };
        for(String node : linkedNodes) {
            if (nodesPerLayer[layer].contains(node)) {
                sameLayerLinkedNodes.add(node);
            }
        }
        return sameLayerLinkedNodes;
    }

    protected LinkedList<String> getDifferentLayerLinkedNodes(){
        LinkedList<String> differentLayerLinkedNodes = new LinkedList<>();
        int layer = switch (this.getClass().getSimpleName()) {
            case "NodeCoreLayer" -> 0;
            case "NodeFirstLayer" -> 1;
            case "NodeSecondLayer" -> 2;
            default -> throw new RuntimeException("Unknown layer when getting different layer linked nodes");
        };
        for(String node : linkedNodes) {
            if (!nodesPerLayer[layer].contains(node)) {
                differentLayerLinkedNodes.add(node);
            }
        }
        return differentLayerLinkedNodes;
    }

    protected void sameLayerBroadcast(Message msg){
        for(String node : getSameLayerLinkedNodes()){
            Message.sendMessage(msg, nodePorts.get(node));
        }
    }

    protected void differentLayerBroadcast(Message msg){
        for(String node : getDifferentLayerLinkedNodes()){
            Message.sendMessage(msg, nodePorts.get(node));
        }
    }

    protected void executeTransaction(Transaction transaction){
        for(Operation operation : transaction.getOperations()){
            if(operation.getType().equals(Operation.OPERATION_WRITE)){
                replicatedHashmap.put(operation.getKey(),operation.getValue());
                // TODO: Log write operation
            }else if(operation.getType().equals(Operation.OPERATION_READ)){
                // TODO: Log read operation
            }
        }
    }

    protected abstract void processMessage(Message msg);

}
