package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Initializes with a random tour
 */
public class RandomInitialization extends Initialization {


    public RandomInitialization(TSPInstance tsp) {
        super(tsp);
    }

    @Override
    public Candidate getInitialSolution() {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < tsp.nCities(); i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        return new Candidate(tsp, list);
    }
}
