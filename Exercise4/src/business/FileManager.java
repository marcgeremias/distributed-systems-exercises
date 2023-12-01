package business;

import classes.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class FileManager {
    private static final String PATH_NODES_PORTS = "Exercise4/res/nodes_ports.txt";
    private static final String PATH_NODES_LINKS = "Exercise4/res/nodes_links.txt";
    private static final String PATH_TRANSACTONS = "Exercise4/res/transactions.txt";
    private static final Integer MAX_NUMBER_OF_LAYERS = 3;
    private static BufferedReader bufferedReader;

    public static HashMap<String, Integer> readNodePorts() {
        HashMap<String, Integer> nodePorts = new HashMap<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_NODES_PORTS));
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] lineSplit = line.split("\\s*=\\s*");
                nodePorts.put(lineSplit[0], Integer.parseInt(lineSplit[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return nodePorts;
    }

    public static ArrayList<Node> getNodes() {
        HashMap<String, Integer> nodePorts = readNodePorts();
        ArrayList<Node> nodes = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_NODES_LINKS));
            int currentLayer = 0;
            while (bufferedReader.ready()) {
                ArrayList<String> linkedNodes = new ArrayList<>();
                String line = bufferedReader.readLine();
                if (!line.contains("=")) {
                    currentLayer++;
                    continue;
                }

                String[] lineSplit = line.split("\\s*=\\s*");
                String[] linkedNodesSplit = lineSplit[1].split("\\s*,\\s*");
                Collections.addAll(linkedNodes, linkedNodesSplit);

                switch (currentLayer) {
                    case 0:
                        nodes.add(new NodeCoreLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0])));
                        break;
                    case 1:
                        nodes.add(new NodeFirstLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0])));
                        break;
                    case 2:
                        nodes.add(new NodeSecondLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0])));
                        break;
                    default:
                        throw new RuntimeException("Found invalid layer while reading the " + PATH_NODES_LINKS + " file");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nodes;
    }

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactionsBatch = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_TRANSACTONS));
            while (bufferedReader.ready()) {
                Transaction currentTransaction;
                String line = bufferedReader.readLine();
                String[] lineSplit = line.split("\\s*,\\s*");
                if(lineSplit[0].equals("b")){ // Not read only (Must be executed in the core layer)
                    currentTransaction = new Transaction(false, Transaction.CORE_LAYER);
                }else{ // Read only (Can be executed in any layer)
                    currentTransaction = new Transaction(true, Integer.parseInt(lineSplit[0].substring(2,3)));
                }
                // Remove the first and last element of the array (b<f>, and c)
                lineSplit = Arrays.copyOfRange(lineSplit, 1, lineSplit.length-1);
                for (String operationString : lineSplit) {
                    currentTransaction.addOperation(new Operation(operationString));
                }
                transactionsBatch.add(currentTransaction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactionsBatch;
    }

    public static ArrayList<String>[] getLayerNodes() {
        ArrayList<String>[] arrayLayerNodes = new ArrayList[MAX_NUMBER_OF_LAYERS];

        for (int i = 0; i < MAX_NUMBER_OF_LAYERS; i++) {
            arrayLayerNodes[i] = new ArrayList<>();
        }

        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_NODES_LINKS));
            int currentLayer = 0;
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.contains("=")) {
                    currentLayer++;
                    continue;
                }
                String[] lineSplit = line.split("\\s*=\\s*");
                arrayLayerNodes[currentLayer].add(lineSplit[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return arrayLayerNodes;
    }


}