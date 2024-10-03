package branchandbound;

import util.UF;

public interface TSPLowerBound {

    /**
     *
     * @param distanceMatrix a symmetrical distance matrix
     * @param excludedEdges a matrix of boolean indicating if an edge is excluded
     *                      if excludedEdges[i][j] is true then the edge i->j is excluded
     *                      Be careful that this matrix may not be symmetrical
     * @return A lower bound on the TSP and the (non excluded) edges used to compute it
     */
    TSPLowerBoundResult compute(double [][] distanceMatrix, boolean [][] excludedEdges);


}
