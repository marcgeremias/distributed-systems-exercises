import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Lightweight extends Thread{
    LamportClock clock;
    int[] requestQ;
    int numOkay;

    private static final int NUM_LIGHTWEIGHTS = 3;
    private static final int NUM_PRINTS = 10;
    private ArrayList<Integer> lightweightPorts;
    private ServerSocket serverSocket;
    private int heavyPort;
    private int myPort;
    private int myId;
    private boolean start;

    public Lightweight(int myId, int heavyPort, int myPort, ArrayList<Integer> portsWithoutMe) {
        this.lightweightPorts = portsWithoutMe;
        this.heavyPort = heavyPort;
        this.myPort = myPort;
        this.myId = myId;
        this.start = false;

        this.requestQ = new int[NUM_LIGHTWEIGHTS];
        this.clock = new LamportClock();
        this.numOkay = 0;
        for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) requestQ[i] = Integer.MAX_VALUE;
    }

    private void notifyHeavyWeight() {
        Message.sendMsg(Message.MESSAGE_DONE, heavyPort);
    }

    private void waitHeavyWeight() {
        while(!getStart());
    }

    private synchronized boolean getStart() {
        return this.start;
    }

    private synchronized void setStart(boolean start) {
        this.start = start;
    }


    private synchronized void requestCS() {
        clock.tick();
        requestQ[myId] = clock.getValue();
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_REQUEST, requestQ[myId]));
        while(!okayCS());
    }

    private void broadcastMsg(Message msg) {
        for (Integer lightweightPort : lightweightPorts) {
            Message.sendMsg(msg, lightweightPort);
        }
    }

    private void releaseCS() {
        clock.tick();
        requestQ[myId] = Integer.MAX_VALUE;
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_RELEASE, requestQ[myId]));
        numOkay = 0;
    }
    private synchronized boolean okayCS() {
        if(numOkay < NUM_LIGHTWEIGHTS - 1) return false;
        for(int i = 0; i < NUM_LIGHTWEIGHTS; i++){
            if(isGreater(requestQ[myId], myId, requestQ[i], i)){
                return false;
            }
            if(isGreater(requestQ[myId], myId, clock.getValue(), i)){
                return false;
            }
        }
        return true;
    }

    private boolean isGreater(int entry1, int pid1, int entry2, int pid2) {
        if(entry2 == Integer.MAX_VALUE) return false;
        return ((entry1 > entry2) || ((entry1 == entry2) && (pid1 > pid2)));
    }

    private void listenServerConnections() {
        while(true){
            String line = Message.getMsg(serverSocket);
            if (line.equals(Message.MESSAGE_PRINT)) {
                this.setStart(true);
                System.out.println("Lightweight " + myId + " ready to start");
            } else {
                Message msg = new Message(line);
                handleMsg(msg);
            }
        }
    }

    private void handleMsg(Message msg) {
        clock.receiveAction(msg.getTimestamp());
        if(msg.getTag().equals(Message.MESSAGE_REQUEST)){
            requestQ[msg.getSrcId()] = msg.getTimestamp();
            Message.sendMsg(new Message(myId, myPort, Message.MESSAGE_ACK, clock.getValue()), msg.getSrcPort());
        }else if(msg.getTag().equals(Message.MESSAGE_ACK)){
            numOkay++;
        } else if(msg.getTag().equals(Message.MESSAGE_RELEASE)){
            requestQ[msg.getSrcId()] = Integer.MAX_VALUE;
        }
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(myPort);
            Thread listenLightweights = new Thread(this::listenServerConnections);
            listenLightweights.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        startServer();
        while(true){
            waitHeavyWeight();
            requestCS();
            for (int i=0; i<NUM_PRINTS; i++){
                System.out.println("I'm lightweight process " + myId);
            }
            Heavyweight.waitSecond();
            this.setStart(false);
            releaseCS();
            notifyHeavyWeight();
        }
    }

}
