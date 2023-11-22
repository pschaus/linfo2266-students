package constraintprogramming.solver;

import util.NotImplementedException;

public class LessOrEqual extends Constraint {

    Variable x, y;

    /**
     * Creates a constraint such
     * that {@code x <= y}
     *
     * @param x the left member
     * @param y the right memer
     * @see TinyCSP#notEqual(Variable, Variable)
     */
    public LessOrEqual(Variable x, Variable y) {
        this.x = x;
        this.y = y;
    }

    @Override
    boolean propagate() {
        // TODO implement the propagation for x <= y
        //  you should only update the minimum and maximum values of the variables
        //  there is no need to call remove() somewhere in the middle of the domain
        throw new NotImplementedException("lessOrEqual");
    }

}