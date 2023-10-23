public class Lightweight extends Thread{
    private LamportClock clock;
    private String heavyId;
    private int id;

    public Lightweight(LamportClock clock, String heavyId, int id) {
        this.heavyId = heavyId;
        this.clock = clock;
        this.id = id;
    }

    public String getFullId() {
        return heavyId + id;
    }

    @Override
    public void run() {
        // TODO Lightweight process
        /*
        while(1){
            waitHeavyWeight();
            requestCS();
        for (int i=0; i<10; i++){
            printf("I'm lightweight process %s\n", myID);
            waitSecond();
        }
            releaseCS();
            notifyHeavyWeight();
        }
*/
    }
}
