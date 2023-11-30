package classes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Message {
    private final String payload;
    public Message(String payload) {
        this.payload = payload;
    }

    public static void sendMessage(Message message, Integer port) {
        try {
            Socket socket = new Socket("localhost", port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(message.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Message getMessage(ServerSocket serverSocket){
        try {
            Socket lightweightSocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(lightweightSocket.getInputStream()));
            String line = bufferedReader.readLine();
            lightweightSocket.close();
            return new Message(line);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return payload;
    }
}
