package mdd;

import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

import util.decarbonation.MaximumDecarbonationInstance;

/**
 * ============================================================================================
 * == This class requires no action from your sides. All the tests that have been written    ==
 * == here should pass already. Still, you might want to take inspiration from it when       ==
 * == defining your own test suites to validate your own code.                               ==
 * ============================================================================================
 *
 * This class provides some tests to validate that the parsing of a maximum decarbonation instance.
 */
public final class TestMaximumDecarbonationParsing {

    @Test
    public void itShouldReturnNullWhenNoInstanceIsDefined() {
        final String test = "c there is no instance in this file\n";
        Assert.assertNull(MaximumDecarbonationInstance.fromString(test));
    }

    @Test
    public void itMustIgnoreComments() {
        final String test = "c there is a simple instance in this file\n" +
            "p edge 2 2\n" +
            "e 1 2\n" +
            "e 1 2\n" ;
        Assert.assertNotNull(MaximumDecarbonationInstance.fromString(test));
    }
    @Test
    public void itMustProperlyParseEdges() {
        final String test = "c there is a simple instance in this file\n" +
            "p edge 3 2\n" +
            "e 1 2\n" +
            "e 2 3\n" ;
        final MaximumDecarbonationInstance instance = MaximumDecarbonationInstance.fromString(test);
        Assert.assertNotNull(instance);
        Assert.assertEquals(3, instance.nbSites());

        BitSet neighbors = new BitSet(3);
        neighbors.set(0);
        neighbors.set(1); // reflexive always a neighbor of yourself
        neighbors.set(2);
        Assert.assertEquals(neighbors, instance.neighbors(2));
    }
    
}
