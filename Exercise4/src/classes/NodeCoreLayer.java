package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {
    private int nWrite; /// write == update

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort) {
        super(nodePorts, linkedNodes, id, port, clientPort);
        this.nWrite = 0;
    }

    /*Core:
          - Update everywhere = Permet a l'usuari efectuar operacions de modificació a qualsevol dels nodes
          - Active = Es passa la recepta (en el nostre cas en concret les transaccions a fer a la resta de nodes)
          - Eager replication = No executa l'operació entrant fins que s'ha assegurat la consistència de les dades
    */

    @Override
    protected void processMessage(Message msg) {
        System.out.println("Node " + id + " from layer " + this.getClass().getSimpleName() + " received transaction " + msg.getPayloadTransaction());

        // 1. Eager replication, send received "recipe" to all nodes before executing it
        Message message = new Message(msg.getPayloadTransaction());
        layerBroadcast(msg);
        //TODO: executeOperations();





    }
}
