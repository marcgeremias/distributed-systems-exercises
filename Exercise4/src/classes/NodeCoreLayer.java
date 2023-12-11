package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class NodeCoreLayer extends Node {
    private int nUpdates; /// write == update

    public NodeCoreLayer(HashMap<String, Integer> nodePorts, ArrayList<String> linkedNodes, String id, Integer port, Integer clientPort, ArrayList<String>[] nodesPerLayer) {
        super(nodePorts, linkedNodes, id, port, clientPort, nodesPerLayer);
        this.nUpdates = 0;
    }

    /*Core:
          - Update everywhere = Permet a l'usuari efectuar operacions de modificació a qualsevol dels nodes
          - Active = Es passa la recepta (en el nostre cas en concret les transaccions a fer a la resta de nodes)
          - Eager replication = No executa l'operació entrant fins que s'ha assegurat la consistència de les dades
    */

    @Override
    protected void processMessage(Message msg) {
        switch(msg.getMessageType()){
            case Message.MESSAGE_TYPE_TRANSACTION:
                System.out.println("Received transaction " + msg.getPayloadTransaction().toString() + " from client");
                // 1. Eager replication, send received "recipe" to all nodes before executing it
                sameLayerBroadcast(new Message(msg.getPayloadTransaction(), Message.MESSAGE_TYPE_TRANSACTION_RECIPE));
                // TODO 2. Wait for all nodes to send OK (we need to add a counter to know how many OKs we have received)
                // 2. Execute transaction operations
                executeTransaction(msg.getPayloadTransaction());
                // 3. Send OK to client
                Message.sendMessage(new Message(Message.MESSAGE_TYPE_OK),clientPort);
                // 4. If the transaction contains write operations increment nUpdates
                if(msg.getPayloadTransaction().containsNotReadOnlyOperation()) nUpdates++;
                break;
            case Message.MESSAGE_TYPE_TRANSACTION_RECIPE:
                System.out.println("Received transaction recipe " + msg.getPayloadTransaction().toString());
                // 1. Execute the transaction recipe
                executeTransaction(msg.getPayloadTransaction());
                /* TODO 2. Send OK to the node that sent the recipe
                    (TO BE ABLE TO DO THIS WE NEED TO ADD THE SENDER ID OR PORT TO THE MESSAGE) */
                // 3. If the transaction contains write operations increment nUpdates
                if(msg.getPayloadTransaction().containsNotReadOnlyOperation()) nUpdates++;
                break;
            default:
                throw new RuntimeException("Unknown message type");
        }
    }
}
