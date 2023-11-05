import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LamportLightweight extends Thread{
    private static final int NUM_LIGHTWEIGHTS = 3;
    private static final int NUM_PRINTS = 1;
    private ArrayList<Integer> lightweightPorts;
    private ServerSocket serverSocket;
    private AtomicBoolean start;
    private LamportQueue queue;
    private int heavyPort;
    AtomicInteger numOkay;
    LamportClock clock;
    private int myPort;
    private int myId;

    public LamportLightweight(int myId, int heavyPort, int myPort, ArrayList<Integer> portsWithoutMe) {
        this.lightweightPorts = portsWithoutMe;
        this.heavyPort = heavyPort;
        this.myPort = myPort;
        this.myId = myId;

        this.start = new AtomicBoolean(false);
        this.numOkay = new AtomicInteger(0);
        this.queue = new LamportQueue();
        this.clock = new LamportClock();
    }

    private void notifyHeavyWeight() {
        Message.sendMsg(Message.MESSAGE_DONE, heavyPort);
    }

    private void waitHeavyWeight() {
        while(!start.get());
    }

    private void requestCS() {
        clock.tick();
        Message msg = new Message(myId, myPort, Message.MESSAGE_REQUEST, clock.getValue());
        queue.add(msg);
        broadcastMsg(msg);
        while(!this.okayCS());
    }

    private void broadcastMsg(Message msg) {
        for (Integer lightweightPort : lightweightPorts) {
            Message.sendMsg(msg, lightweightPort);
        }
    }

    private void releaseCS() {
        queue.removeSrcId(myId);
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_RELEASE, clock.getValue()));
        numOkay.set(0);
    }

    private boolean okayCS() {
        if (numOkay.get() < NUM_LIGHTWEIGHTS - 1) return false;
        return !queue.isEmpty()
                && queue.peek().getSrcId() == myId;
    }

    private void listenServerConnections() {
        while(true){
            String line = Message.getMsg(serverSocket);
            if (line.equals(Message.MESSAGE_PRINT)) {
                this.start.set(true);
            } else {
                Message msg = new Message(line);
                handleMsg(msg);
            }
        }
    }

    private void handleMsg(Message msg) {
        if(msg.getTag().equals(Message.MESSAGE_REQUEST)){
            queue.add(msg);
            Message.sendMsg(new Message(myId, myPort, Message.MESSAGE_ACK, clock.getValue()), msg.getSrcPort());
        }else if(msg.getTag().equals(Message.MESSAGE_ACK)){
            numOkay.getAndIncrement();
        } else if(msg.getTag().equals(Message.MESSAGE_RELEASE)){
            queue.removeSrcId(msg.getSrcId());
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
                System.out.println("I'm lightweight process " + myId + queue);
                Heavyweight.waitSecond();
            }
            this.start.set(false);
            releaseCS();
            notifyHeavyWeight();
        }
    }
}
