import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Heavyweight extends Thread {
    private static final int NUM_LIGHTWEIGHTS = 3;
    private ArrayList<Integer> lightweightPorts;
    private ServerSocket serverSocket;
    private int answersfromLightweight;
    private int otherHeavyPort;
    private boolean token;
    private String id;
    private int myPort;

    public Heavyweight(boolean token, String id, int myPort, int otherHeavyPort) {
        answersfromLightweight = 0;
        this.otherHeavyPort = otherHeavyPort;
        this.token = token;
        this.myPort = myPort;
        this.id = id;

        generateLightweightPorts();
    }
    private void generateLightweightPorts() {
        lightweightPorts = new ArrayList<>();
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) {
            lightweightPorts.add(myPort + i + 1);
        }
    }

    private int getCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        return (int) currentTimeMillis;
    }

    private void sendActionToLightweight() {
        for (Integer lightweightPort : lightweightPorts) {
            try {
                System.out.println("Heavyweight " + id + " sending action to lightweight " + lightweightPort);
                Socket lightweightSocket = new Socket("localhost", lightweightPort);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(lightweightSocket.getOutputStream()));
                bufferedWriter.write("print");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                lightweightSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void listenLightweight() {
        try {
            Socket lightweightSocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(lightweightSocket.getInputStream()));
            bufferedReader.readLine();
            lightweightSocket.close();
            answersfromLightweight++;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendTokenToHeavyweight() {
        try {
            Socket heavySocket = new Socket("localhost", otherHeavyPort);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(heavySocket.getOutputStream()));
            bufferedWriter.write("token");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            heavySocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void listenHeavyweight(){
        try {
            Socket heavySocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(heavySocket.getInputStream()));
            bufferedReader.readLine();
            heavySocket.close();
            token = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void summonLightweights() {
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) {
            Lightweight lightweight = new Lightweight(id, i, myPort, lightweightPorts.get(i));
            lightweight.start();
        }
    }
    private void startServer(){
        try {
            serverSocket = new ServerSocket(myPort);
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
        startServer();
        summonLightweights();
        while (true) {
            while (!token) listenHeavyweight();
            System.out.println("Heavyweight " + id + " received token");
            for (int i=0; i<NUM_LIGHTWEIGHTS; i++) {
                sendActionToLightweight();
            }

            /*while(answersfromLightweight < NUM_LIGHTWEIGHTS) {
                listenLightweight();
            }*/

            waitSecond(); // DEBUGGING

            token = false;
            sendTokenToHeavyweight();
        }
    }
}
