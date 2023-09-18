import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterServer {
    private static int variable;
    private ServerSocket serverSocket;
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;


    public MasterServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                socket = serverSocket.accept();
                System.out.println("New request to manage");
                manageRequest(socket);
                // TODO TANCAR Socket i a fer el contestar client
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void manageRequest(Socket socket) throws IOException {
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String request = bufferedReader.readLine();
        System.out.println("Request: " + request);
        if(request.equals("read")){
            bufferedWriter.write(Integer.toString(variable));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }else{
            variable = Integer.parseInt(request);
            System.out.println("New value: " + variable);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        MasterServer masterServer = new MasterServer(serverSocket);
        variable = 0;
        masterServer.startServer();
    }

}