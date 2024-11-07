package branchandbound;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Generic Branch and Bound solver
 * You solve a problem with Branch and Bound
 * by calling the method {@link #minimize(OpenNodes, Consumer)}}
 */
public class BranchAndBound {

    /**
     * Explore completely the search space
     * defined by the nodes initially
     * in the provided open-nodes.
     * using Branch and Bound
     * @param openNode a collection containing
     *                 initially the root node.
     *                 This collection during execution will contain
     *                 the fringe of the exploration.
     *                 The search might be explored in depth-first-search
     *                 or best-first-search depending on the specific implementation
     *                 for the openNode see {@link BestFirstOpenNodes} and {@link DepthFirstOpenNodes}.
     * @param onSolution a closure called each time a new improving
     *                   solution is found.
     * @param <T> is the type of the state/node for the problem to solve
     */
    public static <T> void minimize (OpenNodes<T> openNode, Consumer<Node<T>> onSolution) {
        double upperBound = Double.MAX_VALUE;
        int iter = 0;

        while (!openNode.isEmpty()) {
            iter++;
            Node<T> n = openNode.remove();
            if (n.isSolutionCandidate()) {
                double objective = n.objectiveFunction();
                if  (objective < upperBound) {
                    upperBound = objective;
                    onSolution.accept(n);
                }
            } else if (n.lowerBound() < upperBound) {
                for (Node<T> child : n.children()) {
                    openNode.add(child);
                }
            }
        }
        // System.out.println("#iter:" + iter);
    }



}

/**
 * Node interface to be used in Branch and Bound.
 * It assumes a minimization problem
 * (just take the opposite if you have a maximization problem)
 * 
 * @param <T>: the type of the "state" associated with the given node.
 */
interface Node<T> {

    /**
     * Return true if the node is a complete solution, that is
     * a leaf node that is a feasible solution to the problem
     * (but not necessarily optimal).
     * @return true if the node is a complete solution (not necessarily optimal)
     */
    boolean isSolutionCandidate();

    /**
     * The objective function of the node.
     * @return The objective function of the node.
     *         It only makes sense to call this function
     *         when the node is a solution candidate
     */
    double objectiveFunction();

    /**
     * A lower bound on the objective function.
     * This function can be called on any-node, not only
     * for a solution candidate.
     * @return A lower bound on the objective function
     */
    double lowerBound();

    /**
     * Depth of the node
     * @return the depth of the node
     */
    int depth();

    List<Node<T>> children();

    /** @return the state associated with this current node */
    T getState();
}

/**
 * ADT for representing the fringe (open-nodes)
 * in the implementation of a BnB
 * @param <T> the type of the state of the nodes
 */
interface OpenNodes<T> {
    /**
     * Add a node to the fringe
     * @param n the node
     */
    void add(Node<T> n);

    /**
     * Remove a node from the fringe
     * @return a node
     */
    Node<T> remove();

    /**
     * Verify if the fringe is empty
     * @return true if the fringe is empty
     */
    boolean isEmpty();

    /**
     * The number of nodes in the fringe
     * @return the number of nodes in the fringe
     */
    int size();
}

/**
 * Implementation of a fringe
 * to get best-first-search strategy
 * @param <T>
 */
class BestFirstOpenNodes<T> implements OpenNodes<T> {

    PriorityQueue<Node<T>> queue;

    BestFirstOpenNodes() {
        queue = new PriorityQueue<Node<T>>(new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> o1, Node<T> o2) {
                return Double.compare(o1.lowerBound(), o2.lowerBound());
            }
        });
    }

    public void add(Node<T> n) {
        queue.add(n);
    }

    public Node<T> remove() {
        return queue.remove();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }
}

/**
 * Implementation of a fringe
 * to get depth-first-search strategy
 * @param <T>
 */
class DepthFirstOpenNodes<T> implements OpenNodes<T> {

    Stack<Node<T>> stack;

    DepthFirstOpenNodes() {
        stack = new Stack<>();
    }

    public void add(Node<T> n) {
        stack.push(n);
    }

    public Node<T> remove() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public int size() {
        return stack.size();
    }
}

/**
 * Implementation of a fringe
 * to get depth-first-search strategy but among the ties
 * on the depth, selecting the node with best lower-bound
 * @param <T>
 */
class DepthFirstBestFirstOpenNodes<T> implements OpenNodes<T> {

    PriorityQueue<Node<T>> queue;

    DepthFirstBestFirstOpenNodes() {
        queue = new PriorityQueue<>(new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> o1, Node<T> o2) {
                if (o1.depth() != o2.depth()) {
                    return (o2.depth() - o1.depth());
                } else {
                    return Double.compare(o1.lowerBound(), o2.lowerBound());
                }
            }
        });
    }

    public void add(Node<T> n) {
        queue.add(n);
    }

    public Node<T> remove() {
        return queue.remove();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int size() {
        return queue.size();
    }
}






