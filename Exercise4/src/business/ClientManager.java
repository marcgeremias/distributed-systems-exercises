package business;

import classes.Transaction;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientManager {
    private ArrayList<Transaction> transactions;
    private HashMap<String,Integer> nodePorts;
    private ArrayList<String>[] linkedNodes;

    public void init(){
        transactions = FileManager.getTransactions();
        linkedNodes = FileManager.getLayerNodes();
        nodePorts = FileManager.readNodePorts();

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        // TODO Send transaction through a Message
    }
}

