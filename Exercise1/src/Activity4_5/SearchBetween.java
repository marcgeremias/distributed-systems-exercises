package Activity4_5;

public class SearchBetween extends Thread{
    private final int[] array;
    private final int toSearch;
    private final int from;
    private final int to;
    private final int threadId;
    private int result = -1;

    public SearchBetween(int[] array, int toSearch, int from, int to, int threadId) {
        this.array = array;
        this.toSearch = toSearch;
        this.from = from;
        this.to = to;
        this.threadId = threadId;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run() {
        for (int i = from; i < to; i++) {
            if (array[i] == toSearch) {
                result = i;
                return;
            }
        }
    }

}
