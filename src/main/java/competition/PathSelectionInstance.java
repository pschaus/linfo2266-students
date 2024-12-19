package competition;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Set;

import static competition.Utils.hashPair;

public class PathSelectionInstance {

    protected int n; // Number of nodes;
    protected int m; // Number of available paths

    // Paths available for the network
    // !!! Do not modify or sort this array directly (use a copy instead)
    private Path[] paths;


    /**
     * Initialize the problem
     * @param routeFilename: path to the routes definition
     */
    public PathSelectionInstance(String routeFilename) {
        parseInstance(routeFilename);
    }

    /**
     * Load the instance
     * @param filename: path to the routes definition
     */
    public void parseInstance(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String currentLine = reader.readLine();
            this.n = Integer.parseInt(currentLine.split(" ")[0]);
            this.m = Integer.parseInt(currentLine.split(" ")[1]);
            this.paths = new Path[this.m];
            int routeIndex = 0;
            currentLine = reader.readLine();

            while (currentLine != null) {
                if (!currentLine.trim().isEmpty()) {
                    // parts[0] contains the endpoints of the route
                    // parts[1] contains the nodes crossed by the route
                    String[] parts = currentLine.split("\\|");
                    int src = Integer.parseInt(parts[0].split(" ")[0]);
                    int dest = Integer.parseInt(parts[0].split(" ")[1]);

                    BitSet nodes = new BitSet(n);
                    for (String nodeStr: parts[1].trim().split(" ")) {
                        nodes.set(Integer.parseInt(nodeStr));
                    }

                    this.paths[routeIndex++] = new Path(src, dest, nodes);
                }
                currentLine = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Verify that the solution makes the network entirely covered
     * @param solution an array containing the indices of the selected routes
     * @return a bitset describing which nodes are uncovered by the given solution
     */
    public BitSet uncoveredNodes(Set<Integer> solution) {
        BitSet coveredNodes = new BitSet(this.n);
        for (Integer i : solution) {
            coveredNodes.or(paths[i].nodes);
        }
        // reverse each value in the bitset
        coveredNodes.flip(0, this.n);

        return coveredNodes;
    }

    /**
     * Verify that the solution makes the network entirely 1-identifiable
     * @param solution an array containing the indices of the selected routes
     * @return a bitset describing which pairs of nodes are not distinguishable under the given solution
     */
    public BitSet undistinguishablesPairs(Set<Integer> solution) {
        BitSet discrimPairs = new BitSet(n *(n -1)/2);
        for (Integer i: solution) {
            discrimPairs.or(paths[i].getDistinguishedSet(n));
        }

        discrimPairs.flip(0, n *(n -1)/2);

        return discrimPairs;
    }

    /**
     * Verify that the solution is a valid solution
     * @param solution an array containing the indices of the selected routes
     * @param verbose, when it is set to true an explanation of why the solution is unvalid is printed
     * @return true iff the solution is valid, false otherwise
     */
    public boolean isValid(Set<Integer> solution, boolean verbose) {
        BitSet uncoveredNodes = uncoveredNodes(solution);
        BitSet undistinguishablesPairs = undistinguishablesPairs(solution);

        if (verbose) {
            for (int i = 0; i < n; i++) {
                if (uncoveredNodes.get(i)) {
                    System.out.printf("Node %d is uncovered\n", i);
                }
            }

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (undistinguishablesPairs.get(hashPair(i, j, n))) {
                        System.out.printf("Nodes %d and %d have the same symptoms\n", i, j);
                    }
                }
            }
        }

        return uncoveredNodes.cardinality() == 0 && undistinguishablesPairs.cardinality() == 0;
    }

    /**
     * Save the solution in a file that will be read by Inginious
     * Call it every time you find a new best solution
     * Warning : Calling it will remove the previous saved solution.
     * Do not modify this code, or Inginious may not be able to read solution
     * @param solution an array containing the indices of the selected routes
     */
    public void writeSolution(Set<Integer> solution) {
        try {
            FileWriter writer = new FileWriter("solution.tmp");
            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append(solution.size()).append("\n");
            for (Integer routeIndex : solution) {
                outputBuilder.append(this.getPath(routeIndex));
                outputBuilder.append("\n");
            }
            writer.write(outputBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a particular path
     * @param index the index of the desired path
     * @return the desired path
     */
    public Path getPath(int index) {
        return paths[index];
    }

}
