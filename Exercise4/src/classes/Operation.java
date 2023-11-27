package classes;

public class Operation {
    private static final String OPERATION_READ = "r";
    private static final String OPERATION_WRITE = "w";
    private String type; // read-only or not-read-only
    private int a;
    private int b;

    public Operation(String type, int a, int b) {
        this.type = type;
        this.a = a;
        this.b = b;
    }

    public String getType() {
        return type;
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    @Override
    public String toString() {
        if(type.equals(OPERATION_READ)){
            return OPERATION_READ+"("+a+")";
        }else if (type.equals(OPERATION_WRITE)){
            return OPERATION_WRITE+"("+a+","+b+")";
        }else{
            throw new RuntimeException("Trying to cast an invalid operation type");
        }
    }
}
