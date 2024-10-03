package branchandbound;


import util.tsp.TSPInstance;

import java.util.LinkedList;
import java.util.List;


/**
 * Compute the cheapest incident edges lower bound for the TSP
 * For each node, the cheapest incoming edge is selected.
 * The sum of these edges is a lower bound on the TSP
 */
public class CheapestIncidentLowerBound implements TSPLowerBound {




    public CheapestIncidentLowerBound() {
    }

    @Override
    public TSPLowerBoundResult compute(double [][] distanceMatrix, boolean [][] excludedEdges) {
        // TODO
         return null;
    }
}
