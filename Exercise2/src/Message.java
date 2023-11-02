public class Message {
    private int srcId;
    private int srcPort;
    private String tag;
    private int timestamp;

    public Message(int srcId, int srcPort, String tag, int timestamp) {
        this.srcId = srcId;
        this.srcPort = srcPort;
        this.tag = tag;
        this.timestamp = timestamp;
    }

    public Message(String serializedMsg) {
        String[] strings = serializedMsg.split(" ");
        this.srcId = Integer.parseInt(strings[0]);
        this.srcPort = Integer.parseInt(strings[1]);
        this.tag = strings[2];
        this.timestamp = Integer.parseInt(strings[3]);
    }

    public int getSrcId() {
        return srcId;
    }

    @Override
    public String toString() {
        return srcId + " " + srcPort + " " + tag + " " + timestamp;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public String getTag() {
        return tag;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
