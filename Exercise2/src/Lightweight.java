import java.io.*;
import java.net.Socket;

public class Lightweight extends Thread{
    private static final int NUM_PRINTS = 10;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private LamportClock clock;
    private Socket heavySocket;
    private String heavyId;
    private int port;
    private int id;

    public Lightweight(LamportClock clock, String heavyId, int id, int port) {
        this.heavyId = heavyId;
        this.clock = clock;
        this.port = port;
        this.id = id;
    }

    public String getFullId() {
        return heavyId + id;
    }

    public void connectToHeavy() {
        try {
            heavySocket = new Socket("localhost", port);
            bufferedReader = new BufferedReader(new InputStreamReader(heavySocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(heavySocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        connectToHeavy();
        System.out.println("Lightweight " + getFullId() + " connected to Heavyweight " + heavyId);
        while(true){
            waitHeavyWeight();
            // TODO requestCS();
            for (int i=0; i<NUM_PRINTS; i++){
                System.out.println("I'm lightweight process " + getFullId());
                Heavyweight.waitSecond();
            }
            // TODO releaseCS();
            // TODO notifyHeavyWeight();
        }
    }

    private void waitHeavyWeight() {
        try {
            bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
