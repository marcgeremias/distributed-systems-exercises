import java.util.LinkedList;
import java.util.Scanner;

public class ParallelSearch {
    private static final int MAX_SIZE = 1000;
    public static void main(String[] args) throws InterruptedException {
        LinkedList<Integer> myList = new LinkedList<>();

        for (int i = 0; i < MAX_SIZE; i++) myList.add(i);

        System.out.print("Enter the number to search for: ");
        int toSearch = new Scanner(System.in).nextInt();

        AscendantSearch ascendantSearch = new AscendantSearch(myList, toSearch);
        DescendantSearch descendantSearch = new DescendantSearch(myList, toSearch);
        descendantSearch.start();
        ascendantSearch.start();
    }
}