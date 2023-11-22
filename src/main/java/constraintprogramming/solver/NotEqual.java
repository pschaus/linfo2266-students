package constraintprogramming.solver;

/**
 * Not Equal constraint between two variables
 */
public class NotEqual extends Constraint {

    Variable x, y;
    int offset;

    /**
     * Creates a constraint such
     * that {@code x != y + v}
     *
     * @param x the left member
     * @param y the right memer
     * @param offset the offset value on y
     * @see TinyCSP#notEqual(Variable, Variable, int)
     */
    public NotEqual(Variable x, Variable y, int offset) {
        this.x = x;
        this.y = y;
        this.offset = offset;
    }

    /**
     * Creates a constraint such
     * that {@code x != y}
     * @param x the left member
     * @param y the right member
     * @see TinyCSP#notEqual(Variable, Variable)
     */
    public NotEqual(Variable x, Variable y) {
        this(x, y, 0);
    }

    @Override
    boolean propagate() {
        if (x.dom.isFixed()) {
            return y.dom.remove(x.dom.min() - offset);
        }
        if (y.dom.isFixed()) {
            return x.dom.remove(y.dom.min() + offset);
        }
        return false;
    }

}
