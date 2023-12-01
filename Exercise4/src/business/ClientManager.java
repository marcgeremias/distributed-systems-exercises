package business;

import classes.Message;
import classes.Transaction;
import java.util.ArrayList;

public class ClientManager {

    public void init(){
        ArrayList<Transaction> transactions = FileManager.getTransactions();
        ArrayList<Message> messages = new ArrayList<>();

        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
            messages.add(new Message(transaction.toString()));
        }



    }
}

