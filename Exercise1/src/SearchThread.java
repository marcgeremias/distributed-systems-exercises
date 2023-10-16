public class SearchThread  extends Thread {
    private final int[] array;
    private final int toSearch;
    private final int threadId;
    private int result = -1;

    public SearchThread(int[] array, int toSearch, int threadId) {
        this.array = array;
        this.toSearch = toSearch;
        this.threadId = threadId;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run() {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == toSearch) {
                result = i;
                break;
            }
        }
    }
}