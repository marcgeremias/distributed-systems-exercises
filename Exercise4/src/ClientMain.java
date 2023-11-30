import business.ClientManager;

import java.net.ServerSocket;

public class ClientMain {

    public static void main(String[] args) {
        /* TODO:
            - 1. Llegir fitxer transactions.txt
            - 2. Enviar cada transacció a cada capa corresponent (dins de la capa és el client qui decideix a quin dels nodes)
        */
        ClientManager clientManager = new ClientManager();
        clientManager.init();
    }
}