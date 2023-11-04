import java.util.PriorityQueue;

public class LamportQueue extends PriorityQueue<Message> {
    public LamportQueue() {
        super((msg1, msg2) -> {
            int compare = Integer.compare(msg1.getTimestamp(), msg2.getTimestamp());
            if (compare == 0) {
                return Integer.compare(msg1.getSrcId(), msg2.getSrcId());
            }
            return compare;
        });
    }

    @Override
    public synchronized boolean add(Message msg) {
        return super.add(msg);
    }

    @Override
    public synchronized boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public synchronized Message peek() {
        return super.peek();
    }
    public synchronized void removeSrcId(int srcId) {
        this.removeIf(msg -> msg.getSrcId() == srcId);
    }
}