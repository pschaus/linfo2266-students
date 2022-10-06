package branchandbound;

import util.tsp.TSPInstance;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



import util.tsp.TSPInstance;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BranchAndBoundTSP {

    /**
     * Entry point for your Branch and Bound TSP Solver
     *
     * You must use the BrandAndBound class without modifying it.
     * Have a look at @see {@link branchandbound.BranchAndBoundKnapsack} for an example.
     * As you will see, you have to implement your own State/Node class.
     *
     * One possibility for representing the state of a node
     * is to store the forbidden edges, set forbidden
     * by the ancestor branching decisions.
     * Therefore, when you find a one-tree (that is a lower-bound)
     * not containing the forbidden edges, you can stop branching since
     * this constitutes a candidate solution and thus a leaf-node.
     *
     *
     * @param instance an instance for the TSP
     * @param lbAlgo a lowe-bound algorithm for TSP that computes a  one-tree
     * @return the list of edges in the optimal solution
     */
    public static List<Edge> optimize(TSPInstance instance, OneTreeLowerBound lbAlgo) {
         return null;
    }

    public static void main(String[] args) {
    }




}
