package dynamicprogramming;

import java.util.LinkedList;
import java.util.List;

import util.knapsack.KnapsackInstance;

public class Knapsack extends Model<KnapsackState> {

    KnapsackInstance instance;
    KnapsackState root;

    public Knapsack(KnapsackInstance instance) {
        this.instance = instance;
        this.root = new KnapsackState(0, instance.capacity);
    }

    @Override
    boolean isBaseCase(KnapsackState state) {
        return state.item == instance.n || state.capacity == 0;
    }

    @Override
    double getBaseCaseValue(KnapsackState state) {
        return 0;
    }

    @Override
    List<Transition<KnapsackState>> getTransitions(KnapsackState state) {
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
                instance.value[state.item]
            ));
        }

        return transitions;
    }

    @Override
    KnapsackState getRootState() {
        return root;
    }

    @Override
    boolean isMaximization() {
        return true;
    }

}