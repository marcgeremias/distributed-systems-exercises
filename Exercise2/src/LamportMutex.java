import java.util.PriorityQueue;
import java.util.Queue;

public class LamportMutex implements Runnable{
    LamportClock lamportClock;
    PriorityQueue<Integer> requestList;
    private boolean requestMade;


    public synchronized void queueRequest(int request) {
        requestList.add(request);
    }
    public synchronized void releaseRequest() {
        requestMade = false;
        requestList.remove(0);
    }


    /*
    public void accesCS() {
        lamportClock.sendAction();
        requestList.add(lamportClock.getValue());
    }
     */

    @Override
    public void run() {
        requestList = new PriorityQueue<>();
        lamportClock = new LamportClock();

    }
}
