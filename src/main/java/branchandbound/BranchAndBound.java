package branchandbound;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.function.Consumer;

public class BranchAndBound {

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
        System.out.println("#iter:" + iter);
    }



}

/**
 * Node interface to be used in Brancn and Bound.
 * It assumes a minimization problem (just take the opposite if you have a maximization problem)
 * 
 * @param <T>: the type of the "state" associated with the given node.
 */
interface Node<T> {

    /**
     * Return true if the node is a complete solution (not necessarily optimal)
     * @return true if the node is a complete solution (not necessarily optimal)
     */
    boolean isSolutionCandidate();

    /**
     * The objective function of the node
     * @return The objective function of the node. It only makes sense to call this function
     *         when the node is a solution candidate
     */
    double objectiveFunction();

    /**
     * A lower bound on the objective function
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

interface OpenNodes<T> {
    void add(Node<T> n);
    Node<T> remove();
    boolean isEmpty();
    int size();
}

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






