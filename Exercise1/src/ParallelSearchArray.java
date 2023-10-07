import java.util.Scanner;

public class ParallelSearchArray {
    private static final int MAX_SIZE = 1000;
    private static final int NO_RESULT_FOUND = -1;
    private static int numThreads;
    private static int toSearch;
    private static int[] array;

    public static int parallelSearch () throws InterruptedException {
        SearchBetween[] threads = new SearchBetween[numThreads];
        int subArraySize = array.length / numThreads;
        int to = subArraySize;
        int from = 0;

        // Start all threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new SearchBetween(array, toSearch, from, to, i);
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
    public static void main(String[] args) throws InterruptedException {
        array = new int[MAX_SIZE];
        for (int i = 0; i < MAX_SIZE; i++) array[i] = i;

        System.out.print("Enter the number to search for: ");
        toSearch = new Scanner(System.in).nextInt();
        System.out.print("Enter the number of threads to open: ");
        numThreads = new Scanner(System.in).nextInt();

        parallelSearch();

        /*
            1000 (5 Threads)
            0-200	= 0
            200-400	= 1
            400-600	= 2
            600-800 = 3
            800-1000 = 4

            1000 (20 Threads)
            0-50	= 0
            50-100	= 1
            100-150	= 2
            150-200	= 3
            200-250	= 4
            250-300	= 5
            300-350	= 6
            350-400	= 7
            400-450	= 8
            450-500	= 9
            ...
         */
    }

}
