package competition;

import java.util.*;

/**
 * Greedy algorithm that selects paths in descending order of size until all the nodes
 * are covered or all the pairs of nodes are distinguishable.
 */
public class GreedySolver extends Solver {

    public GreedySolver(PathSelectionInstance problem) {
        super(problem);
    }

    /**
     * @param coveredNodes a bitset describing which nodes are already covered
     * @param path a bitset describing which nodes are crossed by the considered path
     * @return true iff the considered path crossed at least one uncovered node
     */
    public boolean improvesCover(BitSet coveredNodes, BitSet path) {
        for (int i = 0; i < problem.n; i++) {
            if (!coveredNodes.get(i) && path.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param distinguishablesPairs a bitset describing which pair of nodes are already distinguishable
     * @param distinguishSet a bitset describing which pair of nodes are distinguished by the considered path
     * @return true iff the considered path distinguish at least one pair of nodes that still have the same symptom
     */
    public boolean improves1id(BitSet distinguishablesPairs, BitSet distinguishSet) {
        for (int i = 0; i < problem.n * (problem.n - 1) / 2; i++) {
            if (!distinguishablesPairs.get(i) && distinguishSet.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Greedy algorithm to solve the path selection problem
     * @return a set with the indices of the selected paths
     */
    private Set<Integer> solveGreedy() {

        BitSet coveredNodes = new BitSet(problem.n);
        BitSet distinguishablePairs = new BitSet(problem.n *(problem.n -1)/2);

        Integer[] pathIndexes = new Integer[problem.m];
        for (int i = 0; i < problem.m; i++) {
            pathIndexes[i] = i;
        }

        // Paths are sorted following there size, in descending order
        Arrays.sort(pathIndexes, (Integer a, Integer b) -> -Integer.compare(problem.getPath(a).nodes.cardinality(),
                problem.getPath(b).nodes.cardinality()));

        int index = 0;
        Set<Integer> solution = new HashSet<>();
        // Iterate on paths until a valid solution is found or until each path has been considered
        while ((coveredNodes.cardinality() != problem.n ||
                distinguishablePairs.cardinality() != problem.n *(problem.n -1)/2) &&
                index < problem.m) {

            // If the route allows to cover at least one uncovered node or to distinguish at least one pair of nodes
            // that have the same symptom, then it is added to the solution
            if (improvesCover(coveredNodes, problem.getPath(pathIndexes[index]).nodes) ||
            improves1id(distinguishablePairs,problem.getPath(pathIndexes[index]).getDistinguishedSet(problem.n))) {
                solution.add(pathIndexes[index]);
                coveredNodes.or(problem.getPath(pathIndexes[index]).nodes);
                distinguishablePairs.or(problem.getPath(pathIndexes[index]).getDistinguishedSet(problem.n));
            }
            index++;
        }

        return solution;
    }

    /**
     * Method called to solve the function
     * @return an array containing the indices of the selected paths
     */
    @Override
    public Set<Integer> solve() {
        // TODO Implement methods to solve the problem in the most efficient way possible
        // you can use the given greedy algorithm or remove it;
        Set<Integer> solution = solveGreedy();
        problem.writeSolution(solution);
        return solution;
    }

}
