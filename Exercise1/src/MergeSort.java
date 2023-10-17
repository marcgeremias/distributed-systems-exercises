public class MergeSort {
    private int[] unsortedArray;
    private int[] sortedArray;

    public MergeSort(int[] unsortedArray, int[] sortedArray) {
        this.unsortedArray = unsortedArray;
        this.sortedArray = sortedArray;
    }

    public void mergeSort(int[] array, int length) {
        if (length < 2) return;

        int mid = length / 2;
        int[] leftHalf = new int[mid];
        int[] rightHalf = new int[length - mid];

        for (int i = 0; i < mid; i++) {
            leftHalf[i] = array[i];
        }
        for (int i = mid; i < length; i++) {
            rightHalf[i - mid] = array[i];
        }

        mergeSort(leftHalf, mid);
        mergeSort(rightHalf, length - mid);

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
