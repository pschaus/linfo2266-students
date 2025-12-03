package astar.solver;

/**
 * Node used to store the states to explores in the A*-based searches
 * @param <S>
 */
class Node<S> implements Comparable<Node<S>>  {
    final S state;
    final double f;

    /**
     * Creates a new node
     * @param state the state associated to the node
     * @param f the score used to order the nodes
     */
    public Node(S state, double f) {
        this.state = state;
        this.f = f;
    }

    @Override
    public int compareTo(Node<S> o) {
        return Double.compare(f, o.f);
    }
}
