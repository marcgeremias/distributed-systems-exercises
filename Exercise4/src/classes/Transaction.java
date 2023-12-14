package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction implements Serializable {
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

    public boolean containsNotReadOnlyOperation(){
        for (Operation operation : operations) {
            if(operation.getType().equals(Operation.OPERATION_WRITE)){
                return true;
            }
        }
        return false;
    }


}
