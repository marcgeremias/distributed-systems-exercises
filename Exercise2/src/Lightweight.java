import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Lightweight extends Thread{
    private static final int NUM_PRINTS = 10;
    private ServerSocket serverSocket;
    private LamportClock clock;
    private String heavyId;
    private int heavyPort;
    private int myPort;
    private int id;

    public Lightweight(String heavyId, int id, int heavyPort, int myPort) {
        this.clock = new LamportClock();
        this.heavyPort = heavyPort;
        this.heavyId = heavyId;
        this.myPort = myPort;
        this.id = id;
    }

    @Override
    public void run() {
        startServer();
        System.out.println("Lightweight with port " + myPort + " started their server");
        while(true){
            waitHeavyWeight();
            // TODO requestCS();
            /*for (int i=0; i<NUM_PRINTS; i++){
                System.out.println("I'm lightweight process " + getFullId());
                Heavyweight.waitSecond();
            }*/
            // TODO releaseCS();
            // TODO notifyHeavyWeight();*/
        }
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(myPort);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitHeavyWeight() {
        try {
            Socket heavySocket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(heavySocket.getInputStream()));
            bufferedReader.readLine();
            heavySocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
