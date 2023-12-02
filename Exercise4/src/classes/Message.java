package classes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Message implements Serializable {
    public static final int MESSAGE_TYPE_TRANSACTION = 0;
    public static final int MESSAGE_TYPE_REPLICATED_HASHMAP = 1;
    public static final int MESSAGE_TYPE_OK = 2;
    public static final int MESSAGE_TYPE_KO = -1;
    private HashMap<Integer,Integer> replicatedHashmap;
    private Transaction payloadTransaction;
    private int messageType;

    public Message(Transaction payloadTransaction) {
        this.payloadTransaction = payloadTransaction;
        this.replicatedHashmap = null;
        this.messageType = MESSAGE_TYPE_TRANSACTION;
    }

    public Message(HashMap<Integer, Integer> replicatedHashmap) {
        this.replicatedHashmap = replicatedHashmap;
        this.payloadTransaction = null;
        this.messageType = MESSAGE_TYPE_REPLICATED_HASHMAP;
    }

    public Message() {
        this.payloadTransaction = null;
        this.replicatedHashmap = null;
        this.messageType = MESSAGE_TYPE_OK;
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

    @Override
    public String toString() {
        return "Message{" +
            "payloadTransaction=" + payloadTransaction +
            '}';
    }
}
