import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Message {
    public static final String MESSAGE_RELEASE = "release";
    public static final String MESSAGE_REQUEST = "request";
    public static final String MESSAGE_PRINT = "print";
    public static final String MESSAGE_ACK = "ack";
    public static final String MESSAGE_DONE = "done";
    public static final String MESSAGE_TOKEN = "token";

    private int srcId;
    private int srcPort;
    private String tag;
    private int timestamp;

    public Message(int srcId, int srcPort, String tag, int timestamp) {
        this.srcId = srcId;
        this.srcPort = srcPort;
        this.tag = tag;
        this.timestamp = timestamp;
    }

    public Message(String serializedMsg) {
        String[] strings = serializedMsg.split(" ");
        this.srcId = Integer.parseInt(strings[0]);
        this.srcPort = Integer.parseInt(strings[1]);
        this.tag = strings[2];
        this.timestamp = Integer.parseInt(strings[3]);
    }

    public int getSrcId() {
        return srcId;
    }

    @Override
    public String toString() {
        return srcId + " " + srcPort + " " + tag + " " + timestamp;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public String getTag() {
        return tag;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public static void sendMsg(Message msg, Integer port) {
        try {
            Socket socket = new Socket("localhost", port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(msg.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMsg(String msg, Integer port) {
        try {
            Socket socket = new Socket("localhost", port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMsg(ServerSocket serverSocket){
        try {
            Socket lightweightSocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(lightweightSocket.getInputStream()));
            String line = bufferedReader.readLine();
            lightweightSocket.close();
            return line;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
