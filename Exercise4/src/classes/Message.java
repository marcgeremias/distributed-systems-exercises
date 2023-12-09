package classes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Message implements Serializable {
    public static final int MESSAGE_TYPE_TRANSACTION = 0;           // From Client to any Node passing the Transaction
    public static final int MESSAGE_TYPE_REPLICATED_HASHMAP = 1;    // From Node to PASSIVE node
    public static final int MESSAGE_TYPE_TRANSACTION_RECIPE = 2;    // From Node to ACTIVE node
    public static final int MESSAGE_TYPE_OK = 3;                    // From EAGER Node to Client OK
    public static final int MESSAGE_TYPE_KO = -1;                   // From EAGER Node to Client KO
    private HashMap<Integer,Integer> replicatedHashmap;
    private Transaction payloadTransaction;
    private int messageType;

    public Message(Transaction payloadTransaction, int messageType) {
        this.payloadTransaction = payloadTransaction;
        this.replicatedHashmap = null;
        this.messageType = messageType;
    }

    public Message(HashMap<Integer, Integer> replicatedHashmap) {
        this.replicatedHashmap = replicatedHashmap;
        this.payloadTransaction = null;
        this.messageType = MESSAGE_TYPE_REPLICATED_HASHMAP;
    }

    public Message(int messageType) {
        this.payloadTransaction = null;
        this.replicatedHashmap = null;
        this.messageType = messageType;
    }

    public static void sendMessage(Message message, Integer port) {
        try {
            Socket socket = new Socket("localhost", port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message getMessage(ServerSocket serverSocket){
        try {
            Socket lightweightSocket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(lightweightSocket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            lightweightSocket.close();
            return message;
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public Transaction getPayloadTransaction() {
        return payloadTransaction;
    }

    public HashMap<Integer, Integer> getReplicatedHashmap() {
        return replicatedHashmap;
    }

    public int getMessageType() {
        return messageType;
    }

    /*public void executeOperation(Operation operation){
        if(operation.getType().equals(Operation.OPERATION_READ)){
            int newValue = operation.getValue();
            this.replicatedHashmap.put(operation.getKey(), newValue);
        } else if(operation.getType().equals(Operation.OPERATION_WRITE)){
            int lineToRead = operation.getKey();
            System.out.println("Line " + lineToRead + " - Read: " + this.replicatedHashmap.get(lineToRead));
        }else{
            throw new RuntimeException("Invalid operation type");
        }
    }*/

    @Override
    public String toString() {
        return "Message{" +
            "payloadTransaction=" + payloadTransaction +
            '}';
    }
}
