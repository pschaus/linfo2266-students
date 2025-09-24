package competition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Set;

public class SetCoverInstance {

    protected int n; // Number of elements;
    protected int m; // Number of sets;

    // Available sets
    // !!! Do not modify or sort this array directly (use a copy instead)
    private BitSet[] sets;


    /**
     * Initialize the problem
     * @param filename: path to the instance
     */
    public SetCoverInstance(String filename) {
        parseInstance(filename);
    }

    /**
     * Load the instance
     * @param filename: path to the instance
     */
    public void parseInstance(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String currentLine = reader.readLine();
            this.n = Integer.parseInt(currentLine.split(" ")[0]);
            this.m = Integer.parseInt(currentLine.split(" ")[1]);
            this.sets = new BitSet[this.m];
            int setIndex = 0;
            currentLine = reader.readLine();

            while (currentLine != null) {
                if (!currentLine.trim().isEmpty()) {

                    BitSet elements = new BitSet(n);
                    for (String elementStr: currentLine.trim().split(" ")) {
                        elements.set(Integer.parseInt(elementStr));
                    }

                    this.sets[setIndex++] = elements;
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
     * Verify that the solution makes the universe entirely covered
     * @param solution an array containing the indices of the selected sets
     * @return a bitset describing which elements are uncovered by the given solution
     */
    public BitSet uncoveredElements(Set<Integer> solution) {
        BitSet coveredElements = new BitSet(this.n);
        for (Integer i : solution) {
            coveredElements.or(sets[i]);
        }
        // reverse each value in the bitset
        coveredElements.flip(0, coveredElements.length());

        return coveredElements;
    }

    /**
     * Verify that the solution is a valid solution
     * @param solution an array containing the indices of the selected routes
     * @param verbose, when it is set to true an explanation of why the solution is unvalid is printed
     * @return true iff the solution is valid, false otherwise
     */
    public boolean isValid(Set<Integer> solution, boolean verbose) {
        BitSet uncoveredElements = uncoveredElements(solution);

        if (verbose) {
            for (int i = 0; i < n; i++) {
                if (uncoveredElements.get(i)) {
                    System.out.printf("Element %d is uncovered\n", i);
                }
            }
        }

        return uncoveredElements.cardinality() == 0;
    }

    /**
     * Save the solution in a file that will be read by Inginious
     * Call it every time you find a new best solution
     * Warning : Calling it will remove the previous saved solution.
     * Do not modify this code, or Inginious may not be able to read solution
     * @param solution an array containing the indices of the selected sets
     */
    public void writeSolution(Set<Integer> solution) {
        try {
            FileWriter writer = new FileWriter("solution.tmp");
            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append(solution.size()).append("\n");
            for (Integer setIndex : solution) {
                for (int elem = sets[setIndex].nextSetBit(0); elem >= 0; elem = sets[setIndex].nextSetBit(elem + 1)) {
                    outputBuilder.append(elem).append(" ");
                }
                outputBuilder.append("\n");
            }
            writer.write(outputBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a particular set
     * @param index the index of the desired set
     * @return the desired set
     */
    public BitSet getSet(int index) {
        return sets[index];
    }

}
