package astar.solver;

import util.Solution;

import java.util.*;
import java.util.function.BiConsumer;

public class Astar<S extends State> implements Solver {
    Model<S> model;                    // the model to solve
    Map<S, S> predecessors;            // a map to store the best known predecessor for each state
    Map<S, Integer> previousDecision;  // a map to store, for each state, the decision took from the best predecessor to reach the state
    Map<S, Double> g;                  // cost of best known path

    /**
     * Creates a Astar object for the given problem
     * @param model the model to solve
     */
    public Astar(Model<S> model) {
        this.model = model;
        this.predecessors = new HashMap<>();
        this.previousDecision = new HashMap<>();
        this.g = new HashMap<>();
    }

    /**
     * Search for the optimal solution of the given problem.
     * @return the solution containing the objective value and the decisions taken
     */
    @Override
    public void solve(BiConsumer<Solution, SearchStatistics> onSolution) {
        long startTime = System.currentTimeMillis();
        int nodesExpanded = 0;

        PriorityQueue<Node<S>> pq = new PriorityQueue<>(); //  Open set
        S root = model.getRootState();
        predecessors.put(root, null);
        previousDecision.put(root, -1);
        g.put(root, 0.0);
        pq.add(new Node<>(root, model.h(root)));

        while (!pq.isEmpty()) {
            S current = pq.poll().state;
            nodesExpanded++;

            if (model.isTerminalState(current)) {
                onSolution.accept(rebuildSolution(current, g.get(current)),
                        new SearchStatistics(System.currentTimeMillis() - startTime,nodesExpanded));// Goal reached !
                return;
            }

            for (Transition<S> t: model.getTransitions(current)) {
                S successor = t.getSuccessor();

                double newG = g.get(current) + t.getValue();
                if (model.isTerminalState(successor)) {
                    newG += model.getTerminalStateValue(successor);
                }

                if (g.getOrDefault(successor, Double.MAX_VALUE) > newG) {
                    g.put(successor, newG);
                    predecessors.put(successor, current);
                    previousDecision.put(successor, t.getDecision());
                    double f = newG + model.h(successor); // Score : actual cost + heuristic estimation of the remaining cost to reach the terminal state
                    pq.add(new Node<>(successor, f));
                }
            }
        }

        throw new IllegalArgumentException("No solution found");
    }

    /**
     * Rebuild and returns the optimal solution of the problem and the set of decisions taken to obtain it
     * @return the optimal solution of the problem and the set of decisions taken to obtain it
     */
    private Solution rebuildSolution(S state, double value) {
        LinkedList<Integer> decisions = new LinkedList<>();
        while (previousDecision.get(state) != -1) {
            decisions.addFirst(previousDecision.get(state));
            state = predecessors.get(state);
        }
        return new Solution(value, decisions);
    }
}