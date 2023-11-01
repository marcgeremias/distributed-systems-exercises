public class LamportClock {
    private int ticks;

    public LamportClock() {
        this.ticks = 0;
    }
    public int getValue() {
        return ticks;
    }
    public void tick() {
        ticks++;
    }
    public void sendAction() {tick();}
    public void receiveAction(int receivedValue) {
        this.ticks = Math.max(this.ticks, receivedValue) + 1;
    }
}
