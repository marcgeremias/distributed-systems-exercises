package Activity3;

import java.util.LinkedList;

public class AscendantSearch extends Thread{
    private final LinkedList<Integer> list;
    private final int toSearch;
    public AscendantSearch(LinkedList<Integer> list, int toSearch) {
        this.list = list;
        this.toSearch = toSearch;
    }

    @Override
    public void run() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == toSearch) {
                System.out.println("Found by the ascending thread");
                break;
            }
        }
    }
}
