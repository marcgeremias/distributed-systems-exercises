package business;

import classes.Transaction;

import java.util.ArrayList;

public class ClientManager {
    public void init(){
        ArrayList<Transaction> transactions = FileManager.getTransactions();
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }
}

