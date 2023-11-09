package localsearch;

/**
 * Abstract class that represents a candidate solution to a problem
 * and the variables that constitute it
 */
public abstract class Candidate {

    IntVar[] variables;

    public Candidate(IntVar[] variables) {
        this.variables = variables;
    }
    
    /**
     * Computes the delta (the difference of objective value)
     * obtained by performing the swap on the candidate solution
     * @param swap a swap move
     * @return the delta obtained by performing the swap on the given candidate solution
     */
    public int getDelta(Swap swap) {
        int oldValue = this.getValue();
        
        int tmp = this.variables[swap.variableIds[0]].getValue();
        for (int i = 0; i < swap.variableIds.length - 1; i++) {
            this.variables[swap.variableIds[i]].setValue(this.variables[swap.variableIds[i + 1]].getValue());
        }
        this.variables[swap.variableIds[swap.variableIds.length - 1]].setValue(tmp);

        int delta = this.getValue() - oldValue;
        
        tmp = this.variables[swap.variableIds[swap.variableIds.length - 1]].getValue();
        for (int i = swap.variableIds.length - 1; i > 0; i--) {
            this.variables[swap.variableIds[i]].setValue(this.variables[swap.variableIds[i - 1]].getValue());
        }
        this.variables[swap.variableIds[0]].setValue(tmp);

        return delta;
    }

    /**
     * Transforms the candidate solution by applying a given swap move to it
     * If a swap contains variables: [x1, x5, x7, x9]
     * Then applying the swap changes the values of the variables such that:
     * x1.newValue = x5.oldValue, x5.newValue = x7.oldValue,
     * x7.newValue = x9.oldValue and x9.newValue = x1.oldValue
     * @param swap a swap move
     */
    public void apply(Swap swap) {
        int tmp = this.variables[swap.variableIds[0]].getValue();
        for (int i = 0; i < swap.variableIds.length - 1; i++) {
            this.variables[swap.variableIds[i]].setValue(this.variables[swap.variableIds[i + 1]].getValue());
        }
        this.variables[swap.variableIds[swap.variableIds.length - 1]].setValue(tmp);
    }
    
    /**
     * Returns the objective value of the candidate solution
     * @return the objective value of the candidate solution
     */
    abstract int getValue();
    
    /**
     * Returns a deep copy of the candidate
     * @return a deep copy of the candidate
     */
    abstract Candidate copy();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (IntVar variable : this.variables) {
            builder.append(variable.getValue()).append(' ');
        }
        return builder.toString();
    }

}
