package linearprogramming;

import util.NotImplementedException;
import util.NotImplementedExceptionAssume;
import java.lang.management.ManagementFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class Assertions {

    /**
     * Asserts that both the solution value and its objective retrieved from the {@link LinearProgramming} formulation
     * through the {@link FlowMatrices} are valid
     */
    static void assertCorrectness(FlowNetwork network) {
        try {
            FlowMatrices matrices = new FlowMatrices(network);
            // the network should be untouched at this point: the problem is not solved yet
            for (FlowEdge e: network.edges()) {
                assertEquals("No flow is supposed to be assigned to the network when you initialize a FlowMatrices instance",
                        e.flow(), 0., 0.001);
            }
            FlowNetwork copy = new FlowNetwork(network);
            LinearProgramming simplex = new LinearProgramming(matrices.A, matrices.b, matrices.c);
            double simplexSol = simplex.value();
            long fordFulkersonStart = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            double fordFulkersonSol = new FordFulkerson(copy, copy.source(), copy.sink()).value();
            long fordFulkersonElapsed = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - fordFulkersonStart;
            // first checks if the objective value is correct
            assertEquals("The linear formulation solution should be equal to the fordFulkerson solution",
                    fordFulkersonSol, simplexSol, 0.001);
            // checks if the whole flow makes sense
            double[] solution = simplex.primal();
            // gets the flow passing through the network
            long assignFlowStart = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
            FlowNetwork G = matrices.assignFlow(solution);
            long assignFlowElapsed = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() - assignFlowStart;
            if (network.V() > 200) {
                // for big instances, assigning the flow from a solution should be faster than calling FordFulkerson
                assertTrue(String.format("You take too much time to assign your flow to the network. %d vs %d", assignFlowElapsed, fordFulkersonElapsed),
                        assignFlowElapsed * 1.1 <= fordFulkersonElapsed);
            }
            // first check if each individual flow makes sense
            int V = G.V();
            int s = G.source();
            int t = G.sink();
            for (int v = 0 ; v < V ; ++v) {
                double inFlow = 0;
                double outFlow = 0;
                for (FlowEdge edge: G.adj(v)) {
                    double flow = edge.flow();
                    if (edge.from() == v) {
                        outFlow += flow;
                    } else {
                        inFlow += flow;
                    }
                    assertTrue("The flow passing through an edge exceeds its capacity",
                            flow <= edge.capacity());
                }
                if (v == s) {
                    // the flow passing from the source == solution objective
                    assertEquals("The source has an incoming flow but it should be 0",
                            inFlow, .0, .001);
                    assertEquals("The outgoing flow from the source differs from the solution",
                            outFlow, simplexSol, .001);
                } else if (v == t) {
                    // the flow passing to the sink == solution objective
                    assertEquals("The ingoing flow coming to the sink differs from the solution",
                            inFlow, simplexSol, .001);
                    assertEquals("The sink has an incoming flow but it should be 0",
                            outFlow, .0, .001);
                } else {
                    // flow in == flow out for all nodes
                    assertEquals("The ingoing flow passing through a node differs from its outgoing flow",
                            outFlow, inFlow, .001);
                }
            }
            // checks if the full network is valid
            FlowEdge[] edgeTo = new FlowEdge[G.V()]; // edgeTo[v] = last edge on shortest residual s->v path
            boolean[] marked = new boolean[G.V()];  // marked[v] = true iff s->v path in residual graph
            // breadth-first search
            Queue<Integer> queue = new LinkedList<>();
            queue.add(s);
            marked[s] = true;
            while (!queue.isEmpty() && !marked[t]) {
                int v = queue.remove();

                for (FlowEdge e : G.adj(v)) {
                    int w = e.other(v);

                    // if residual capacity from v to w
                    if (e.residualCapacityTo(w) > 0) {
                        if (!marked[w]) {
                            edgeTo[w] = e;
                            marked[w] = true;
                            queue.add(w);
                        }
                    }
                }
            }

            // check that s is on the source side of min cut and that t is not on source side
            assertTrue("The source is not written on the source side of min cut, this should not happen", marked[s]);
            assertFalse("The sink appears on the source side of min cut, this should not happen", marked[t]);

            // check that value of min cut = value of max flow
            double mincutValue = 0.0;
            for (int v = 0; v < G.V(); v++) {
                for (FlowEdge e : G.adj(v)) {
                    if ((v == e.from()) && marked[(e.from())] && !marked[(e.to())])
                        mincutValue += e.capacity();
                }
            }
            assertEquals("The solution that you computed is not optimal", mincutValue, simplexSol, .001);
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }

    /**
     * Asserts that both the solution value and its objective retrieved from the {@link LinearProgramming} formulation
     * through the {@link MatchingMatrices} are valid
     */
    static void assertCorrectness(Graph graph) {
        try {
            MatchingMatrices matching = new MatchingMatrices(graph);
            LinearProgramming simplex = new LinearProgramming(matching.A, matching.b, matching.c);
            double simplexSol = simplex.value();
            double hopcroftKarpSol = new HopcroftKarp(graph).size();
            assertEquals("The objective value is not optimal",
                    hopcroftKarpSol, simplexSol, 0.001);
            double[] solution = simplex.primal();
            HashSet<Integer> selectedNode = new HashSet<>();
            for (Edge e: graph.uniqueEdges()) {
                if (matching.isEdgeSelected(solution, e)) {
                    assertFalse(String.format("Vertex %d has been selected twice (at least two adjacent edges have been selected)", e.v()),
                            selectedNode.contains(e.v()));
                    assertFalse(String.format("Vertex %d has been selected twice (at least two adjacent edges have been selected)", e.w()),
                            selectedNode.contains(e.w()));
                    selectedNode.add(e.v());
                    selectedNode.add(e.w());
                }
            }
            assertEquals(selectedNode.size() / 2., simplexSol, 0.001);
        } catch (NotImplementedException e) {
            NotImplementedExceptionAssume.fail(e);
        }
    }
}
