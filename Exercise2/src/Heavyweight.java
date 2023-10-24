import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Heavyweight extends Thread {
    private static final int NUM_LIGHTWEIGHTS = 3;
    private ArrayList<Socket> lightweightsSockets;
    private BufferedWriter otherHeavyOut;
    private BufferedReader otherHeavyIn;
    private ServerSocket serverSocket;
    private int answersfromLightweigth;
    private Socket clientSocket;
    private int otherPort;
    private boolean token;
    private String id;
    private int port;

    public Heavyweight(boolean token, String id, int port, int otherPort) {
        lightweightsSockets = new ArrayList<>();
        answersfromLightweigth = 0;
        this.otherPort = otherPort;
        this.token = token;
        this.port = port;
        this.id = id;
    }

    private void sendTokenToHeavyweight() {
        try {
            otherHeavyOut.write("token");
            otherHeavyOut.newLine();
            otherHeavyOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitSecond() {
        try {
            sleep(1000);
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

            while(answersfromLightweigth < NUM_LIGHTWEIGHTS) {
                listenLightweight();
            }

            //waitSecond(); // DEBUGGING
            token = false;
            sendTokenToHeavyweight(); // Pass the token
        }
    }



    private void summonLightweights() {
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) {
            Lightweight lightweight = new Lightweight(new LamportClock(), id, i, port);
            lightweight.start();
            // TODO Create ports for each lightweight process and pass them to the Lightwheight constructor
            try {
                clientSocket = serverSocket.accept();
                lightweightsSockets.add(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendActionToLightweight() {
        for (Socket socket : lightweightsSockets) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("print");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void listenLightweight() {
        for (Socket socket : lightweightsSockets) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedReader.readLine();
                answersfromLightweigth++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void listenHeavyweight(){
        try {
            otherHeavyIn.readLine();
            token = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startServer(){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            otherHeavyIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectToHeavy(){
        try {
            clientSocket = new Socket("localhost", otherPort);
            otherHeavyOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
