import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterServer {

    private static int variable;
    private static boolean busy;
    private ServerSocket serverSocket;

    public MasterServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New request to manage");
                ClientHandler clientThread = new ClientHandler(socket);
                clientThread.start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void updateVariable(int value) {
        variable = value;
    }

    public static int readVariable() {
        return variable;
    }

    public synchronized static boolean isBusy() {
        return busy;
    }

    public static void setBusy() {
        busy = true;
    }

    public static void cleanBusy() {
        busy = false;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        MasterServer masterServer = new MasterServer(serverSocket);
        cleanBusy();
        variable = 0;
        masterServer.startServer();
    }

}