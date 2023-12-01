package classes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Message implements Serializable {
    private Transaction payloadTransaction;

    public Message(Transaction payloadTransaction) {
        this.payloadTransaction = payloadTransaction;
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

    @Override
    public String toString() {
        return "Message{" +
            "payloadTransaction=" + payloadTransaction +
            '}';
    }
}
