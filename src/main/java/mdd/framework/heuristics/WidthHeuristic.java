package mdd.framework.heuristics;

/**
 * This heuristic is used to determine the maximum width of a layer
 * in a MDD which is compiled using a given state as root.
 */
public interface WidthHeuristic<T> {
    int maximumWidth(final T state);
}
