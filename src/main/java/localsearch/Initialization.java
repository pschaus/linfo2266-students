package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;

/**
 * Interface for initializing the tour, that
 * is finding a first solution to the TSP problem
 */
public abstract class Initialization {

    protected TSPInstance tsp;

    public Initialization(TSPInstance tsp) {
        this.tsp = tsp;
    }

    public abstract Candidate getInitialSolution();
}
