package mdd.exercise;

import java.util.BitSet;
import java.util.Iterator;

import mdd.framework.core.Decision;
import mdd.framework.core.Relaxation;
import util.decarbonation.MaximumDecarbonationInstance;

/**
 * In this class you will implement a relaxation of the the maximum decarbonation
 * problem model that you have defined.
 */
public final class MaximumDecarbonationRelaxation implements Relaxation<MaximumDecarbonationState> {
    /** The instnance of the problem we want to solve */
    private final MaximumDecarbonationInstance instance;

    // TODO you can add fields to this class if deemed useful.
    
    /** 
     * Creates the DP model based on a given instance
     * @param instance the actual problem instance which is going to be solved
     */
    public MaximumDecarbonationRelaxation(final MaximumDecarbonationInstance instance) {
        this.instance = instance;
    }

    @Override
    public MaximumDecarbonationState mergeStates(final Iterator<MaximumDecarbonationState> states) {
        // TODO Fill in the details of your DP relaxation
         return null;
    }

    @Override
    public int relaxEdge(final MaximumDecarbonationState from, final MaximumDecarbonationState to, final MaximumDecarbonationState merged, final Decision d, final int cost) {
        // TODO Fill in the details of your DP relaxation
         return 0;
    }
    
}
