package localsearch;

import util.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a candidate solution for the TSP problem
 */
public class Candidate {

    // The tour of the candidate solution from the first city
    // to the last, the first and last city is not repeated
    private int[] tour; // a permutation of the cities from 0 to n-1

    private TSPInstance tsp;
    private double cost;

    public Candidate(TSPInstance tsp, int[] tour) {
        this.tsp = tsp;
        this.tour = tour;
        this.cost = computeCost();
    }

    public TSPInstance getTsp() {
        return tsp;
    }

    public double getCost() {
        return cost;
    }

    /**
     *returns the city that is after the city placed at index index
     * @param index
     * @return an int that is the city
     */
    public int getSucc(int index) {
        return tour[(index + 1) % tour.length];
    }

    public double computeCost() {
        // computes the initial cost of the solution : only called once
        // at initialisation and updated using twoOptDelta at each move
        double sum = 0;
        for (int i = 0; i < tour.length; i++) {
            sum += tsp.distance(tour[i], getSucc(i));
        }
        return sum;
    }


    /**
     * Compute the delta of the 2-opt move between index1 and index2
     *
     * @param index1 the first index (excluded)
     * @param index2 > index1, the second index (included)
     * @return the delta of the 2-opt move, that is the cost after the move minus the cost before the move
     */
    public double twoOptDelta(int index1, int index2) {
        return tsp.distance(tour[index1], tour[index2]) +
                tsp.distance(tour[(index1 + 1) % tour.length], tour[(index2 + 1) % tour.length]) -
                tsp.distance(tour[index1], tour[(index1 + 1) % tour.length]) -
                tsp.distance(tour[index2], tour[(index2 + 1) % tour.length]);
    }


    /**
     * Apply the 2-opt move by reverting the order of the cities between
     * index1 (excluded) and index2 (included) if index1 > index2 they need to be swapped
     * and also update the cost of the solution
     * @param index1 the first index (excluded)
     * @param index2 the second index (included)
     */
    public void twoOpt(int index1, int index2) {

        // TODO Swap the successor of index1 with index 2. Note : index1 can be greater than index2
         throw new util.NotImplementedException("Candidate.applySwap");
    }


    @Override
    public Candidate clone() {
        Candidate c = new Candidate(tsp, this.tour);
        return c;
    }

    public int[] getTour() {
        return tour;
    }

}
