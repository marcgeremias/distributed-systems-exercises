# Distributed Systems - Exercise 0: A naïve view
The main objective of this exercise is to design a distributed architecture for a system capable of sharing a variable among N processes (called servers) using network connectors (sockets).

## Requirements
Java JDK Version: OpenJDK 20.0.2

## How to run the program
1. To run the program, you first need to compile the source code using the Java JDK.
2. Then, the program can be executed. There are two options:

### a) Terminal
The simplest way is by using the Java command-line interpreter directly.
(Tip: When using IntelliJ IDE, the intermediate code is automatically generated in the out/production/* folder)
To do this, first, run the central server with the following command:
```bash
java MasterServer
```
Next, you can run the rest of the servers with the following command if you want them to be of type "readonly":
```bash
java ClientServer readonly
```
Alternatively, if you want them to be of type "updateonly," run the following command:
```bash
java ClientServer updateonly
```

### b) Script
Another way to run the program is by first executing the Master as in option (a) but when simulating the clients, you can use a script that handles running multiple instances of the program in various terminals. To do this, you can modify one of the files in the "scripts" folder and make the script point to where the intermediate code of the program has been created.

### Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)