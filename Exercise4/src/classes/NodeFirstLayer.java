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
        //Transaction transaction = msg.getPayloadTransaction();
        //System.out.println("Node " + id + " from layer " + this.getClass().getSimpleName() + " received transaction " + transaction.toString());
    }
}
