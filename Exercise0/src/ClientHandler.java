import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run() {
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = null;

            while(true) {
                // Wait for a new client request
                request = bufferedReader.readLine();
                if (request.equals("exit")) break;

                while(MasterServer.isBusy());
                MasterServer.setBusy();

                if (request.equals("read")) {
                    bufferedWriter.write(Integer.toString(MasterServer.readVariable()));
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else {
                    int newValue = Integer.parseInt(request);
                    MasterServer.updateVariable(newValue);
                }
                MasterServer.cleanBusy();
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
