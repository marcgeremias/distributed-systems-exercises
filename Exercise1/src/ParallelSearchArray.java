import java.util.LinkedList;
import java.util.Scanner;

public class ParallelSearchArray {
    private static final int MAX_SIZE = 10;
    public static int ParallelSearch (int toSearch, int[] array, int numThreads){
        int result = -1;

        // Calculate the size of each subarray
        int subArraySize = array.length / numThreads;


        return result;
    }
    public static void main(String[] args) throws InterruptedException {
        // Input from user
        Scanner scanner = new Scanner(System.in);
        // Array of numbers to search through
        int[] myArray = new int[10];
        // Hardcoded list of numbers (0 - 10)
        for (int i = 0; i < MAX_SIZE; i++) {
            myArray[i] = i;
        }
        // Target number to search for --> introduced by user
        System.out.println("Enter the number to search for: ");
        int numberToFind = scanner.nextInt();
        // Number of threads to open
        System.out.println("Enter the number of threads to open: ");
        int numThreads = scanner.nextInt();
        // Start the threads
        int result = ParallelSearch(numberToFind, myArray, numThreads);
        System.out.println("Result: " + result);
    }

}
