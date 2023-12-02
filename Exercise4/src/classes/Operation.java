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

    /**
     * Constructor used to create w/r operations from
     * transaction format (r(a) or w(a,b))
     */
    public Operation(String rawOperation) {
        String[] operationSplit = rawOperation.split("[()]");
        this.type = operationSplit[0];
        String[] operationParams = operationSplit[1].split(",");
        this.a = Integer.parseInt(operationParams[0]);

        if(operationParams.length > 1){
            this.b = Integer.parseInt(operationParams[1]);
        }
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
