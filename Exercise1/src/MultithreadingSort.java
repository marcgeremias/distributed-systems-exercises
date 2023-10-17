import java.util.Arrays;
import java.util.Random;

public class MultithreadingSort {
    public static void main(String[] args) {
        //******************************************************************************
        //********************* Multithreaded MergeSort Implementation *****************
        //******************************************************************************
        System.out.println("\nMultithreading MergeSort:");

        int[] unsortedArray = new int[4];
        int[] tmp = new int[unsortedArray.length];
        Random random = new Random();
        for (int i = 0; i < unsortedArray.length; i++) {
            unsortedArray[i] = random.nextInt(1000);
            tmp[i] = unsortedArray[i];
        }
        int[] sortedArray = new int[unsortedArray.length];

        System.out.println("Unsorted array:" + Arrays.toString(unsortedArray));

        long startTime = System.nanoTime();

        new ParallelMergeSort(unsortedArray, sortedArray).run();

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double seconds = (double) elapsedTime / 1_000_000_000.0;

        System.out.println("Sorted array using threads:" + Arrays.toString(sortedArray));
        System.out.println("Elapsed time Multithreaded MergeSort: " + seconds + " s");


        //******************************************************************************
        //************************ Normal MergeSort Implementation *********************
        //******************************************************************************
        System.out.println("\nSingle-threaded MergeSort:");

        for (int i = 0; i < unsortedArray.length; i++) {
            unsortedArray[i] = tmp[i];
            sortedArray[i] = 0;
        }

        System.out.println("Unsorted array:" + Arrays.toString(unsortedArray));

        long startTimeNormal = System.nanoTime();

        MergeSort mergeSort = new MergeSort(unsortedArray, sortedArray);
        mergeSort.mergeSort(unsortedArray, unsortedArray.length);

        long endTimeNormal = System.nanoTime();
        long elapsedTimeNormal = endTimeNormal - startTimeNormal;
        double secondsNormal = (double) elapsedTimeNormal / 1_000_000_000.0;

        System.out.println("Sorted array using threads:" + Arrays.toString(sortedArray));
        System.out.println("Elapsed time Normal MergeSort: " + secondsNormal + " s");

    }

}

