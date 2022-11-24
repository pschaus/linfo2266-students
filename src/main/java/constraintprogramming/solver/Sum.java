package constraintprogramming.solver;

import util.NotImplementedException;

public class Sum extends Constraint{

    private final Variable[] x;
    private final Variable y;

    /**
     * Creates a sum constraint.
     * This constraint holds iff
     * {@code x[0]+x[1]+...+x[x.length-1] == y}.
     *
     * @param x the non empty left hand side of the sum
     * @param y the right hand side of the sum
     */
    public Sum(Variable[] x, Variable y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a sum constraint.
     * This constraint holds iff
     * {@code x[0]+x[1]+...+x[x.length-1] == y}.
     *
     * @param x the non empty left hand side of the sum
     * @param y the right hand side of the sum
     */
    public Sum(Variable[] x, int y) {
        this(x, x[0].getCsp().makeVariable(y+1));
        this.y.dom.fix(y);
    }


    @Override
    boolean propagate() {
        // TODO 1 update the value of y based on x
        // TODO 2 update the value of each x[i] based on y and the other x[i]'s
        throw new NotImplementedException("sum");
    }
}
