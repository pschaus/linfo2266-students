package localsearch;

import util.tsp.TSPInstance;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The main class to run the local search
 */
public class LocalSearch {

    private Candidate bestCandidate, currentCandidate;
    private NeighborSelection neighborSelection;
    private Initialization initialization;
    public boolean hasImproved = true;

    int it = 0;

    public LocalSearch(NeighborSelection neighborSelection, Initialization initialization) {
        this.neighborSelection = neighborSelection;
        this.initialization = initialization;
    }

    public Candidate run() {
        currentCandidate = initialization.getInitialSolution();
        bestCandidate = currentCandidate.clone();
        while (stopCriterion()) {
            hasImproved = false;
            it++;

            Candidate nCandidate = neighborSelection.getNeighbor(currentCandidate.clone());
            if (nCandidate.getCost() < currentCandidate.getCost()) {
                hasImproved = true;
                bestCandidate = nCandidate.clone();
            }

            currentCandidate = nCandidate;
        }
        return bestCandidate;
    }



    public boolean stopCriterion() {
        // you can edit this method for your tests/graphs if needed
        return it < 200;
    }

}


