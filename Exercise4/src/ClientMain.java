import business.ClientManager;

import java.net.ServerSocket;

public class ClientMain {

    public static void main(String[] args) {
        ClientManager clientManager = new ClientManager();
        clientManager.init();
    }
}