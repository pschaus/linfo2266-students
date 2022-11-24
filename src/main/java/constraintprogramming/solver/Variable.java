package constraintprogramming.solver;

/**
 * Implementation of the variable
 */
public class Variable {

    public Domain dom;
    private final TinyCSP csp;


    /**
     * Creates a variable with some values
     * @param n a positive number such that {0..n-1} is the domain variable
     */
    public Variable(TinyCSP csp, int n) {
        this.csp = csp;
        dom = new Domain(n);
    }

    public TinyCSP getCsp() {
        return csp;
    }

    @Override
    public String toString() {
        return dom.toString();
    }
}
