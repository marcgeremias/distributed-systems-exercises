import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Heavyweight extends Thread {
    private static final int NUM_LIGHTWEIGHTS = 3;
    private ArrayList<Lightweight> lightweights;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int otherPort;
    private boolean token;
    private String id;
    private int port;

    public Heavyweight(boolean token, String id, int port, int otherPort) {
        this.otherPort = otherPort;
        this.token = token;
        this.port = port;
        this.id = id;
    }

    private void passToken() {
        token = false;
        try {
            bufferedWriter.write("token");
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sleepDuring(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        summonLightweights();

        // TODO Summon the 3 lightweight processes
        while (true) {
            while (!token) listenHeavyweight();
            System.out.println("Heavyweight " + id + " received token");
            for (int i=0; i<NUM_LIGHTWEIGHTS; i++) {
                sendActionToLightweight();
            }
            /*    while(answersfromLightweigth < NUM_LIGHTWEIGHTS) {
                    listenLightweight();
                }
            */
            sleepDuring(1000);
            passToken(); // Pass the token
        }
    }

    private void summonLightweights() {
        lightweights = new ArrayList<>();
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) { // ProcessLWA1,
            Lightweight lightweight = new Lightweight(new LamportClock(), id, i);
            lightweights.add(lightweight);
        }
        // TODO Keep interconnecting the lightweight processes
    }

    private void sendActionToLightweight() {


    }

    private void listenHeavyweight(){
        try {
            bufferedReader.readLine();
            token = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startServer(){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectToHeavy(){
        try {
            clientSocket = new Socket("localhost", otherPort);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
