package localsearch;

import util.tsp.TSPInstance;
import java.util.*;

public abstract class BeamSearchInitialization extends Initialization {

    protected int beamWidth;

    public BeamSearchInitialization(TSPInstance tsp, int beamWidth) {
        super(tsp);
        this.beamWidth = beamWidth;
    }

    /** Defines how to expand a partial solution */
    protected abstract List<PartialSolution> expand(PartialSolution s);

    /**
     * Generates an initial solution using beam search
     * at each iteration, computes all possible expansions of the partial solutions in the beam
     * keeps only the best beamWidth partial solutions for the next iteration
     * Note : initially, the beam contains only one partial solution with the first city (0) visited
     * @return a Candidate representing the initial solution
     */
    @Override
    public Candidate getInitialSolution() {
        // TODO implement a method generates an initial solution using beam search, keeping beamWidth partial solutions at each step
         throw new util.NotImplementedException("BeamSearchInitialization.getInitialSolution");
    }

    /**
     * Evaluate the cost of a partial solution (not complete tour)
     * @param tour
     * @return the cost of the partial solution : the sum of distances between consecutive cities
     */
    protected double evaluatePartialSolution(ArrayList<Integer> tour) {
        double distance = 0;
        for (int i = 0; i < tour.size() - 1; i++)
            distance += tsp.distance(tour.get(i), tour.get(i + 1));
        return distance;
    }

    /**
     * A class representing a partial solution in the beam search
     * contains the remaining cities to visit as a BitSet
     * the current solution as an ArrayList of integers
     * and the cost of the partial solution
     */
    protected static class PartialSolution {
        ArrayList<Integer> solution;
        double cost;
        BitSet toVisit;

        PartialSolution(ArrayList<Integer> solution, double cost, BitSet toVisit) {
            this.solution = solution;
            this.cost = cost;
            this.toVisit = toVisit;
        }
    }
}
