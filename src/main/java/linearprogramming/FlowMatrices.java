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
        throw new NotImplementedException("FlowMatrices");
        this.network = network;
        List<FlowEdge> edges = network.edges(); // Get all edges
        int m = edges.size(); // Number of variables (equal to number of edges)
        int s = network.source(); // Source node
        int t = network.sink(); // Sink node

        // Collect all nodes and filter out non-source and non-sink nodes
        Set<Integer> allNodes = new HashSet<>();
        for (FlowEdge e : edges) {
            allNodes.add(e.from());
            allNodes.add(e.to());
        }
        List<Integer> nonSourceSinkNodes = allNodes.stream()
                .filter(v -> v != s && v != t)
                .collect(Collectors.toList());
        int k = nonSourceSinkNodes.size(); // Number of non-source and non-sink nodes

        // Total number of constraints: capacity constraints (m) + flow conservation constraints (2 inequalities per non-source/sink node)
        int totalConstraints = m + 2 * k;

        // Initialize matrices and vectors
        A = new double[totalConstraints][m];
        b = new double[totalConstraints];
        c = new double[m];

        // 1. Fill objective function vector c (maximize net outflow from source)
        for (int i = 0; i < m; i++) {
            FlowEdge e = edges.get(i);
            if (e.from() == s) { // Edges flowing out from source, coefficient +1
                c[i] = 1.0;
            } else if (e.to() == s) { // Edges flowing into source, coefficient -1
                c[i] = -1.0;
            } else { // Other edges do not affect source's net outflow, coefficient 0
                c[i] = 0.0;
            }
        }

        // 2. Fill capacity constraints (first m rows: x_e ≤ capacity(e))
        for (int i = 0; i < m; i++) {
            Arrays.fill(A[i], 0.0); // Initialize row
            A[i][i] = 1.0; // Coefficient of current edge variable is 1
            b[i] = edges.get(i).capacity(); // Upper bound is edge's capacity
        }

        // 3. Fill flow conservation constraints (remaining 2*k rows)
        int constraintIdx = m; // Start from row m
        for (int v : nonSourceSinkNodes) {
            // 3.1 First row: sum(inflow) - sum(outflow) ≤ 0
            double[] row1 = new double[m];
            Arrays.fill(row1, 0.0);
            for (int j = 0; j < m; j++) {
                FlowEdge e = edges.get(j);
                if (e.to() == v) { // Edges flowing into node v, coefficient +1
                    row1[j] = 1.0;
                } else if (e.from() == v) { // Edges flowing out from node v, coefficient -1
                    row1[j] = -1.0;
                }
            }
            A[constraintIdx] = row1;
            b[constraintIdx] = 0.0;
            constraintIdx++;

            // 3.2 Second row: sum(outflow) - sum(inflow) ≤ 0 (equivalent to inflow = outflow when combined with first row)
            double[] row2 = new double[m];
            Arrays.fill(row2, 0.0);
            for (int j = 0; j < m; j++) {
                FlowEdge e = edges.get(j);
                if (e.to() == v) { // Edges flowing into node v, coefficient -1
                    row2[j] = -1.0;
                } else if (e.from() == v) { // Edges flowing out from node v, coefficient +1
                    row2[j] = 1.0;
                }
            }
            A[constraintIdx] = row2;
            b[constraintIdx] = 0.0;
            constraintIdx++;
        }
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
        List<FlowEdge> originalEdges = network.edges();
        FlowNetwork result = new FlowNetwork(network.source(), network.sink()); // Create new flow network
        for (int i = 0; i < originalEdges.size(); i++) {
            FlowEdge original = originalEdges.get(i);
            double flow = solution[i]; // Flow of the i-th edge is the i-th element of the solution
            // Create a new edge with assigned flow and add to the result network
            FlowEdge withFlow = new FlowEdge(original.from(), original.to(), original.capacity(), flow);
            result.addEdge(withFlow);
        }
        return result;
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
