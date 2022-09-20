package dynamicprogramming;

import java.util.Objects;

public class KnapsackState extends State {

    int item, capacity;

    public KnapsackState(int index, int capacity) {
        this.item = index;
        this.capacity = capacity;
    }

    @Override
    int hash() {
        return Objects.hash(item, capacity);
    }

    @Override
    boolean isEqual(State s) {
        if (s instanceof KnapsackState) {
            KnapsackState state = (KnapsackState) s;
            return item == state.item && capacity == state.capacity;
        }
        return false;
    }

}