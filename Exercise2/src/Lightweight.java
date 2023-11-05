import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Lightweight extends Thread {
    protected static final int NUM_LIGHTWEIGHTS = 3;
    protected static final int NUM_PRINTS = 10;
    protected ArrayList<Integer> lightweightPorts;
    protected AtomicBoolean start;
    protected int heavyPort;
    protected AtomicInteger numOkay;
    protected LamportClock clock;
    protected int myPort;
    protected int myId;
    protected ServerSocket serverSocket;

    public Lightweight(int myId, int myPort, int heavyPort ,ArrayList<Integer> portsWithoutMe) {
        this.myId = myId;
        this.myPort = myPort;
        this.heavyPort = heavyPort;
        this.lightweightPorts = portsWithoutMe;

        this.start = new AtomicBoolean(false);
        this.numOkay = new AtomicInteger(0);
        this.clock = new LamportClock();
    }


    protected void broadcastMsg(Message msg) {
        for (Integer lightweightPort : lightweightPorts) {
            Message.sendMsg(msg, lightweightPort);
        }
    }
    protected void notifyHeavyWeight() {
        Message.sendMsg(Message.MESSAGE_DONE, heavyPort);
    }
    protected void waitHeavyWeight() {
        while(!start.get());
    }
}
