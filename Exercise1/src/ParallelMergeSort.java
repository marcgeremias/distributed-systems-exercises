import java.util.Arrays;

public class ParallelMergeSort implements Runnable {
    private int[] unsortedArray;
    private int[] sortedArray;

    public ParallelMergeSort(int[] unsortedArray, int[] sortedArray) {
        this.unsortedArray = unsortedArray;
        this.sortedArray = sortedArray;
    }

    @Override
    public void run() {
        mergeSort(unsortedArray, unsortedArray.length);
    }

    private void mergeSort(int[] array, int length) {
        if (length < 2) return;

        int mid = length / 2;
        int[] leftHalf = Arrays.copyOfRange(array, 0, mid);
        int[] rightHalf = Arrays.copyOfRange(array, mid, length);

        Thread leftThread = new Thread(new ParallelMergeSort(leftHalf, sortedArray));
        Thread rightThread = new Thread(new ParallelMergeSort(rightHalf, sortedArray));

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        merge(array, leftHalf, rightHalf);

        for (int i = 0; i < length; i++) {
            sortedArray[i] = array[i];
        }
    }

    private void merge(int[] a, int[] leftHalf, int[] rightHalf) {
        int i = 0;
        int j = 0;
        int k = 0;

        int leftHalfLength = leftHalf.length;
        int rightHalfLength = rightHalf.length;

        while (i < leftHalfLength && j < rightHalfLength) {
            if (leftHalf[i] <= rightHalf[j]) {
                a[k++] = leftHalf[i++];
            } else {
                a[k++] = rightHalf[j++];
            }
        }
        while (i < leftHalfLength) {
            a[k++] = leftHalf[i++];
        }
        while (j < rightHalfLength) {
            a[k++] = rightHalf[j++];
        }
    }
}
