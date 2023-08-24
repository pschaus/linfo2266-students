package localsearch;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.params.provider.Arguments;
import util.psp.PSPInstance;
import util.psp.PSPInstance.Demand;

public class Utils {

    public static void assertFeasible(PSPInstance instance, Candidate candidate) {
        int cost = 0, lastType = PSP.IDLE;
        HashSet<Integer> satisfiedDemands = new HashSet<>();

        for (int p = 0; p < instance.nPeriods; p++) {
            int demandId = candidate.variables[p].getValue();
            if (demandId != PSP.IDLE) {
                if (!satisfiedDemands.add(demandId)) {
                    fail("A demand is satisfied more than once");
                }

                Demand demand = instance.demands[demandId];

                if (p > demand.deadline) {
                    fail("A deadline is violated");
                }

                cost += instance.stockingCost[demand.type] * (demand.deadline - p);
                if (lastType != PSP.IDLE) {
                    cost += instance.changeoverCost[lastType][demand.type];
                }

                lastType = demand.type;
            }
        }

        if (satisfiedDemands.size() != instance.demands.length) {
            fail("Not all demands are satisfied");
        }

        if (cost != candidate.getValue()) {
            fail("The value of the candidate is wrong");
        }
    }

    public static boolean isFeasible(PSPInstance instance, Candidate candidate) {
        int cost = 0, lastType = PSP.IDLE;
        HashSet<Integer> satisfiedDemands = new HashSet<>();

        for (int p = 0; p < instance.nPeriods; p++) {
            int demandId = candidate.variables[p].getValue();
            if (demandId != PSP.IDLE) {
                if (!satisfiedDemands.add(demandId)) {
                    return false;
                }

                Demand demand = instance.demands[demandId];

                if (p > demand.deadline) {
                    return false;
                }

                cost += instance.stockingCost[demand.type] * (demand.deadline - p);
                if (lastType != PSP.IDLE) {
                    cost += instance.changeoverCost[lastType][demand.type];
                }

                lastType = demand.type;
            }
        }

        if (satisfiedDemands.size() != instance.demands.length) {
            return false;
        }

        if (cost != candidate.getValue()) {
            return false;
        }

        return true;
    }

    public static List<Arguments> readPSPInstances(String folderPath) {
        File folder = new File(folderPath);
        String[] filenames = folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("psp");
            }
        });

        LinkedList<Arguments> instances = new LinkedList<>();
        for (String filename : filenames) {
            instances.add(arguments(named(filename, new PSPInstance(folderPath + "/" + filename))));
        } 

        return instances;
    }

}
