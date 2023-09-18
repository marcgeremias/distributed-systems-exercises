import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ClientServer {
    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static final String LOCAL_IP = "localhost";
    private static final int LOCAL_PORT = 1234;

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

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 0) return;

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
        openSocket(LOCAL_IP, LOCAL_PORT);

        bufferedWriter.write(String.valueOf(newValue));
        bufferedWriter.newLine();
        bufferedWriter.flush();

        closeSocket();
    }

    private static int read() throws InterruptedException, IOException {
        int value=0;
        for (int i=0; i<10; i++){
             value = getCurrentValue();
             sleep(1000);
        }
        return value;
    }

    private static int getCurrentValue() throws IOException, InterruptedException {
        openSocket(LOCAL_IP, LOCAL_PORT);

        bufferedWriter.write("read");
        bufferedWriter.newLine();
        bufferedWriter.flush();

        while(!bufferedReader.ready()){
            sleep(100);
        }
        String request = bufferedReader.readLine();
        System.out.println("Value: " + request);

        closeSocket();

        return Integer.parseInt(request);
    }
}