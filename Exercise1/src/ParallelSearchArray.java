import java.util.LinkedList;
import java.util.Scanner;

public class ParallelSearchArray {
    private static final int MAX_SIZE = 10;
    public static int ParallelSearch (int toSearch, int[] array, int numThreads){
        int result = -1;

        int subArraySize = array.length / numThreads;


        return result;
    }
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int[] myArray = new int[10];
        for (int i = 0; i < MAX_SIZE; i++) {
            myArray[i] = i;
        }

        System.out.println("Enter the number to search for: ");
        int numberToFind = scanner.nextInt();

        System.out.println("Enter the number of threads to open: ");
        int numThreads = scanner.nextInt();

        int result = ParallelSearch(numberToFind, myArray, numThreads);
        System.out.println("Result: " + result);
    }

}
