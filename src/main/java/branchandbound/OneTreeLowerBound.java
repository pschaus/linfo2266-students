package branchandbound;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A lower bound on the TSP based on the 1-tree relaxation
 * A minimum 1-tree is composed of the edges:
 * - that belong to the minimum spanning-tree that spans all
 *   nodes of the graph (independently of their direction), except the node 0.
 * - the two edges that connect the node 0 to the two
 *   closest nodes (also independently of their direction).
 */
public class OneTreeLowerBound implements TSPLowerBound {

    @Override
    public TSPLowerBoundResult compute(double [][] distMatrix, boolean [][] excluded) {
        // TODO
         return null;
    }





}
