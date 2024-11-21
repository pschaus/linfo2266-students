package constraintprogramming.solver;

import util.NotImplementedException;

import java.util.BitSet;
import java.util.Iterator;

/**
 * Implementation of a very basic domain
 * using the {@see java.util.BitSet} data structure
 * to store the values
 */
public class Domain implements Iterable<Integer> {

    private BitSet values;

    /**
     * Initializes a domain with {0, ... ,n-1}
     *
     * @param n
     */
    public Domain(int n) {
        values = new BitSet(n);
        values.set(0, n);
    }

    private Domain(BitSet dom) {
        this.values = dom;
    }

    /**
     * Verifies if only one value left
     *
     * @return true if only one value left
     */
    public boolean isFixed() {
        return size() == 1;
    }

    /**
     * Gets the domain size
     *
     * @return the domain size
     */
    public int size() {
        return values.cardinality();
    }

    /**
     * Gets the minimum of the domain
     *
     * @return the minimum of the domain
     */
    public int min() {
        return values.nextSetBit(0);
    }

    /**
     * Gets the maximum of the domain
     *
     * @return the maximum of the domain
     */
    public int max() {
        return values.previousSetBit(values.length()-1);
    }


    /**
     * Gets the value of the domain if only one exists
     * If the domain is not fixed, throw a {@link TinyCSP.Inconsistency}
     *
     * @return the minimum of the domain
     */
    public int value() {
        if (isFixed())
            return values.nextSetBit(0);
        throw new TinyCSP.Inconsistency();
    }

    /**
     * Removes value v
     *
     * @param v
     * @throws constraintprogramming.solver.TinyCSP.Inconsistency if the domain becomes empty
     * @return if the value was present before
     */
    public boolean remove(int v) {
        if (0 <= v && v < values.length()) {
            if (values.get(v)) {
                values.clear(v);
                if (size() == 0) throw new TinyCSP.Inconsistency();
                return true;
            }
        }
        return false;
    }

    /**
     * Removes every value less than the specified value from the domain.
     * Throws an error if nothing remains within the domain
     *
     * @param v the value such that all the values less than v are removed
     * @throws constraintprogramming.solver.TinyCSP.Inconsistency if the domain becomes empty
     * @return if the domain has changed
     */
    public boolean removeBelow(int v) {
        // TODO remove all values below v (v not included)
        throw new NotImplementedException("removeBelow");
    }

    /**
     * Removes every value greater than the specified value from the domain.
     * Throws an error if nothing remains within the domain
     *
     * @param v the value such that all the values greater than v are removed
     * @throws constraintprogramming.solver.TinyCSP.Inconsistency if the domain becomes empty
     * @return if the domain has changed
     */
    public boolean removeAbove(int v) {
        // TODO remove all values above v (v not included)
        throw new NotImplementedException("removeAbove");
    }

    /**
     * Fixes the domain to value v
     *
     * @param v a value that is in the domain
     * @throws constraintprogramming.solver.TinyCSP.Inconsistency if the domain does not contain the value
     * @return true if the domain has changed
     */
    public boolean fix(int v) {
        if (!values.get(v)) throw new TinyCSP.Inconsistency();
        boolean wasFixed = isFixed();
        values.clear();
        values.set(v);
        return !wasFixed;
    }

    @Override
    public Domain clone() {
        return new Domain((BitSet) values.clone());
    }

    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return values.stream().iterator();
    }
}
