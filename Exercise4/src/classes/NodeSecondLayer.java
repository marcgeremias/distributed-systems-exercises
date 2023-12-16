package classes;

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
        /*    Message msg = Message.getMessage(nodeServerSocket);

            switch (msg.getMessageType()) {
                case Message.MESSAGE_TYPE_REPLICATED_HASHMAP:
                    System.out.println("Received transaction " + msg.getPayloadTransaction().toString() + " from node port" + msg.getSrcPort());
                    // 1. Update replicatedHashmap
                    replicatedHashmap = msg.getReplicatedHashmap();
                    // 2. Lazy -> Execute transaction operations
                    executeTransaction(msg.getPayloadTransaction());
                    break;
                default:
                    throw new RuntimeException("Unknown message type incoming to the second layer");
            }*/
        }
    }
}
