package classes;

import business.FileManager;
import business.WebSocketServerEndpoint;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeSecondLayer extends Node{

    public NodeSecondLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        super(nodePorts, linkedNodes, id, port, clientPort, nodesPerLayer);
    }

    /*Second Layer:
      - CADA 10 SEGONS REP UNA COPIA DEL HASHMAP DE LA FirstLayer
      - Primary backup = Força a tots els clients a executar operacions en el mateix node
      - Passive = Es passa directament la imatge/resultat/elquesigui (en el nostre cas el hashmap resultant)
      - Lazy = S'executa la operació encara que encara no s'hagi assegurat la consistència de les dades
    */
    @Override
    protected void processMessage() {
        while (true) {
            Message msg = Message.getMessage(nodeServerSocket);

            switch (msg.getMessageType()) {
                case Message.MESSAGE_TYPE_REPLICATED_HASHMAP:
                    System.out.println("Node " + id + " received replicated hashmap " + msg.getReplicatedHashmap().toString() + " from first layer");
                    // 1. Update replicatedHashmap
                    replicatedHashmap = msg.getReplicatedHashmap();
                    // 2. Write new log
                    FileManager.writeNewLog(this.id, replicatedHashmap);
                    WebSocketServerEndpoint.sentToAllSessions(id + ":" + replicatedHashmap.toString());
                    break;
                case Message.MESSAGE_TYPE_TRANSACTION:
                    System.out.println("Node " + id + " received transaction " + msg.getPayloadTransaction().toString() + " from client");
                    // 1. Execute transaction operations
                    executeTransaction(msg.getPayloadTransaction());
                    // 2. Send OK to client
                    Message.sendMessage(new Message(replicatedHashmap, port, Message.MESSAGE_TYPE_OK), clientPort);
                    break;
                default:
                    throw new RuntimeException("Unknown message type incoming to the second layer " + msg.getMessageType());
            }
        }
    }
}
