package localsearch;

import java.util.HashMap;

/**
 * Implements a KOpt neighborhood for the TSP problem
 * also known as the Lin-Kernighan heuristic
 */
public class KOptSelection implements NeighborSelection {

    int maxK;

    public KOptSelection(int maxK) {
        this.maxK = maxK;
    }

    @Override
    public Candidate getNeighbor(Candidate candidate) {
        // TODO implement a KOpt neighborhood by repeatedly applying the best 2Swap k times then return the best solution found
        // hint : use a map to save intermediate solutions
         throw new util.NotImplementedException("KOpt.getNeighbor");
    }

}
