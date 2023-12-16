package classes;

import business.FileManager;
import business.WebSocketServerEndpoint;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class Node implements Runnable{
    public static final int CORE_LAYER = 0;
    public static final int FIRST_LAYER = 1;
    public static final int SECOND_LAYER = 2;
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
        processMessage();
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

    protected void secondLayerBroadcast(Message msg){
        for(String node : nodesPerLayer[SECOND_LAYER]){
            Message.sendMessage(msg, nodePorts.get(node));
        }
    }

    // TODO: Check if this is correct becasue FIRST_LAYER const is not used anywhere
    protected void differentLayerBroadcast(Message msg){
        for(String node : getDifferentLayerLinkedNodes()){
            Message.sendMessage(msg, nodePorts.get(node));
        }
        System.out.println(getDifferentLayerLinkedNodes());
    }

    protected void executeTransaction(Transaction transaction){
        for(Operation operation : transaction.getOperations()){
            if(operation.getType().equals(Operation.OPERATION_WRITE)){
                replicatedHashmap.put(operation.getKey(),operation.getValue());
                //writeLog(operation.getKey(),operation.getValue()); // w (a,b) -> write the value b in the hashmap with key a
            }else if(operation.getType().equals(Operation.OPERATION_READ)){
                //readLog(operation.getKey()); // r (a) -> read the value of key a from the hashmap
            }
            FileManager.updateNodeLog(this.id, replicatedHashmap);
            WebSocketServerEndpoint.sendAllLogs();
        }
    }

    /*private void writeLog(int key, int value) {
        replicatedHashmap.put(key,value);
        System.out.println("Node " + id + " wrote in " + key + " -> Value of:" + value);
    }

    private void readLog(int key) {
        if(replicatedHashmap.containsKey(key)){
            System.out.println("Node " + id + " read " + key + " -> " + replicatedHashmap.get(key));
        }else{
            System.out.println("ERROR: Node " + id + " read " + key + " is NULL");
        }
    }*/

    protected abstract void processMessage();

}
