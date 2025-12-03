package astar.problems;

import java.util.*;

import astar.solver.Model;
import astar.solver.Transition;
import util.knapsack.KnapsackInstance;

public class Knapsack extends Model<KnapsackState> {

    KnapsackInstance instance;
    KnapsackState root;

    public Knapsack(KnapsackInstance instance) {
        this.instance = instance;
        this.root = new KnapsackState(0, instance.capacity);
    }

    @Override
    public boolean isTerminalState(KnapsackState state) {
        return state.item == instance.n || state.capacity == 0;
    }

    @Override
    public double getTerminalStateValue(KnapsackState state) {
        return 0;
    }

    @Override
    public List<Transition<KnapsackState>> getTransitions(KnapsackState state) {
        List<Transition<KnapsackState>> transitions = new LinkedList<>();

        // do not take the item
        transitions.add(new Transition<KnapsackState>(
                new KnapsackState(state.item + 1, state.capacity),
                0,
                0
        ));

        // take the item if remaining capacity allows
        if (instance.weight[state.item] <= state.capacity) {
            transitions.add(new Transition<KnapsackState>(
                    new KnapsackState(state.item + 1, state.capacity - instance.weight[state.item]),
                    1,
                    -instance.value[state.item]
            ));
        }

        return transitions;
    }

    @Override
    public double h(KnapsackState state) {
        double[] ratio = new double[instance.n];
        int capacity = state.capacity;
        for (int i = state.item; i < instance.n; i++) {
            ratio[i] = ((double) instance.value[i] / instance.weight[i]);
        }

        class RatioComparator implements Comparator<Integer> {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Double.compare(ratio[o1], ratio[o2]);
            }
        }

        Integer[] sortedVariables = new Integer[instance.n - state.item];
        for (int i = state.item; i < instance.n; i++) {
            sortedVariables[i - state.item] = i;
        }
        Arrays.sort(sortedVariables, new RatioComparator().reversed());

        int maxProfit = 0;
        Iterator<Integer> itemIterator = Arrays.stream(sortedVariables).iterator();
        while (capacity > 0 && itemIterator.hasNext()) {
            int item = itemIterator.next();
            if (capacity >= instance.weight[item]) {
                maxProfit += instance.value[item];
                capacity -= instance.weight[item];
            } else {
                double itemProfit = ratio[item] * capacity;
                maxProfit += (int) Math.floor(itemProfit);
                capacity = 0;
            }
        }

        return -maxProfit;
    }

    @Override
    public KnapsackState getRootState() {
        return root;
    }

}