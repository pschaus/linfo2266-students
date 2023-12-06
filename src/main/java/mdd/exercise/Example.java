package mdd.exercise;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import mdd.framework.core.Frontier;
import mdd.framework.core.Problem;
import mdd.framework.core.Relaxation;
import mdd.framework.core.Solver;
import mdd.framework.heuristics.StateRanking;
import mdd.framework.heuristics.VariableHeuristic;
import mdd.framework.heuristics.WidthHeuristic;
import mdd.framework.implem.frontier.SimpleFrontier;
import mdd.framework.implem.heuristics.DefaultVariableHeuristic;
import mdd.framework.implem.heuristics.FixedWidth;
import mdd.framework.implem.solver.ParallelSolver;
import mdd.framework.implem.solver.SequentialSolver;
import util.decarbonation.MaximumDecarbonationInstance;

/** 
 * This class provides you with utility methods you might want to reuse
 * as well as with an example of how to solve the maximum decarbonation 
 * problem.
 */
public final class Example {
    /**
     * This is how you would go about solving an actual instance of the problem. For more details, 
     * feel free to check the code of the utility methods that have been defined hereunder.
     * 
     * @param args the parameters passed on from the command line (not used)
     */
    public static void main(final String[] args) throws IOException {
        final String instance = 
            "c The example instance from the assigment \n" +
            "c The expected solution to this instance is: {1, 4, 5, 6} \n" +
            "c and hence an objective value of 4 \n" +
            "p edge 6 7 \n" +
            "e 1 2 \n" +
            "e 2 3 \n" +
            "e 2 4 \n" +
            "e 2 5 \n" +
            "e 3 4 \n" +
            "e 3 5 \n" +
            "e 3 6 \n"
            ;
        
        //Set<Integer> solution = solveSequentialFromString(instance, 2);
        Set<Integer> solution = solveSequentialFromFile("data/decarbonation/keller4.clq", 100);
        System.out.println(solution);
    }

    /** 
     * This method lets you solve a max decarbonation instance using a given max layer width.
     * 
     * @param fname the path to the instance file
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     * @param nbThreads the number of threads that can be used to solve this problem
     * 
     * @return an optimal solution to the given instance
     */
    public static Set<Integer> solveParallelFromFile(final String fname, final int maximumWidth, final int nbThreads) throws IOException {
        return solveParametric(MaximumDecarbonationInstance.fromFile(fname), maximumWidth, parallel(nbThreads));
    }
    /** 
     * This method lets you solve a max decarbonation instance using a given max layer width.
     * 
     * @param text the instance in text format
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     * @param nbThreads the number of threads that can be used to solve this problem
     * 
     * @return an optimal solution to the given instance
     */
    public static Set<Integer> solveParallelFromString(final String text, final int maximumWidth, final int nbThreads) {
        return solveParametric(MaximumDecarbonationInstance.fromString(text), maximumWidth, parallel(nbThreads));
    }
    
    /** 
     * This method lets you solve a max decarbonation instance using a given max layer width.
     * 
     * @param fname the path to the instance file
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     * 
     * @return an optimal solution to the given instance
     */
    public static Set<Integer> solveSequentialFromFile(final String fname, final int maximumWidth) throws IOException {
        return solveParametric(MaximumDecarbonationInstance.fromFile(fname), maximumWidth, SEQUENTIAL);
    }
    /** 
     * This method lets you solve a max decarbonation instance using a given max layer width.
     * 
     * @param text the instance in text format
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     * 
     * @return an optimal solution to the given instance
     */
    public static Set<Integer> solveSequentialFromString(final String text, final int maximumWidth) {
        return solveParametric(MaximumDecarbonationInstance.fromString(text), maximumWidth, SEQUENTIAL);
    }
    
    /** 
     * This functional interface allows one to seamlessly create a new solver from the given set of
     * parameters. In all generality, you should think of it as a generic constructor.
     */
    @FunctionalInterface
    private static interface SolverFactory {
        /** 
         * Creates a new bab-mdd solver 
         *
         * @param problem the dp formulation of the problem
         * @param relax the relaxation of the dp model
         * @param varh the variable heuristic which is used to tell what variable to use next when compiling DDs
         * @param ranking a heuristic to tell the most promising of two nodes
         * @param width the strategy used to tell the maximum allowed width for a layer when compiling a DD.
         * @param frontier the solver frontier (aka Fringe).
         */
        <T> Solver create(
                final Problem<T> problem, 
                final Relaxation<T> relax, 
                final VariableHeuristic<T> varh,
                final StateRanking<T> ranking, 
                final WidthHeuristic<T> width,
                final Frontier<T> frontier);
    } 
    /** This factory creates a sequential solver (YOU NEED TO IMPLEMENT THE SEQUENTIAL SOLVER) */
    private static final SolverFactory SEQUENTIAL = SequentialSolver::new;
    /** This method yields a factory that can create a parallel solver */
    private static final SolverFactory parallel(final int nbThreads) {
        return new SolverFactory() {
            @Override
            public <T> Solver create(
                final Problem<T> p, 
                final Relaxation<T> r, 
                final VariableHeuristic<T> v,
                final StateRanking<T> s, 
                final WidthHeuristic<T> w, 
                final Frontier<T> f) {
                return new ParallelSolver<T>(nbThreads, p, r, v, s, w, f);
            }
        };
    }

    /** 
     * This method lets you solve a max decarbonation instance.
     * 
     * @param instance the instance you want to solve
     * @param maximumWidth the maximum layer width for any of the mdd you will compile
     * @param isolv a factory to create the solver
     * 
     * @return an optimal solution to the given instance
     */
    private static Set<Integer> solveParametric(final MaximumDecarbonationInstance instance, final int maximumWidth, final SolverFactory isolv) {
        final Problem<MaximumDecarbonationState>        problem = new MaximumDecarbonationProblem(instance);
        final Relaxation<MaximumDecarbonationState>       relax = new MaximumDecarbonationRelaxation(instance);
        final StateRanking<MaximumDecarbonationState>   ranking = new MaximumDecarbonationStateRanking();
        final VariableHeuristic<MaximumDecarbonationState> varh = new DefaultVariableHeuristic<>();
        final WidthHeuristic<MaximumDecarbonationState>   width = new FixedWidth<>(maximumWidth);
        final Frontier<MaximumDecarbonationState>      frontier = new SimpleFrontier<>(ranking);
        final Solver solver = isolv.create(problem, relax, varh, ranking, width, frontier);
        solver.maximize();

        return solver.bestSolution()
            .map(decisions -> 
                decisions.stream()
                    .filter(d -> d.val() == 1)     // reproduce only those sites for which it was decided to include the site in solution (this is a hint)
                    .map(d -> 1 + d.var())         // variable identifiers are 0-based. sites are 1-based
                    .collect(Collectors.toSet()))  // fetch all results
            .get();                                // this problem always admits a solution (no build site is a solution)
    }
}
