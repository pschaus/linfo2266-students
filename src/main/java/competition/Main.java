package competition;

import java.lang.management.ManagementFactory;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        SetCoverInstance instance = new SetCoverInstance(args[0]);
        Solver solver = new GreedySolver(instance);

        long startTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        Set<Integer> solution = solver.solve();
        long endTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
        double solvingTime = (endTime - startTime)/1e9d;


        System.out.printf("Solution found in %.3f seconds\n", solvingTime);
        if (instance.isValid(solution, true)) {
            System.out.println("The solution is valid");
        }
        else {
            System.out.println("The solution is not valid");
        }

        System.out.println("Solution : " + solution);
        System.out.printf("Cost : %d\n", solution.size());

        // Uncomment to see the detail of the selected sets
        /* for (Integer set: solution)
            System.out.println(instance.getSet(set)); */

    }
}
