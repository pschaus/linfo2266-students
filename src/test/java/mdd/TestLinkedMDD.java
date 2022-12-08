package mdd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import mdd.framework.core.CompilationInput;
import mdd.framework.core.CompilationType;
import mdd.framework.core.Decision;
import mdd.framework.core.Problem;
import mdd.framework.core.Relaxation;
import mdd.framework.core.SubProblem;
import mdd.framework.heuristics.StateRanking;
import mdd.framework.heuristics.VariableHeuristic;
import mdd.framework.implem.heuristics.DefaultVariableHeuristic;
import mdd.framework.implem.mdd.LinkedDecisionDiagram;

/**
 * ============================================================================================
 * == This class requires no action from your sides. All the tests that have been written    ==
 * == here should pass already. Still, you might want to take inspiration from it when       ==
 * == defining your own test suites to validate your own code.                               ==
 * ============================================================================================
 *
 * This is an example test suite that was written to validate the implementation of the MDD
 * interface by the LinkedMDD class.
 */
public final class TestLinkedMDD {

    @Test
    public void byDefaultTheDDIsExact() {
        LinkedDecisionDiagram<Integer> dd = new LinkedDecisionDiagram<>();
        assertTrue(dd.isExact());
    }

    @Test
    public void itRemembersThePartialAssignmentFromTheOriginalSubproblemWhenCompilingExact() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 42, Integer.MAX_VALUE, Collections.singleton(new Decision(0, 42))),
                3,
                Integer.MIN_VALUE
        );

        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().get().contains(new Decision(0, 42)));
    }
    @Test
    public void itRemembersThePartialAssignmentFromTheOriginalSubproblemWhenCompilingRelaxed() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 42, Integer.MAX_VALUE,
                        Collections.singleton(new Decision(0, 42))),
                3,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().get().contains(new Decision(0, 42)));
    }
    @Test
    public void itRemembersThePartialAssignmentFromTheOriginalSubproblemWhenCompilingRestricted() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Restricted,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 42, Integer.MAX_VALUE, Collections.singleton(new Decision(0, 42))),
                3,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().get().contains(new Decision(0, 42)));
    }

    // In an exact setup, the dummy problem would be 3*3*3 = 9 large at the bottom level
    @Test
    public void exactCompletelyUnrollsTheMddNoMatterItsWsidth() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().isPresent());
        assertTrue(dd.bestValue().isPresent());
        assertEquals(dd.bestValue().get(), Integer.valueOf(6));
        assertEquals(dd.bestSolution().get(),
                Arrays.stream(new Decision[]{
                        new Decision(2, 2),
                        new Decision(1, 2),
                        new Decision(0, 2)}
                ).collect(Collectors.toSet()));
    }
    @Test
    public void restrictedDropsTheLessInterestingNodes() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Restricted,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().isPresent());
        assertTrue(dd.bestValue().isPresent());
        assertEquals(dd.bestValue().get(), Integer.valueOf(6));
        assertEquals(dd.bestSolution().get(),
                Arrays.stream(new Decision[]{
                        new Decision(2, 2),
                        new Decision(1, 2),
                        new Decision(0, 2)}
                ).collect(Collectors.toSet()));
    }
    @Test
    public void relaxMergesTheLessInterestingNodes() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().isPresent());
        assertTrue(dd.bestValue().isPresent());
        assertEquals(dd.bestValue().get(), Integer.valueOf(24));
        assertEquals(dd.bestSolution().get(),
                Arrays.stream(new Decision[]{
                        new Decision(2, 2),
                        new Decision(1, 2),
                        new Decision(0, 2)}
                ).collect(Collectors.toSet()));
    }
    @Test
    public void relaxPopulatesTheCutsetAndWillNotSquashFirstLayer() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.bestSolution().isPresent());
        assertTrue(dd.bestValue().isPresent());
        assertEquals(dd.bestValue().get(), Integer.valueOf(24));

        HashSet<SubProblem<DummyState>> cutset = new HashSet<>();
        Iterator<SubProblem<DummyState>> csit = dd.exactCutset();
        while (csit.hasNext()) {
            cutset.add(csit.next());
        }
        assertEquals(3, cutset.size()); // L1 was not squashed even though it was 3 wide
    }

    @Test
    public void anExactMddMustBeExact() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
    }
    @Test
    public void aRelaxedMddIsExactAsLongAsNoMergeOccurs() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                10,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
    }
    @Test
    public void aRelaxedMddIsNotExactWhenAMergeHasOccured() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertFalse(dd.isExact());
    }
    @Test
    public void aRestrictedMddIsExactAsLongAsNoRestrictionOccurs() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Restricted,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                10,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
    }
    @Test
    public void aRestrictedMddIsNotExactWhenARestrictionHasOccured() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Restricted,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertFalse(dd.isExact());
    }
    @Test
    public void whenTheProblemIsInfeasibleThereIsNoSolution() {
        DummyInfeasibleProblem problem = new DummyInfeasibleProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
        assertFalse(dd.bestSolution().isPresent());
    }
    @Test
    public void whenTheProblemIsInfeasibleThereIsNoBestValue() {
        DummyInfeasibleProblem problem = new DummyInfeasibleProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                1,
                Integer.MIN_VALUE
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
        assertFalse(dd.bestValue().isPresent());
    }
    @Test
    public void exactSkipsNodesWithUbLessThanBestLb() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Exact,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, 100, Collections.emptySet()),
                5,
                1000
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
        assertFalse(dd.bestValue().isPresent());
    }
    @Test
    public void relaxedSkipsNodesWithUbLessThanBestLb() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, 100, Collections.emptySet()),
                1,
                1000
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
        assertFalse(dd.bestValue().isPresent());
    }
    @Test
    public void restrictedSkipsNodesWithUbLessThanBestLb() {
        DummyProblem problem = new DummyProblem();
        CompilationInput<DummyState> input = new CompilationInput<>(
                CompilationType.Restricted,
                problem,
                new DummyRelax(),
                new DefaultVariableHeuristic<>(),
                new DummyRanking(),
                new SubProblem<DummyState>(problem.initialState(), 0, 100, Collections.emptySet()),
                1,
                1000
        );
        LinkedDecisionDiagram<DummyState> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertTrue(dd.isExact());
        assertFalse(dd.bestValue().isPresent());
    }

    @Test
    public void relaxedComputesLocalBounds() {
        LocBoundsExamplePb problem = new LocBoundsExamplePb();
        LocBoundsExampleRelax relax = new LocBoundsExampleRelax();
        LocbBoundsExampleVarHeuristic varh = new LocbBoundsExampleVarHeuristic();
        LocbBoundsExampleRanking ranking = new LocbBoundsExampleRanking();

        CompilationInput<Character> input = new CompilationInput<>(
                CompilationType.Relaxed,
                problem,
                relax,
                varh,
                ranking,
                new SubProblem<Character>(problem.initialState(), 0, Integer.MAX_VALUE, Collections.emptySet()),
                3,
                Integer.MIN_VALUE
        );

        LinkedDecisionDiagram<Character> dd = new LinkedDecisionDiagram<>();
        dd.compile(input);
        assertFalse(dd.isExact());
        assertTrue(dd.bestValue().isPresent());
        assertEquals((Integer) 16, (Integer) dd.bestValue().get());

        HashMap<Character, Integer> v = new HashMap<>();
        Iterator<SubProblem<Character>> cutset = dd.exactCutset();
        while (cutset.hasNext()) {
            SubProblem<Character> sub = cutset.next();
            v.put(sub.getState(), sub.getUpperBound());
        }
        assertEquals((Integer) 16, v.get('a'));
        assertEquals((Integer) 14, v.get('b'));
    }

    private static class DummyState {
        final int value;
        final int depth;
        public DummyState(final int d, final int v){
            this.depth = d;
            this.value = v;
        }
        @Override
        public int hashCode() {
            return (depth * 47) + value;
        }
        @Override
        public boolean equals(final Object other) {
            if (other == null || !(other instanceof DummyState)) {
                return false;
            } else {
                DummyState that = (DummyState) other;
                return depth == that.depth && value == that.value;
            }
        }
    }
    private static class DummyProblem implements Problem<DummyState> {
        public int        nbVars()       { return 3; }
        public int        initialValue() { return 0; }
        public DummyState initialState() {
            return new DummyState(0, 0);
        }
        public DummyState transition(final DummyState state, final Decision d) {
            return new DummyState(state.value + d.val(), state.depth +1);
        }
        public int transitionCost(final DummyState state, final Decision d) {
            return  d.val();
        }
        public Iterator<Integer> domain(final DummyState state, final int var) {
            return Arrays.asList(0, 1, 2).iterator();
        }
    }
    private static class DummyInfeasibleProblem implements Problem<DummyState> {
        public int        nbVars()       { return 3; }
        public int        initialValue() { return 0; }
        public DummyState initialState() {
            return new DummyState(0, 0);
        }
        public DummyState transition(final DummyState state, final Decision d) {
            return new DummyState(state.value + d.val(), state.depth +1);
        }
        public int transitionCost(final DummyState state, final Decision d) {
            return  d.val();
        }
        public Iterator<Integer> domain(final DummyState state, final int var) {
            return new ArrayList<Integer>().iterator();
        }
    }
    private static class DummyRelax implements Relaxation<DummyState> {
        @Override
        public DummyState mergeStates(Iterator<DummyState> states) {
            DummyState ret = null;
            if (states.hasNext()) {
                DummyState s = states.next();
                ret = new DummyState(s.depth, 100);
            }
            return ret;
        }
        @Override
        public int relaxEdge(DummyState from, DummyState to, DummyState merged, Decision d, int cost) {
            return 20;
        }
        @Override
        public int fastUpperBound(final DummyState state, final Set<Integer> variables) {
            return 50;
        };
    }
    private static class DummyRanking implements StateRanking<DummyState> {
        @Override
        public int compare(DummyState o1, DummyState o2) {
            return o1.value - o2.value;
        }
    }

    /**
     * The example problem and relaxation for the local bounds should generate
     * the following relaxed MDD in which the layer 'a','b' is the LEL.
     *
     * ```plain
     *                      r
     *                   /     \
     *                10        7
     *               /           |
     *             a              b
     *             |     +--------+-------+
     *             |     |        |       |
     *             2     3        6       5
     *              \   /         |       |
     *                M           e       f
     *                |           |     /   \
     *                4           0   1      2
     *                |           |  /        \
     *                g            h           i
     *                |            |           |
     *                0            0           0
     *                +------------+-----------+
     *                             t
     * ```
     */
    private static class LocBoundsExamplePb implements Problem<Character> {
        public int       nbVars()       { return 3;  }
        public Character initialState() { return 'r'; }
        public int       initialValue() { return 0;  }
        @Override
        public Iterator<Integer> domain(Character state, int var){
            switch (state) {
                case 'r': return Arrays.asList(10, 7).iterator();
                case 'a': return Arrays.asList(2).iterator();
                case 'b': return Arrays.asList(3, 6, 5).iterator();
                case 'M': return Arrays.asList(4).iterator();
                case 'e': return Arrays.asList(0).iterator();
                case 'f': return Arrays.asList(1, 2).iterator();
                default : return Collections.emptyIterator();
            }
        }
        @Override
        public Character transition(Character state, Decision d) {
            switch (state) {
                case 'r':
                    switch (d.val()) {
                        case 10: return 'a';
                        case  7: return 'b';
                    }
                    break;
                case 'a': // with value 2 :: merged into M
                    return 'c';
                case 'b':
                    switch (d.val()) {
                        case 3: return 'd';
                        case 6: return 'e';
                        case 5: return 'f';
                    }
                    break;
                case 'M':
                    // with value 4
                    return 'g';
                case 'e':
                    // with value 0
                    return 'h';
                case 'f':
                    switch (d.val()) {
                        case 1: return 'h';
                        case 2: return 'i';
                    }
                default: return 't';
            }
            return null;
        }
        @Override
        public int transitionCost(Character c, Decision d) {
            return d.val();
        }
    }
    private static class LocBoundsExampleRelax implements Relaxation<Character> {
        @Override
        public Character mergeStates(Iterator<Character> states) {
            return 'M';
        }
        @Override
        public int relaxEdge(Character from, Character to, Character merged, Decision d, int cost) {
            return cost;
        }
    }
    private static class LocbBoundsExampleVarHeuristic implements VariableHeuristic<Character> {
        @Override
        public Integer nextVariable(Set<Integer> variables, Iterator<Character> states) {
            if (!states.hasNext()) {
                return null;
            } else {
                Character c = states.next();
                switch (c) {
                    case 'r': return 0;
                    case 'a': return 1;
                    case 'b': return 1;
                    // c d are merged into M
                    case 'c': return 2;
                    case 'd': return 2;
                    case 'M': return 2;
                    case 'e': return 2;
                    case 'f': return 2;
                    default:  return null;
                }
            }
        }
    }
    private static class LocbBoundsExampleRanking implements StateRanking<Character> {
        @Override
        public int compare(Character a, Character b) {
            return a.compareTo(b);
        }
    }
}