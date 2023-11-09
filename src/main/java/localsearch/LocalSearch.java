package localsearch;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

import util.NotImplementedException;

public class LocalSearch {

    Problem problem;
    
    // use these variables to keep track of your current and current best candidate solutions
    Candidate bestCandidate, currentCandidate;

    // variables to record the CPU time spent by the search
    ThreadMXBean thread;
    long startTime, timeLimit;


    public LocalSearch(Problem problem) {
        this.problem = problem;
        this.thread = ManagementFactory.getThreadMXBean();

        // TODO initialize additional fields you need
    }

    /**
     * Launches the local search and returns the best possible solution under the given time limit
     * @param timeLimit the maximum time allowed, in seconds
     * @return a candidate solution to the problem
     */
    public Candidate solve(int timeLimit) {
        this.startTime = this.thread.getCurrentThreadCpuTime();
        this.timeLimit = 1000000000L * timeLimit;

        // TODO implement your own local search algorithm
        //  use the method mustStop() to check if your solver needs to stop searching for an improving solution
         throw new NotImplementedException("LocalSearch.solve");
    }

    /**
     * Reset the search to restart from a new current solution
     * Hint: Randomly perturbate the current best solution
     */
    public void resetSearch() {
        // TODO implement and use this function to restart your local search
    }

    /**
     * Computes the n best feasible swaps that can be performed on the currentCandidate
     * Each swap being returned has its delta being set (i.e. {@link Swap#getDelta()} gives the correct delta)
     *
     * @param n the maximum number of swaps to keep
     * @return a list of the n best feasible swaps
     */
    public List<Swap> getNBestSwaps(int n) {
        // TODO compute the list of the n best swaps from the currentSolution
        //  suggestion: experiment with swaps of different sizes (see swap.getExtendedSwapWith(...))
        //  Each swap being returned must have its delta being set (use swap.getDelta(candidate.getDelta(swap)))
         throw new NotImplementedException("LocalSearch.getNBestSwaps");
    }

    /**
     * Select a swap from the given list
     * @param swaps a list of swaps
     * @return the selected swap move
     */
    public Swap selectSwap(List<Swap> swaps) {
        // TODO select a swap from the given list
        //  Suggestion: do not always pick the best one, to have some diversification
         throw new NotImplementedException("LocalSearch.selectSwap");
    }

    /**
     * Replaces the bestCandidate by the currentCandidate if it improves the value
     * The problem is assumed to be a minimization problem
     * @return true if the bestCandidate was updated
     */
    public boolean maybeSaveCurrentCandidate() {
        // TODO update the bestCandidate if the currentCandidate is better
         throw new NotImplementedException("LocalSearch.maybeSaveCurrentCandidate");
    }

    /**
     * Checks whether the time limit has been reached
     * @return true if the time limit has been reached
     */
    private boolean mustStop() {
        return this.thread.getCurrentThreadCpuTime() >= this.startTime + this.timeLimit;
    }

}
