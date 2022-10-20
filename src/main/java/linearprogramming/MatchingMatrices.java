package linearprogramming;

import util.NotImplementedException;

/**
 * Encodes a maximum cardinality matching problem
 * Given a bipartite undirected graph {@link Graph}, its set of edges {@link Edge} and vertices, the goal is to find
 * the biggest set of edges ({@link Graph#edges()} or {@link Graph#uniqueEdges()})
 * that can be selected without having common vertices
 *
 * This problem is very similar to the {@link FlowMatrices}. There are 2 main ways to solve it:
 *  - do it from scratch
 *  - transform the problem to a {@link FlowMatrices} one and benefit from your already implemented function(s)
 *
 * This is encoded as a linear program, by initializing the matrices A, b and c suitable for
 * solving the problem through a simplex solver {@link LinearProgramming}
 */
public class MatchingMatrices {

    // matrices of the problem: maximize cx s.t. Ax <= b
    final double[][] A;
    final double[] b;
    final double[] c;
    private final Graph graph; // instance that needs to be modelled

    /**
     * Creates the matrices A, b and c from a maximum cardinality matching graph
     * The graph is assumed to be bipartite
     * @param graph maximum cardinality matching that needs to be modeled as a linear program
     */
    public MatchingMatrices(Graph graph) {
        this.graph = graph;
        // TODO
        //  encode the problem into matrices A, b and c
        //  such that a LinearProgramming instance can be created from them
         throw new NotImplementedException("MatchingMatrices");
    }

    /**
     * Given the solution from the {@link LinearProgramming} created based on the problem,
     * tells if an edge has been selected in the matching
     *
     * @param solution optimal primal solution from the linear program
     *                 this is retrieved through a {@link LinearProgramming#primal()} call by using
     *                 the A, B and C matrices from the formulation
     * @param edge edge for which the selection must be told
     * @return true if the edge is selected in the solution
     */
    public boolean isEdgeSelected(double[] solution, Edge edge) {
         throw new NotImplementedException("MatchingMatrices");
    }

    public static void main(String[] args) {
        Edge[] edges = new Edge[] {
                new Edge(0, 4),
                new Edge(1, 5),
                new Edge(1, 6),
                new Edge(2, 7),
                new Edge(3, 7),
        };
        Graph instance = Graph.bipartite(8, edges);
        MatchingMatrices matrices = new MatchingMatrices(instance);  // transform the problem in LP form
        LinearProgramming simplex = new LinearProgramming(matrices.A, matrices.b, matrices.c);
        System.out.println("max matching = " + simplex.value());
        double[] solution = simplex.primal();
        System.out.println(edges[2] + " is" + (matrices.isEdgeSelected(solution, edges[2]) ? "" : " not") + " selected ");
    }

}
