package competition;

import java.util.*;
import java.util.Set;

public class GreedySolver extends Solver {

    public GreedySolver(SetCoverInstance problem) {
        super(problem);
    }

    /**
     * @param coveredNodes a bitset describing which elements are already covered
     * @param set a bitset describing which elements are covered by the considered set
     * @return true iff the considered set crossed at least one uncovered element
     */
    public boolean improvesCover(BitSet coveredNodes, BitSet set) {
        for (int i = 0; i < problem.n; i++) {
            if (!coveredNodes.get(i) && set.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Greedy algorithm to solve the set cover problem
     * @return an array containing the indices of the selected sets
     */
    private Set<Integer> solveGreedy() {

        BitSet coveredElements = new BitSet(problem.n);

        Integer[] setIndexes = new Integer[problem.m];
        for (int i = 0; i < problem.m; i++) {
            setIndexes[i] = i;
        }

        // Paths are sorted following there size, in descending order
        Arrays.sort(setIndexes, (Integer a, Integer b) -> -Integer.compare(problem.getSet(a).cardinality(),
                problem.getSet(b).cardinality()));

        int index = 0;
        Set<Integer> solution = new HashSet<>();
        // Iterate on sets until a valid solution is found or until each set has been considered
        while (coveredElements.cardinality() != problem.n  && index < problem.m) {

            // If the route allows to cover at least one uncovered element or to distinguish at least one pair of elements
            // that have the same symptom, then it is added to the solution
            if (improvesCover(coveredElements, problem.getSet(setIndexes[index]))) {
                solution.add(setIndexes[index]);
                coveredElements.or(problem.getSet(setIndexes[index]));
            }
            index++;
        }

        return solution;
    }

    /**
     * Method called to solve the function
     * @return an array containing the indices of the selected sets
     */
    public Set<Integer> solve() {
        // TODO Implement methods to solve the problem in the most efficient way possible
        // you can use the given greedy algorithm or remove it;
        Set<Integer> solution = solveGreedy();
        problem.writeSolution(solution);
        return solution;
    }

}
