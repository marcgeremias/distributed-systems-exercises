import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Lightweight extends Thread{
    LamportClock clock;
    //int[] requestQ;
    //AtomicIntegerArray requestQ;
    private final PriorityQueue<Message> queue = new PriorityQueue<>((r1, r2) -> {
        int compare = Integer.compare(r1.getTimestamp(), r2.getTimestamp());
        if (compare == 0) {
            return Integer.compare(r1.getSrcId(), r2.getSrcId());
        }
        return compare;
    });
    AtomicInteger numOkay;
    //int numOkay;

    private static final int NUM_LIGHTWEIGHTS = 3;
    private static final int NUM_PRINTS = 10;
    private ArrayList<Integer> lightweightPorts;
    private ServerSocket serverSocket;
    private int heavyPort;
    private int myPort;
    private int myId;
    //private boolean start;
    private AtomicBoolean start;

    public Lightweight(int myId, int heavyPort, int myPort, ArrayList<Integer> portsWithoutMe) {
        this.lightweightPorts = portsWithoutMe;
        this.heavyPort = heavyPort;
        this.myPort = myPort;
        this.myId = myId;
        //this.start = false;
        this.start = new AtomicBoolean(false);

        //this.requestQ = new int[NUM_LIGHTWEIGHTS];
        //this.requestQ = new AtomicIntegerArray(NUM_LIGHTWEIGHTS);

        this.clock = new LamportClock();
        //this.numOkay = 0;
        this.numOkay = new AtomicInteger(0);
        //for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) requestQ[i] = Integer.MAX_VALUE;
        //for (int i = 0; i < NUM_LIGHTWEIGHTS; i++) requestQ.set(i, Integer.MAX_VALUE);

    }

    private void notifyHeavyWeight() {
        Message.sendMsg(Message.MESSAGE_DONE, heavyPort);
    }

    private void waitHeavyWeight() {
        //while(!getStart());
        while(!start.get());
    }

    /*private synchronized boolean getStart() {
        return this.start;
    }

    private synchronized void setStart(boolean start) {
        this.start = start;
    }*/


    private void requestCS() {
        clock.tick();
        //requestQ.set(myId, clock.getValue());
        Message msg = new Message(myId, myPort, Message.MESSAGE_REQUEST, clock.getValue());
        synchronized (queue) {
            queue.add(msg);
        }
        broadcastMsg(msg);
        while(!this.okayCS());
    }

    private void broadcastMsg(Message msg) {
        for (Integer lightweightPort : lightweightPorts) {
            Message.sendMsg(msg, lightweightPort);
        }
    }

    private void releaseCS() {
        //clock.tick();
        //requestQ.set(myId, Integer.MAX_VALUE);
        //queue.removeIf(msg -> msg.getSrcId() == myId);
        synchronized (queue) {
            queue.removeIf(msg -> msg.getSrcId() == myId);
        }
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_RELEASE, clock.getValue()));
        numOkay.set(0);
    }
    private boolean okayCS() {
        if (numOkay.get() < NUM_LIGHTWEIGHTS - 1) return false;
        //return !queue.isEmpty()
        //        && queue.peek().getSrcId() == myId;
        synchronized (queue) {
            return !queue.isEmpty()
                    && queue.peek().getSrcId() == myId;
        }
    }


    private void listenServerConnections() {
        while(true){
            String line = Message.getMsg(serverSocket);
            if (line.equals(Message.MESSAGE_PRINT)) {
                this.start.set(true);
                System.out.println("Lightweight " + myId + " ready to start");
            } else {
                Message msg = new Message(line);
                handleMsg(msg);
            }
        }
    }

    private void handleMsg(Message msg) {
        //clock.receiveAction(msg.getTimestamp());
        if(msg.getTag().equals(Message.MESSAGE_REQUEST)){
            //requestQ.set(msg.getSrcId(), msg.getTimestamp());
            //queue.add(msg);
            clock.receiveAction(msg.getTimestamp());
            synchronized (queue) {
                queue.add(msg);
            }
            Message.sendMsg(new Message(myId, myPort, Message.MESSAGE_ACK, clock.getValue()), msg.getSrcPort());
        }else if(msg.getTag().equals(Message.MESSAGE_ACK)){
            clock.receiveAction(msg.getTimestamp());
            numOkay.getAndIncrement();
        } else if(msg.getTag().equals(Message.MESSAGE_RELEASE)){
            //requestQ.set(msg.getSrcId(), Integer.MAX_VALUE);
            //queue.removeIf(m -> m.getSrcId() == msg.getSrcId());
            synchronized (queue) {
                queue.removeIf(m -> m.getSrcId() == msg.getSrcId());
            }
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
