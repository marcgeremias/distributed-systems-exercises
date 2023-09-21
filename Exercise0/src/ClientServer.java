import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ClientServer {
    private static final String LOCAL_IP = "localhost";
    private static final int LOCAL_PORT = 1234;

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    public ClientServer(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            //TODO closeEverything()
        }
    }

    public static void closeSocket(){
        try{
            if(bufferedReader != null) bufferedReader.close();
            if(bufferedWriter != null) bufferedWriter.close();
            if(socket != null) socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void openSocket(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private static void update() throws IOException, InterruptedException {
        int value=0;
        for (int i=0; i<10; i++){
            value = getCurrentValue();
            updateCurrentValue(value+1);
            sleep(1000);
        }
    }

    private static void updateCurrentValue(int newValue) throws IOException {
        bufferedWriter.write(String.valueOf(newValue));
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private static int read() throws InterruptedException, IOException {
        int value=0;
        for (int i=0; i<10; i++){
             value = getCurrentValue();
             sleep(1000);
        }
        return value;
    }

    private static int getCurrentValue() throws IOException {
        bufferedWriter.write("read");
        bufferedWriter.newLine();
        bufferedWriter.flush();

        while(!bufferedReader.ready());

        String updatedValue = bufferedReader.readLine();
        System.out.println("Value: " + updatedValue);

        return Integer.parseInt(updatedValue);
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 0) return;
        openSocket(LOCAL_IP, LOCAL_PORT);

        switch(args[0]) {
            case "readonly":
                System.out.println("readonly");
                read();
                break;
            case "updateonly":
                System.out.println("updateonly");
                update();
                break;
            default:
                System.out.println("Unknown operation.");
                break;
        }

        bufferedWriter.write("exit");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        closeSocket();
    }
}