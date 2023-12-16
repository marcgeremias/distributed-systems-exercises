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
    private int sourcePort;
    // TODO: Add sourcePort to all the constructors?
    //  Not sure because it is only needed in the eager replication used in the core, not in the passive replication
    //  used in the first and second layers

    public Message(Transaction payloadTransaction, int messageType) {
        this.payloadTransaction = payloadTransaction;
        this.replicatedHashmap = null;
        this.messageType = messageType;
    }

    public Message(HashMap<Integer, Integer> replicatedHashmap, int sourcePort, int messageType) {
        this.replicatedHashmap = replicatedHashmap;
        this.payloadTransaction = null;
        this.messageType = messageType;
        this.sourcePort = sourcePort;
    }

    public Message(int messageType) {
        this.payloadTransaction = null;
        this.replicatedHashmap = null;
        this.messageType = messageType;
    }

    // Managing of OK messages from EAGER REPLICATION nodes from core layer
    public Message(Transaction payloadTransaction, int messageType, int sourcePort) {
        this.payloadTransaction = payloadTransaction;
        this.replicatedHashmap = null;
        this.messageType = messageType;
        this.sourcePort = sourcePort;
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

    public int getSrcPort() {
        return this.sourcePort;
    }

    @Override
    public String toString() {
        return "Message{" +
            "payloadTransaction=" + payloadTransaction +
            '}';
    }
}
