package mdd.framework.implem.solver;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import mdd.framework.core.CompilationInput;
import mdd.framework.core.CompilationType;
import mdd.framework.core.Decision;
import mdd.framework.core.DecisionDiagram;
import mdd.framework.core.Frontier;
import mdd.framework.core.Problem;
import mdd.framework.core.Relaxation;
import mdd.framework.core.Solver;
import mdd.framework.core.SubProblem;
import mdd.framework.heuristics.StateRanking;
import mdd.framework.heuristics.VariableHeuristic;
import mdd.framework.heuristics.WidthHeuristic;
import mdd.framework.implem.mdd.LinkedDecisionDiagram;

/**
 * From the lecture, you should have a good grasp on what a branch-and-bound
 * with mdd solver does even though you haven't looked into concrete code 
 * yet.
 * 
 * One of the tasks from this assignment is for you to implement the vanilla
 * algorithm (sequentially) as it has been explained during the lecture. 
 * 
 * To help you, we provide you with a well documented framework that defines
 * and implements all the abstractions you will need in order to implement 
 * a generic solver. Additionally, and because the BaB-MDD framework parallelizes 
 * *VERY* well, we provide you with a parallel implementation of the algorithm
 * (@see ParallelSolver). Digging into that code, understanding it, and stripping
 * away all the parallel-related concerns should finalize to give you a thorough
 * understanding of the sequential algo.
 * 
 * # Note
 * ONCE YOU HAVE A CLEAR IDEA OF HOW THE CODE WORKS, THIS TASK SHOULD BE EXTREMELY
 * EASY TO COMPLETE.
 */
public final class SequentialSolver<T> implements Solver {
    /** The problem we want to maximize */
    private final Problem<T> problem;
    /** A suitable relaxation for the problem we want to maximize */
    private final Relaxation<T> relax;
    /** An heuristic to identify the most promising nodes */
    private final StateRanking<T> ranking;
    /** An heuristic to chose the maximum width of the DD you compile */
    private final WidthHeuristic<T> width;
    /** An heuristic to chose the next variable to branch on when developing a DD */
    private final VariableHeuristic<T> varh;

    /**
     * This is the fringe: the set of nodes that must still be explored before
     * the problem can be considered 'solved'.
     *
     * # Note:
     * This fringe orders the nodes by upper bound (so the highest ub is going
     * to pop first). So, it is guaranteed that the upper bound of the first
     * node being popped is an upper bound on the value reachable by exploring
     * any of the nodes remaining on the fringe. As a consequence, the
     * exploration can be stopped as soon as a node with an ub <= current best
     * lower bound is popped.
     */
    private final Frontier<T> frontier;
    /** 
     * Your implementation (just like the parallel version) will reuse the same
     * data structure to compile all mdds. 
     * 
     * # Note: 
     * This approach is recommended but we do not force this design choice on you.
     * You might decide against reusing the same object over and over (even though
     * it has been designed to be reused). Should you decide to not reuse this
     * object, then you can simply ignore this field (and remove it altogetger).
     */
    private final DecisionDiagram<T> mdd;

    /** This is the value of the best known lower bound. */
    private int bestLB;
    /** If set, this keeps the info about the best solution so far. */
    private Optional<Set<Decision>> bestSol;

    /** Creates a fully qualified instance */
    public SequentialSolver(
        final Problem<T> problem,
        final Relaxation<T> relax,
        final VariableHeuristic<T> varh,
        final StateRanking<T> ranking,
        final WidthHeuristic<T> width,
        final Frontier<T> frontier)  
    {
        this.problem = problem;
        this.relax   = relax;
        this.varh    = varh;
        this.ranking = ranking;
        this.width   = width;
        this.frontier= frontier;
        this.mdd     = new LinkedDecisionDiagram<>();
        this.bestLB  = Integer.MIN_VALUE;
        this.bestSol = Optional.empty();
    }

    @Override
    public void maximize() {
        // TODO Now it is your turn to implement the (sequential) BaB-MDD algorithm.
        // TODO Try to stick really close to the pseudo-code, this is going to help you !
    }

    @Override
    public Optional<Integer> bestValue() {
        if (bestSol.isPresent()) {
            return Optional.of(bestLB);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Set<Decision>> bestSolution() {
        return bestSol;
    }

    // TODO You might want to implement some additional utility methods for yourself
}
