package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction implements Serializable {
    public static final int CORE_LAYER = 0;
    public static final int FIRST_LAYER = 1;
    public static final int SECOND_LAYER = 2;
    private ArrayList<Operation> operations;
    private boolean readOnly = false;
    private int layer;

    public Transaction(boolean readOnly,int layer) {
        this.operations = new ArrayList<>();
        this.readOnly = readOnly;
        this.layer = layer;
    }

    public void addOperation(Operation operation){
        operations.add(operation);
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(readOnly ? "b<" + layer + ">, " :  "b, ");
        for (Operation operation : operations) {
            // If is the last operation, remove the comma
            if(operations.indexOf(operation) == operations.size()-1){
                stringBuilder.append(operation.toString());
                break;
            }
            stringBuilder.append(operation.toString()).append(", ");
        }
        stringBuilder.append(", c");

        return stringBuilder.toString();
    }
}
