# Distributed Systems - Exercise 3: RPC-BASED CHAT

## Description
This project is a chat based on RPC (Remote Procedure Call) that allows to communicate 
between two or more users. The chat is based on a client-server architecture, where the 
server is the one that manages the connections between the clients and the clients are 
the ones that send the messages to the server and the server sends them to the rest of 
the clients.

All the messages are stored in a file called "chat.txt" that is created when the server and
that simulates the chat history. The server is the one that manages the file and the clients
only read it. That way, each client has an updated version of the chat history.

## How to compile the program - Requirements
In order to compile the program, you have to include the following dependencies:

- sudo apt-get install rpcsvc-proto
- sudo cp /usr/include/tirpc/rpc/* /usr/include/rpc/
- sudo apt-get install libncurses5-dev libncursesw5-dev
- sudo cp /usr/include/tirpc/netconfig.h /usr/include/
- sudo apt install rpcbind

/*
    Really usefull to find packages with the required libraries

    apt-file search rpcgen
    (To installt the package: sudo apt-get install apt-file)
*/

## How to run the program

TODO

## Authors
- [Guillem Godoy Hernández](https://github.com/guillemghdz) (guillem.godoy)
- [Marc Geremias Serra](https://github.com/marcgeremias) (marc.geremias)

## References
- https://www2.phys.canterbury.ac.nz/dept/docs/manuals/unix/DEC_4.0e_Docs/HTML/AQ0R5BTE/DOCU_003.HTM 
- https://stackoverflow.com/questions/75128485/undefined-reference-to-xdr-int 
- https://devicetests.com/fix-fatal-error-rpc-rpc-h-no-file-directory-installing-snort-ubuntu 
- https://youtu.be/HbBxO5RXNhU
