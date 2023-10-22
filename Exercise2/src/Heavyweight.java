import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Heavyweight implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int otherPort;
    private boolean token;
    private int port;
    private int id;

    public Heavyweight(boolean token, int id, int port, int otherPort) {
        this.id = id;
        this.port = port;
        this.otherPort = otherPort;
        this.token = token;
    }

    @Override
    public void run() {
        if(token){
            startServer();
            connectToHeavy();
        }else{
            connectToHeavy();
            startServer();
        }

    }

    private void startServer(){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();


            // create a new thread for the client connected
            //Thread t = new Thread();


            // read data from the client
            // send data to the client

            //otherHeavySocket.close();
            //serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectToHeavy(){
        try {
            //Thread.sleep(1000);

            clientSocket = new Socket("localhost", otherPort);

            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
