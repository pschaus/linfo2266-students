package localsearch;

import util.NotImplementedException;
import util.psp.PSPInstance;

/**
 * This class implements an invariant that computes the total changeover cost
 * of a given production schedule
 */
public class ChangeoverCostInvariant extends Invariant {

    PSPInstance instance;
    IntVar[] production; // production schedule of a candidate solution (see PSPCandidate.java)
    IntVar cost; // variable that contains the total changeover cost

    public ChangeoverCostInvariant(PSPInstance instance, IntVar[] production) {
        super(production);
        this.instance = instance;
        this.production = production;
        this.cost = new IntVar(0);
    }

    @Override
    public void update(IntVar x, int oldValue) {
        // TODO update the cost variable to the new total changeover cost
        //      given the new and old values of variable x
         throw new NotImplementedException("ChangeoverCostInvariant.update");
    }

    @Override
    public int getValue() {
        return this.cost.getValue();
    }

}
