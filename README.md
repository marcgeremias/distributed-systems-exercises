# Distributed Systems - Exercise 1: Questionnaire About Threads And Concurrency

The main goal of this exercise is to solve the problems presented in the questionnaire about threads and concurrency.

## Requirements
Java JDK Version: OpenJDK 20.0.2

## Recommendations
IDE: IntelliJ IDEA 2023.2.2

## How to run the program
To separately test the proper functioning of the subactivities in Exercise 1, three different Package-type directories have been created. These directories contain the necessary Java classes to execute each section. Below is a brief explanation of how to execute each section.

### Activity3
In this first directory, you'll find the classes AscendantSearch, DescendantSearch, and ParallelSearch. To run this first activity, execute the main() method of the ParallelSearch class. This class is responsible for creating and starting the two threads to traverse the ArrayList in ascending and descending order simultaneously. It displays on the screen which thread found the prime value.

By default, the ArrayList consists of 1000 cells, where each cell contains the value of the index. Additionally, when executed, it will ask in the terminal for the value to search for.

### Activity4_5
In this second directory, you'll find the classes ParallelSearchArray, SearchBetween, and SearchThread. To execute the search within the array using different threads, simply execute the main() method of the main class ParallelSearchArray and write in the terminal the value to search for along with the number of threads to use.

By default, the array contains 100,000,000 cells and does not use shared memory, meaning a copy of the array is made for each new thread created. If you want to modify the algorithm to be used, comment the current algorithm and uncomment the other one to use.

### Activity7_8
In this third directory, you'll find the class MultithreadingSort, which implements the other two classes, MergeSort and ParallelMergeSort. These three classes together are responsible for performing MergeSort in parallel and sequentially, comparing the execution time of each (as requested in sections 7 and 8 of the statement).

To execute this activity, run the main() method of the MultithreadingSort class. The program will generate a random array of X cells (determined by the user) and perform MergeSort on this array in parallel and sequentially. It will display on the screen the execution time of each and the result of the sorted array.

### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)