package localsearch;

import java.util.ArrayList;

import util.NotImplementedException;

public class LocalSearch {

    Problem problem;
    
    // use these variables to keep track of your current and current best candidate solutions
    Candidate bestCandidate, currentCandidate;


    public LocalSearch(Problem problem) {
        this.problem = problem;
        // TODO initialize additional fields you need
    }

    /**
     * Launches the local search and returns the best possible solution under the given time limit
     * @param timeLimit the maximum time allowed, in seconds
     * @return a candidate solution to the problem
     */
    public Candidate solve(int timeLimit) {
        // TODO implement your own local search algorithm
         throw new NotImplementedException("LocalSearch.solve");
    }

    /**
     * Reset the search to restart from a new current solution
     * Hint: Randomly perturbate the current best solution
     */
    private void resetSearch() {
        // you can implement and use this function to restart your local search
    }

    /**
     * Computes the n best feasible swaps that can be performed on the currentCandidate
     * @param n the maximum number of swaps to keep
     * @return a list of the n best feasible swaps
     */
    private ArrayList<Swap> getNBestSwaps(int n) {
        // TODO compute the list of the n best swaps from the currentSolution
        // suggestion: experiment with swaps of different sizes (see swap.getExtendedSwapWith(...))
         throw new NotImplementedException("LocalSearch.getNBestSwaps");
    }

    /**
     * Select a swap from the given list
     * Suggestion: do not always pick the best one
     * @param bestSwaps a list of swaps
     * @return the selected swap move
     */
    private Swap selectSwap(ArrayList<Swap> swaps) {
        // TODO select a swap from the given list
         throw new NotImplementedException("LocalSearch.selectSwap");
    }

    /**
     * Replaces the bestCandidate by the currentCandidate if it improves the value
     * The problem is assumed to be a minimization problem
     * @return true if the bestCandidate was updated
     */
    private boolean maybeSaveCurrentCandidate() {
        // TODO update the bestCandidate if the currentCandidate is better
         throw new NotImplementedException("LocalSearch.maybeSaveCurrentCandidate");
    }

    /**
     * Checks whether the time limit has been reached
     * @return true if the time limit has been reached
     */
    private boolean mustStop() {
        // TODO check whether the time limit has been reached
         throw new NotImplementedException("LocalSearch.mustStop");
    }

}
