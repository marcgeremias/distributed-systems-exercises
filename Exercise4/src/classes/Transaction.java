package classes;

import java.util.ArrayList;

public class Transaction {
    private ArrayList<Operation> operations;
    private int layer;
    /* If the layer is -1 the transaction is Not read-only and will
    be executed inside the core layer, otherwise it will be executed
    inside the specified layer */

    public Transaction(int layer) {
        this.operations = new ArrayList<>();
        this.layer = layer;
    }

    public void addOperation(Operation operation){
        operations.add(operation);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        // Use ternary operator to check if the transaction is read-only
        stringBuilder.append(layer == -1 ? "b<f>" : "b<" + layer + ">, ");


        for (Operation operation : operations) {
            // TODO
        }
        return null;
    }
}
