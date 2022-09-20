package util;

import java.util.List;

/**
 * Class representing the solution to an optimization problem
 */
public class Solution {

    private double value;
    private List<Integer> decisions;

    public Solution(double value, List<Integer> decisions) {
        this.value = value;
        this.decisions = decisions;
    }

    public double getValue() {
        return value;
    }

    public List<Integer> getDecisions() {
        return decisions;
    }

    public boolean isValid() {
        return true;
    }

}