package astar.solver;

import util.Solution;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Solver {
    void solve(BiConsumer<Solution, SearchStatistics> onSolution);

}
