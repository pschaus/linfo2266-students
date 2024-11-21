package constraintprogramming.solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TinyCSP {

    public int nRecur = 0;

    /* constraints of the CSP */
    List<Constraint> constraints = new LinkedList<>();
    /* variables of the CSP */
    List<Variable> variables = new LinkedList<>();

    /**
     * Create a variable
     *
     * @param domSize the number of values in the domain
     * @return a variable with domain {0..domSize-1}
     */
    public Variable makeVariable(int domSize) {
        Variable x = new Variable(this, domSize);
        variables.add(x);
        return x;
    }

    public void add(Constraint c) {
        constraints.add(c);
        fixPoint();
    }

    public void notEqual(Variable x, Variable y) {
        notEqual(x, y, 0);
    }


    public void notEqual(Variable x, Variable y, int offset) {
        constraints.add(new NotEqual(x, y, offset));
        fixPoint();
    }
    
    public void lessOrEqual(Variable x, Variable y) {
        constraints.add(new LessOrEqual(x, y));
        fixPoint();
    }
    
    public void sum(Variable[] x, Variable y) {
        constraints.add(new Sum(x, y));
        fixPoint();
    }

    public void sum(Variable[] x, int y) {
        constraints.add(new Sum(x, y));
        fixPoint();
    }

    public void fixPoint() {
        boolean fix = false;
        while (!fix) {
            fix = true;
            for (Constraint c : constraints) {
                fix &= !c.propagate();
            }
        }
    }

    private ArrayList<Domain> backupDomains() {
        ArrayList<Domain> backup = new ArrayList<>();
        for (Variable x : variables) {
            backup.add(x.dom.clone());
        }
        return backup;
    }

    private void restoreDomains(ArrayList<Domain> backup) {
        for (int i = 0; i < variables.size(); i++) {
            variables.get(i).dom = backup.get(i);
        }
    }

    Optional<Variable> firstNotFixed() {
        return variables.stream().filter(x -> !x.dom.isFixed()).findFirst();
    }

    public void dfs(Runnable closure) {

        nRecur += 1;

        // pickup a variable that is not yet fixed if any
        Optional<Variable> notFixed = firstNotFixed();
        if (!notFixed.isPresent()) { // all variables fixed, a solution is found
            closure.run();
        } else {
            Variable y = notFixed.get(); // take the unfixed variable
            int v = y.dom.min();
            ArrayList<Domain> backup = backupDomains();

            // left branch x = v
            try {
                y.dom.fix(v);
                fixPoint();
                dfs(closure);
            } catch (Inconsistency i) {
            }

            restoreDomains(backup);
            backup = backupDomains();

            // right branch x != v
            try {
                y.dom.remove(v);
                fixPoint();
                dfs(closure);
            } catch (Inconsistency i) {
            }
            restoreDomains(backup);
        }
    }

    public static class Inconsistency extends RuntimeException {

    }

}
