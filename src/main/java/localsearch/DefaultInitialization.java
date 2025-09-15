package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;

/***
 * Initializes with a tour being as the list 0 -> 1 -> ... -> n-1
 */
public class DefaultInitialization extends Initialization {
    public DefaultInitialization(TSPInstance tsp) {
        super(tsp);
    }

    @Override
    public Candidate getInitialSolution() {
        int[] tour = new int[tsp.nCities()];
        for (int i = 0; i < tsp.nCities(); i++) {
            tour[i] = i;
        }
        return new Candidate(tsp, tour);
    }


}
