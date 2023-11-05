import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class LamportMutex extends Lightweight {
    private LamportQueue queue;

    public LamportMutex(
            int myId, int heavyPort, int myPort, ArrayList<Integer> portsWithoutMe) {

        super(myId, myPort, heavyPort, portsWithoutMe);
        this.queue = new LamportQueue();
    }

    private void requestCS() {
        clock.tick();
        Message msg = new Message(myId, myPort, Message.MESSAGE_REQUEST, clock.getValue());
        queue.add(msg);
        broadcastMsg(msg);
        while(!this.okayCS());
    }

    private void releaseCS() {
        queue.removeSrcId(myId);
        broadcastMsg(new Message(myId, myPort, Message.MESSAGE_RELEASE, clock.getValue()));
        numOkay.set(0);
    }

    private boolean okayCS() {
        if (numOkay.get() < NUM_LIGHTWEIGHTS - 1) return false;
        return !queue.isEmpty() && queue.peek().getSrcId() == myId;
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
            for (int i=0; i<NUM_PRINTS; i++){
                System.out.println("I'm lightweight process A" + (myId + 1));
                Heavyweight.waitSecond();
            }
            this.start.set(false);
            releaseCS();
            notifyHeavyWeight();
        }
    }
}
