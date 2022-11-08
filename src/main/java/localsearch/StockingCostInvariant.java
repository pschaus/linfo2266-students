package localsearch;

import util.NotImplementedException;
import util.psp.PSPInstance;
import util.psp.PSPInstance.Demand;

/**
 * This class implements an invariant that computes the total stocking cost
 * of a given production schedule
 */
public class StockingCostInvariant extends Invariant {

    PSPInstance instance;
    IntVar[] production; // production schedule of a candidate solution (see PSPCandidate.java)
    IntVar cost; // variable that contains the total stocking cost

    public StockingCostInvariant(PSPInstance instance, IntVar[] production) {
        super(production);
        this.instance = instance;
        this.production = production;
        this.cost = new IntVar(0);
    }

    @Override
    public void update(IntVar x, int oldValue) {
        // TODO update the cost variable to the new total stocking cost
        //      given the new and old values of variable x
         throw new NotImplementedException("StockingCostInvariant.update");
    }

    @Override
    public int getValue() {
        return this.cost.getValue();
    }

}
