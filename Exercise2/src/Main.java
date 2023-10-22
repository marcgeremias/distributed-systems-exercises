public class Main {
    public static void main(String[] args) {
        int portA = 1000;
        int portB = 2000;

        Heavyweight ProcessA = new Heavyweight(true, 1, portA, portB);
        Heavyweight ProcessB = new Heavyweight(false, 2, portB, portA);
        ProcessA.start();
        ProcessB.start();
    }
}
