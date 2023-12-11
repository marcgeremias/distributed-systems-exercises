package business;

import classes.*;

import java.io.*;
import java.util.*;

public class FileManager {
    private static final String PATH_NODES_PORTS = "Exercise4/res/config_files/nodes_ports.txt";
    private static final String PATH_NODES_LINKS = "Exercise4/res/config_files/nodes_links.txt";
    private static final String PATH_CLIENT_PORT = "Exercise4/res/config_files/client_port.txt";
    private static final String PATH_TRANSACTIONS = "Exercise4/res/transactions.txt";
    private static final String PATH_BASE_NODES_LOGS = "Exercise4/res/nodes_logs/";
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

    public static ArrayList<Node> readNodes() {
        HashMap<String, Integer> nodePorts = readNodePorts();
        ArrayList<String>[] nodesPerLayer = FileManager.readLayerNodes();

        ArrayList<Node> nodes = new ArrayList<>();
        int clientPort = readClientPort();

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
                        nodes.add(new NodeCoreLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0]), clientPort, nodesPerLayer));
                        break;
                    case 1:
                        nodes.add(new NodeFirstLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0]), clientPort, nodesPerLayer));
                        break;
                    case 2:
                        nodes.add(new NodeSecondLayer(nodePorts, linkedNodes, lineSplit[0], nodePorts.get(lineSplit[0]), clientPort, nodesPerLayer));
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

    public static ArrayList<Transaction> readTransactions() {
        // TODO: CLEAN THIS SPAGHETTI CODE
        ArrayList<Transaction> transactionsBatch = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_TRANSACTIONS));
            while (bufferedReader.ready()) {
                Transaction currentTransaction;
                String line = bufferedReader.readLine();
                String[] lineSplit = line.split("\\s*,\\s*"); //         String[] operationSplit = rawOperation.split("[()]");
                if(lineSplit[0].equals("b")){ // Not read only (Must be executed in the core layer)
                    currentTransaction = new Transaction(false, Transaction.CORE_LAYER);
                }else{ // Read only (Can be executed in any layer)
                    currentTransaction = new Transaction(true, Integer.parseInt(lineSplit[0].substring(2,3)));
                }
                // Remove the first and last element of the array (b<f>, and c)
                lineSplit = Arrays.copyOfRange(lineSplit, 1, lineSplit.length-1);
                // For each lineSplit
                String[] newLineSplit;
                // IF the first element of an array cell contains is "w" concatenate it with the next cell and remove the next cell keeping the ","
                for (int i = 0; i < lineSplit.length; i++) {
                    if(lineSplit[i].contains("w")){
                        newLineSplit = new String[lineSplit.length-1];
                        for (int j = 0; j < lineSplit.length; j++) {
                            if(j == i){
                                newLineSplit[j] = lineSplit[j] + "," + lineSplit[j+1];
                                j++;
                            }else if(j > i){
                                newLineSplit[j-1] = lineSplit[j];
                            }else{
                                newLineSplit[j] = lineSplit[j];
                            }
                        }
                        lineSplit = newLineSplit;
                    }
                }

                for (String operationString : lineSplit) {
                    if(operationString.contains("w")){
                        String[] operationSplit = operationString.split("[()]");
                        String[] operationParams = operationSplit[1].split(",");
                        currentTransaction.addOperation(new Operation(Integer.parseInt(operationParams[0]), Integer.parseInt(operationParams[1])));
                    }else{
                        currentTransaction.addOperation(new Operation(Integer.parseInt(operationString.substring(2,3))));
                    }

                }
                transactionsBatch.add(currentTransaction);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactionsBatch;
    }

    public static ArrayList<String>[] readLayerNodes() {
        ArrayList<String>[] arrayLayerNodes = new ArrayList[NodesManager.MAX_NUMBER_OF_LAYERS];
        for (int i = 0; i < NodesManager.MAX_NUMBER_OF_LAYERS; i++) arrayLayerNodes[i] = new ArrayList<>();

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

    public static int readClientPort() {
        try {
            bufferedReader = new BufferedReader(new FileReader(PATH_CLIENT_PORT));
            if(bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String[] lineSplit = line.split("\\s*=\\s*");
                return Integer.parseInt(lineSplit[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static void writeNewLog (String nodeId, HashMap<Integer, Integer> replicatedHashmap) {
        String path = PATH_BASE_NODES_LOGS + nodeId + ".txt";
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, true));
            // TODO: Make the hashmap look better
            bufferedWriter.write(String.valueOf(replicatedHashmap));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readAllLogs () {
        ArrayList<String>[] nodesPerLayer = FileManager.readLayerNodes();
        // TODO: It can be improved to read each log and append it an array of strings
        try {
            // For each node, read its log file and append it to the logs string
            StringBuilder logs = new StringBuilder();
            for (int i = 0; i < nodesPerLayer.length; i++) {
                if(i == 0) logs.append("------- CORE LAYER -------\n");
                else if(i == 1) logs.append("------- FIRST LAYER -------\n");
                else if(i == 2) logs.append("------- SECOND LAYER -------\n");
                for (String node : nodesPerLayer[i]) {
                    String path = PATH_BASE_NODES_LOGS + node + ".txt";
                    if(new File(path).exists()){
                        logs.append("Node ").append(node).append(":\n");
                        bufferedReader = new BufferedReader(new FileReader(path));
                        while (bufferedReader.ready()) {
                            logs.append(bufferedReader.readLine()).append("\n");
                        }
                    }
                }
            }
            return logs.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}