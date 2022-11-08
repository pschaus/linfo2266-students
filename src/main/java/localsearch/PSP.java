package localsearch;

import util.NotImplementedException;
import util.psp.PSPInstance;
import util.psp.PSPInstance.Demand;

public class PSP implements Problem {

    public static final int IDLE = -1;

    PSPInstance instance;

    public PSP(PSPInstance instance) {
        this.instance = instance;
    }

    @Override
    public int getNumberOfVariables() {
        return this.instance.nPeriods;
    }

    @Override
    public Candidate getInitialCandidate() {
        // TODO create an initial candidate solution with a valid production schedule
        // hint: the array 'this.instance.demands' contains all Demand objects sorted
        //       by increasing deadline -> schedule all demands backwards to produce a
        //       feasible schedule
         throw new NotImplementedException("PSP.getInitialCandidate");
    }

    @Override
    public boolean isFeasible(Candidate candidate, Swap swap) {
        // TODO check whether applying the given swap to the candidate results
        //      in a feasible candidate (i.e. no deadline are violated)
         throw new NotImplementedException("PSP.isFeasible");
    }

    public static void main(String[] args) {
        PSPInstance instance = new PSPInstance("data/PSP/small/psp_50_5_90_1_0");
        LocalSearch search = new LocalSearch(new PSP(instance));
        search.solve(60);
    }
    
}
