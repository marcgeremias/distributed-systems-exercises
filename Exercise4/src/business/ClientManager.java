package business;

import classes.Message;
import classes.Transaction;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ClientManager {
    private ArrayList<Transaction> transactions;
    private HashMap<String,Integer> nodePorts;
    private ArrayList<String>[] linkedNodes;
    private ServerSocket clientServerSocket;
    private final int clientPort;

    public ClientManager(){
        transactions = FileManager.readTransactions();
        linkedNodes = FileManager.readLayerNodes();
        nodePorts = FileManager.readNodePorts();
        clientPort = FileManager.readClientPort();

        startClientServer();
    }

    private void startClientServer() {
        try {
            clientServerSocket = new ServerSocket(clientPort);
            System.out.println("Client started server on port " + clientPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(){
        for (Transaction transaction : transactions) {
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        Message msg = new Message(transaction, Message.MESSAGE_TYPE_TRANSACTION);
        Random random = new Random();
        int randomNode = random.nextInt(linkedNodes[transaction.getLayer()].size());
        Message.sendMessage(msg,nodePorts.get(linkedNodes[transaction.getLayer()].get(randomNode)));
        System.out.println("Sending to layer " + transaction.getLayer() + " node " + linkedNodes[transaction.getLayer()].get(randomNode));

        // Wait for response
        Message nodeResponse = Message.getMessage(clientServerSocket);
        if(nodeResponse.getMessageType() == Message.MESSAGE_TYPE_OK){
            System.out.println("Result of transaction " + transaction + " =\n" + nodeResponse.getReplicatedHashmap());
        }else if(nodeResponse.getMessageType() == Message.MESSAGE_TYPE_KO){
            throw new RuntimeException("Transaction " + transaction + " ERROR");
        }

        try {
            sleep(1000); // DEBUGGING
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

