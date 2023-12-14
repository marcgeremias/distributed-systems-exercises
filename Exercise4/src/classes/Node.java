package classes;

import business.FileManager;


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
                writeLog(operation.getKey(),operation.getValue()); // w (a,b) -> write the value b in the hashmap with key a
            }else if(operation.getType().equals(Operation.OPERATION_READ)){
                readLog(operation.getKey()); // r (a) -> read the value of key a from the hashmap
            }
        }
        FileManager.writeNewLog(this.id, replicatedHashmap);
    }

    private void writeLog(int key, int value) {
        replicatedHashmap.put(key,value);
        System.out.println("Node " + id + " wrote in " + key + " -> Value of:" + value);
    }

    private void readLog(int key) {
        if(replicatedHashmap.containsKey(key)){
            System.out.println("Node " + id + " read " + key + " -> " + replicatedHashmap.get(key));
        }else{
            System.out.println("ERROR: Node " + id + " read " + key + " is NULL");
        }
    }

    /*
    void sendOKToSrc(Message message) {
        ArrayList<Integer> destPorts = new ArrayList<>();
        int srcPort = message.getSrcPort();
        // Getting all ports into destPorts except the source port
        for (String node : linkedNodes) {
            if (nodePorts.get(node) != srcPort) {
                destPorts.add(nodePorts.get(node));
            }
        }
        // Considering that the core layer is the only one that works with the eager replication
        // We need to run all the nodes sending OK to the source
        for (Integer destPort : destPorts) {
            sendOK(message, srcPort, destPort);
        }
    }
     */

    void sendOKMsg(Message message, int srcPort, int destPort) {
        // If I receive a message, send OK to the node that sent the message
        Message msgOK = new Message(message.getPayloadTransaction(), Message.MESSAGE_TYPE_OK, destPort);
        System.out.println("Port: " + destPort + ", send a OK message to port: " + srcPort);
        Message.sendMessage(msgOK, srcPort);
    }

    protected abstract void processMessage(Message msg);

}
