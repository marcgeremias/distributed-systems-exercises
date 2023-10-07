import java.util.LinkedList;

public class DescendantSearch extends Thread{
    private final LinkedList<Integer> list;
    private final int toSearch;
    public DescendantSearch(LinkedList<Integer> list, int toSearch) {
        this.list = list;
        this.toSearch = toSearch;
    }

    @Override
    public void run() {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) == toSearch) {
                System.out.println("Found by the descending thread");
                break;
            }
        }
    }
}
