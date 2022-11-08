package localsearch;

public interface Problem {

    /**
     * Returns the number of variables of the problem
     * @return the number of variables of the problem
     */
    int getNumberOfVariables();
    
    /**
     * Returns a valid candidate solution of the problem
     * It will be used as the starting point of the local search algorithm
     * @return a valid candidate solution
     */
    Candidate getInitialCandidate();

    /**
     * Checks whether applying the swap to the given candidate solution
     * yields another feasible candidate solution
     * @param candidate a candidate solution
     * @param swap a swap move
     * @return true if the resulting candidate solution is feasible
     */
    boolean isFeasible(Candidate candidate, Swap swap);

}
