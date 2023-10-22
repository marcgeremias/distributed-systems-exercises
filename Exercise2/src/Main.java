public class Main {
    public static void main(String[] args) {
        int portA = 8080;
        int portB = 8081;

        Heavyweight ProcessA = new Heavyweight(true, 1, portA, portB);
        Heavyweight ProcessB = new Heavyweight(false, 2, portB, portA);

        /*
        Thread threadA = new Thread(ProcessA);
        Thread threadB = new Thread(ProcessB);

        threadA.start();
        threadB.start();

         */
    }
}
