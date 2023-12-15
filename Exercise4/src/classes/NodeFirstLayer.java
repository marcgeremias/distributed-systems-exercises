package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeFirstLayer extends Node {

    public NodeFirstLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        super(nodePorts, linkedNodes, id, port, clientPort, nodesPerLayer);
    }

    /*First Layer:
      - CADA 10 UPDATES DEL CORE ES REP UNA COPIA DEL HASHMAP de la CoreLayer
      - Primary backup = Força a tots els clients a executar operacions en el mateix node
      - Passive = Es passa directament la imatge/resultat/elquesigui (en el nostre cas el hashmap resultant)
      - Lazy = S'executa l'operació encara que encara no s'hagi assegurat la consistència de les dades
     */

    @Override
    protected void processMessage() {
        while(true){
            Message msg = Message.getMessage(nodeServerSocket);

            switch(msg.getMessageType()){
                case Message.MESSAGE_TYPE_REPLICATED_HASHMAP:
                    System.out.println("Received transaction " + msg.getPayloadTransaction().toString() + " from node port" + msg.getSrcPort());
                    // 1. Update replicatedHashmap
                    replicatedHashmap = msg.getReplicatedHashmap();
                    break;
                default:
                    throw new RuntimeException("Unknown message type");
            }



        }
    }
}
