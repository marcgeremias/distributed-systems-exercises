import java.util.PriorityQueue;
import java.util.Queue;

public class LamportMutex implements Runnable{
    LamportClock lamportClock;
    PriorityQueue<Integer> priorityQueue;

    @Override
    public void run() {
        priorityQueue = new PriorityQueue<>();
        lamportClock = new LamportClock();



    }
}
