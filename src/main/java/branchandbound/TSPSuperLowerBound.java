package branchandbound;

import util.UF;

public class TSPSuperLowerBound implements TSPLowerBound {

    TSPLowerBound algo1 = new HeldKarpLowerBound();
    TSPLowerBound algo2 = new CheapestIncidentLowerBound();
    @Override
    public TSPLowerBoundResult compute(double[][] distanceMatrix, boolean[][] excludedEdges) {
        TSPLowerBoundResult res1 = algo1.compute(distanceMatrix,excludedEdges);
        TSPLowerBoundResult res2 = algo2.compute(distanceMatrix,excludedEdges);
        return res1.lb() > res2.lb() ? res1 : res2;
    }
}
