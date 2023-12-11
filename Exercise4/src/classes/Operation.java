package classes;

import java.io.Serializable;

public class Operation implements Serializable {
    public static final String OPERATION_READ = "r";
    public static final String OPERATION_WRITE = "w";
    private String type; // read-only or not-read-only
    private int a;
    private int b; // If the operation is read

    public Operation(String type, int a, int b) {
        this.type = type;
        this.a = a;
        this.b = b;
    }

    public Operation(int a) {
        this.type = OPERATION_READ;
        this.a = a;
    }

    public Operation(int a, int b) {
        this.type = OPERATION_WRITE;
        this.a = a;
        this.b = b;
    }

    public String getType() {
        return type;
    }

    public int getKey() {
        return a;
    }

    public int getValue() {
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
