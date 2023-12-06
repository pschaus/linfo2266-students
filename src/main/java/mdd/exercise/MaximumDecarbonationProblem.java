package mdd.exercise;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import mdd.framework.core.Decision;
import mdd.framework.core.Problem;
import util.decarbonation.MaximumDecarbonationInstance;

/**
 * In this class you will model the maximum decarbonation problem that is
 * described in your assignment.
 */
public final class MaximumDecarbonationProblem implements Problem<MaximumDecarbonationState> {
    /** The instance of the problem we want to solve */
    private final MaximumDecarbonationInstance instance;

    // TODO you can add fields to this class if deemed useful.

    /** 
     * Creates the DP model based on a given instance
     * @param instance the actual problem instance which is going to be solved
     */
    public MaximumDecarbonationProblem(final MaximumDecarbonationInstance instance) {
        this.instance = instance;
    }

    @Override
    public int nbVars() {
        // TODO Fill in the details of your DP model
         return 0;
    }

    @Override
    public MaximumDecarbonationState initialState() {
        // TODO Fill in the details of your DP model
         return null;
    }

    @Override
    public int initialValue() {
        // TODO Fill in the details of your DP model
         return 0;
    }

    @Override
    public Iterator<Integer> domain(final MaximumDecarbonationState state, final int var) {
        // TODO Fill in the details of your DP model
         return null;
    }

    @Override
    public MaximumDecarbonationState transition(final MaximumDecarbonationState state, final Decision decision) {
        // TODO Fill in the details of your DP model
         return null;
    }

    @Override
    public int transitionCost(final MaximumDecarbonationState state, final Decision decision) {
        // TODO Fill in the details of your DP model
         return 0;
    }
    
}
