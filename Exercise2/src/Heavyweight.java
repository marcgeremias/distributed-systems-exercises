import java.io.*;
import java.net.ServerSocket;
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

    private void sendActionToLightweights() {
        for (Integer lightweightPort : lightweightPorts) {
                Message.sendMsg(Message.MESSAGE_PRINT, lightweightPort);
        }
    }

    private void listenLightweight() {
        Message.getMsg(serverSocket);
        answersfromLightweight++;
    }

    private void sendTokenToHeavyweight() {
        Message.sendMsg(Message.MESSAGE_TOKEN, otherHeavyPort);
    }

    private void listenHeavyweight(){
        Message.getMsg(serverSocket);
        token = true;
    }

    private void summonLightweights() {
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) {
            ArrayList<Integer> portsWithoutMe = new ArrayList<>(lightweightPorts);
            portsWithoutMe.remove(i);
            // TODO Implmement interface or abstract class for lightweight
            if(id.equals(Main.PROCESS_A_ID)){
                LamportLightweight lightweight = new LamportLightweight(i, myPort, lightweightPorts.get(i), portsWithoutMe);
                lightweight.start();
            }else{
                AgrawalaLightweight lightweight = new AgrawalaLightweight(i, myPort, lightweightPorts.get(i), portsWithoutMe);
                lightweight.start();
            }
        }
        System.out.println("Summoned and started all lightweight processes from heavyweight " + id);
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

            sendActionToLightweights();

            while(answersfromLightweight < NUM_LIGHTWEIGHTS) {
                listenLightweight();
            }

            token = false;
            answersfromLightweight = 0;
            sendTokenToHeavyweight();
        }
    }
}
