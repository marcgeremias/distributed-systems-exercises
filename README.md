# Distributed Systems - Exercise 2: Distributed Mutual Exclusion

The objective of this exercise is to understand process synchronization mechanisms and implement two algorithms based on the application of a logical clock to achieve mutual exclusion in a distributed system.

You will need to design an application that contains two main processes: ProcessA and ProcessB. First, ProcessA should create 3 secondary processes that we'll call ProcessLWA1, ProcessLWA2, and ProcessLWA3. On the other hand, ProcessB will do the same, but in this case, we'll name them ProcessLWB1, ProcessLWB2, and ProcessLWB3. Each secondary process must run in an infinite loop that will display its identification on the screen 10 times while waiting for 1 second.

Both main processes must run on the same machine, so all secondary processes will compete for the same shared resource: the screen. What is requested in this case is to implement a mutual exclusion policy based on tokens between the two main processes. The secondary processes of A will have to coordinate with a mutual exclusion policy based on the Lamport algorithm, and those of B with a policy based on the Ricart and Agrawala algorithm.

## Requirements
Java JDK Version: OpenJDK 20.0.2

## How to run the program

The program is designed so that to execute it, simply press the run button and both main processes and their respective secondary processes will start automatically.

### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)