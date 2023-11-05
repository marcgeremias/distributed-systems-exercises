import java.util.ArrayList;

public class Main {
    public static final int PROCESS_A_PORT = 1000;
    public static final String PROCESS_A_ID = "A";
    public static final int PROCESS_B_PORT = 2000;
    public static final String PROCESS_B_ID = "B";
    public static void main(String[] args) {
        Heavyweight ProcessA = new Heavyweight(true, PROCESS_A_ID, PROCESS_A_PORT, PROCESS_B_PORT);
        Heavyweight ProcessB = new Heavyweight(false, PROCESS_B_ID, PROCESS_B_PORT, PROCESS_A_PORT);
        ProcessA.start();
        ProcessB.start();
    }
}