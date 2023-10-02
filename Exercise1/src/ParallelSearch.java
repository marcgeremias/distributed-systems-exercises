import java.util.LinkedList;
import java.util.Scanner;

public class ParallelSearch {
    private static final int MAX_SIZE = 10;
    private final LinkedList<Integer> list;
    private final int target;
    private boolean foundByFirstThread = false;
    private boolean foundBySecondThread = false;

    public ParallelSearch(LinkedList<Integer> list, int target) {
        this.list = list;
        this.target = target;
    }
    public void searchFromBeginning() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == target) {
                foundByFirstThread = true;
                break;
            }
        }
    }
    public void searchFromEnd() {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) == target) {
                foundBySecondThread = true;
                break;
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        // List of numbers to search through
        LinkedList<Integer> myList = new LinkedList();
        // Hardcoded list of numbers (0 - 10)
        for (int i = 0; i < MAX_SIZE; i++) {
            myList.add(i);
        }
        // Target number to search for --> introduced by user
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number to search for: ");
        int numberToFind = scanner.nextInt();

        // Create a new instance of the ParallelSearch class
        ParallelSearch parallelSearch = new ParallelSearch(myList, numberToFind);

        Thread thread1 = new Thread(() -> parallelSearch.searchFromBeginning());
        Thread thread2 = new Thread(() -> parallelSearch.searchFromEnd());

        // Start the threads
        thread1.start();
        thread2.start();
        // Wait for the threads to finish
        thread1.join();
        thread2.join();

        if (parallelSearch.foundByFirstThread) {
            System.out.println("Number found by the 1st thread.");
        } else if (parallelSearch.foundBySecondThread) {
            System.out.println("Number found by the 2nd thread.");
        } else {
            System.out.println("Number not found in the list.");
        }

    }
}