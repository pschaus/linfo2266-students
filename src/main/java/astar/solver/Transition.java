package astar.solver;

/**
 * Class representing a transition in a A* like model
 */
public class Transition<S extends State> {

    final private S successor;
    final private int decision;
    final private double value;

    public Transition(S successor, int decision, double value) {
        this.successor = successor;
        this.decision = decision;
        this.value = value;
    }

    public S getSuccessor() {
        return successor;
    }

    public int getDecision() {
        return decision;
    }

    public double getValue() {
        return value;
    }
}