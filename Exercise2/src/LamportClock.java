public class LamportClock {
    private int tics;

    public LamportClock() {
        this.tics = 0;
    }
    public int getValue() {
        return tics;
    }
    // To be used in internal events
    public void tick() {
        tics++;
    }
    // Increment tics before sending an action
    public void sendAction() {
        tick();
        // TODO: send message to other process
    }
    public void receiveAction(int receivedValue) {
        this.tics = Math.max(this.tics, receivedValue) + 1;
    }


}
