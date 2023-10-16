import java.util.Arrays;

public class MultithreadingSort {
    public static void main(String[] args) {
        System.out.println("Multithreading MergeSort");
        int[] array = {5, 6, 7, 3, 10, 2, 9, 1, 8, 4, 1, 4, 17, 58, 33, 50, 40};
        int[] sortedArray = new int[array.length];
        System.out.println("Unsorted array:" + Arrays.toString(array));

        long startTime = System.nanoTime();

        ParallelMergeSorter parallelMergeSort = new ParallelMergeSorter(array, sortedArray);
        Thread mainThread = new Thread(parallelMergeSort);

        mainThread.start();

        try {
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        double seconds = (double) elapsedTime / 1_000_000_000.0;
        System.out.println("Elapsed time: " + seconds + " s");


        System.out.println("Sorted array using threads:" + Arrays.toString(sortedArray));
    }

}

