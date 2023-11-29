package classes;

import java.util.ArrayList;

public class Transaction {
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
