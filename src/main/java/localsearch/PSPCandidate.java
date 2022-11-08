package localsearch;

import util.NotImplementedException;
import util.psp.PSPInstance;

/**
 * This class represents a candidate solution for the PSP.
 * Two invariants are used to keep track of the objective function
 * when the variables are updated.
 */
public class PSPCandidate extends Candidate {

    PSPInstance instance;
    StockingCostInvariant stockingCost;
    ChangeoverCostInvariant changeoverCost;
    
    public PSPCandidate(PSPInstance instance) {
        // create a candidate solution with one variable per time period
        // the value of the variable is the index of the demand produced at that time
        // (index of the demand = index in the instance.demands array)
        super(IntVar.makeIntVarArray(instance.nPeriods, PSP.IDLE));
        this.instance = instance;
        this.stockingCost = new StockingCostInvariant(instance, this.variables);
        this.changeoverCost = new ChangeoverCostInvariant(instance, this.variables);
    }

    @Override
    int getValue() {
        // compute the stocking and changeover costs separately
        return this.stockingCost.getValue() + this.changeoverCost.getValue();
    }

    @Override
    Candidate copy() {
        PSPCandidate copy = new PSPCandidate(this.instance);
        for (int p = 0; p < this.instance.nPeriods; p++) {
            copy.variables[p].setValue(this.variables[p].getValue());
        }
        return copy;
    }
}
