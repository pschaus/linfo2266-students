package mdd.exercise;

import java.util.BitSet;
import java.util.Iterator;

/** 
 * This is the class where you will define the STATE part of
 * your model to solve the maximum decarbonation problem.
 * 
 * PS: 
 * Do not forget to give a correct implementation for the
 * equals() and hashCode() methods.
 */
public final class MaximumDecarbonationState {
    // TODO You can implement the state of your model in whichever way you like.


    @Override
    public boolean equals(final Object o) {
        if (o != null && (o instanceof MaximumDecarbonationState)) {
            final MaximumDecarbonationState other = (MaximumDecarbonationState) o;
            // TODO implement an equality test between two states of your model
             return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        // TODO provide a hashcode implementation that would be consistent
        // TODO with the equals method defined above
         return 0;
    }

}
