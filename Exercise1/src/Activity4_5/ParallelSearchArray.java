package Activity4_5;

import java.io.IOException;
import java.util.Scanner;

public class ParallelSearchArray {
    private static final int MAX_SIZE = 100000000;
    private static final int NO_RESULT_FOUND = -1;
    private static int numThreads;
    private static int toSearch;
    private static int[] array;

    public static int parallelSearch () throws InterruptedException {
        SearchBetween[] threads = new SearchBetween[numThreads];
        //Activity4_5.SearchThread[] threads = new Activity4_5.SearchThread[numThreads];
        int subArraySize = array.length / numThreads;
        int to = subArraySize;
        int from = 0;

        // Start all threads
        for(int i = 0; i < numThreads; i++){
            threads[i] = new SearchBetween(array, toSearch, from, to, i);
            //threads[i] = new Activity4_5.SearchThread(Arrays.copyOfRange(array, from, to), toSearch, i);
            threads[i].start();
            from = to;
            to += subArraySize;
        }

        // Wait for all threads to finish to get the return value
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
            // Check if any thread found the number
            if (threads[i].getResult() != -1) {
                System.out.println("[" + i + "] Found at index " + threads[i].getResult());
                return threads[i].getResult();
            }
        }
        return NO_RESULT_FOUND;
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        array = new int[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) array[i] = i;


        System.out.print("Enter the number to search for: ");
        toSearch = new Scanner(System.in).nextInt();
        System.out.print("Enter the number of threads to open: ");
        numThreads = new Scanner(System.in).nextInt();
        parallelSearch();
    }


}
