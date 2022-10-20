package linearprogramming;

import util.NotImplementedException;


/**
 * Encodes a maximum fow problem
 * Given a flow network {@link FlowNetwork}, the goal is to find the flow on all edges {@link FlowEdge} maximizing
 * the flow passing from the source {@link FlowNetwork#source()} until the sink {@link FlowNetwork#sink()}
 *
 * This is encoded as a linear program, by initializing the matrices A, b and c suitable for
 * solving the problem through a simplex solver {@link LinearProgramming}
 */
public class FlowMatrices {

    // matrices of the problem: maximize cx s.t. Ax <= b
    final double[][] A;
    final double[] b;
    final double[] c;
    private final FlowNetwork network; // instance that needs to be modelled

    /**
     * Creates the matrices A, b and c from a flow network
     * The network is assumed to have only one source {@link FlowNetwork#source()}
     * and one sink {@link FlowNetwork#sink()}
     *
     * @param network flow network that needs to be modeled as a linear program
     */
    public FlowMatrices(FlowNetwork network) {
        this.network = network;
        // TODO
        //  encode the problem into matrices A, b and c
        //  such that a LinearProgramming instance can be created from them
         throw new NotImplementedException("FlowMatrices");
        // A = new double[0][0];
        // c = new double[0];
        // b = new double[0];
    }

    /**
     * Assign the flow passing through the {@link FlowEdge} in the solution
     * You are supposed to assign the flow from the provided {@code solution} only
     *
     * @param solution optimal primal solution from the linear program
     *                 this is retrieved through a {@link LinearProgramming#primal()} call by using
     *                 the A, B and C matrices from the formulation
     * @return flow network where the flow across all edges is set according to the primal solution
     */
    public FlowNetwork assignFlow(double[] solution) {
         throw new NotImplementedException("FlowMatrices");
    }

    public static void main(String[] args) {
        FlowNetwork instance = FlowNetwork.fromFile("data/Flow/example.txt");
        FlowMatrices matrices = new FlowMatrices(instance); // transform the problem in LP form
        LinearProgramming simplex = new LinearProgramming(matrices.A, matrices.b, matrices.c);
        System.out.println("max flow = " + simplex.value());
        // assign the flow
        double[] solution = simplex.primal();
        FlowNetwork flowAssigned = matrices.assignFlow(solution);
        System.out.println(flowAssigned);
    }

}