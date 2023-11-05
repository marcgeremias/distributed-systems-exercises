import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AgrawalaMutex extends Lightweight{
    private final LinkedList pendingQ;
    private AtomicInteger myTS = new AtomicInteger(0);
    private AtomicBoolean wait;

    public AgrawalaMutex(
            int myId, int heavyPort, int myPort, ArrayList<Integer> portsWithoutMe) {

        super(myId, myPort, heavyPort, portsWithoutMe);

        this.myTS.set(Integer.MAX_VALUE);
        this.pendingQ = new LinkedList<Message>();
        this.wait = new AtomicBoolean(false);
    }

    private void requestCS() {
        clock.tick();
        myTS.set(clock.getValue());
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_REQUEST, clock.getValue()));
        while(numOkay.get() < NUM_LIGHTWEIGHTS - 1);
    }

    private void releaseCS() {
        myTS.set(Integer.MAX_VALUE);
        clock.tick();
        while(!pendingQ.isEmpty()){
            Message msg;
            synchronized (pendingQ){
                msg = (Message) pendingQ.removeFirst();
            }
            //Message msg = (Message) pendingQ.removeFirst();
            Message.sendMsg(new Message(myId, myPort,
                    Message.MESSAGE_OKAY, clock.getValue()), msg.getSrcPort());
        }
    }

    private void handleMsg(Message msg) {
        int timestamp = msg.getTimestamp();
        clock.receiveAction(timestamp);
        if(msg.getTag().equals(Message.MESSAGE_REQUEST)){
            if((myTS.get() == Integer.MAX_VALUE)
                || (timestamp < myTS.get())
                || ((timestamp == myTS.get()) && (msg.getSrcId() < myId))){
                Message.sendMsg(new Message(myId, myPort,
                        Message.MESSAGE_OKAY, clock.getValue()), msg.getSrcPort());
            }else{
                synchronized (pendingQ){
                    pendingQ.add(msg);
                }
            }
        } else if(msg.getTag().equals(Message.MESSAGE_OKAY)){
            numOkay.getAndIncrement();
            if(numOkay.get() == NUM_LIGHTWEIGHTS - 1){
                wait.set(true);
            }
        }
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
            while(!wait.get());
            for (int i=0; i<NUM_PRINTS; i++){
                System.out.println("I'm lightweight process B" + (myId + 1));
                Heavyweight.waitSecond();
            }
            this.start.set(false);
            this.wait.set(false);
            numOkay.set(0);
            releaseCS();
            notifyHeavyWeight();
        }
    }


}
