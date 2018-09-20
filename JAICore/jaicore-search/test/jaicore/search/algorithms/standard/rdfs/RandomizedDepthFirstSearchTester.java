package jaicore.search.algorithms.standard.rdfs;

import jaicore.search.core.interfaces.ISolutionEvaluator;
import jaicore.search.algorithms.parallel.parallelexploration.distributed.interfaces.SerializableGraphGenerator;
import jaicore.search.algorithms.standard.rstar.RandomCompletionGammaGraphGenerator;
import jaicore.search.testproblems.knapsack.KnapsackProblem;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class RandomizedDepthFirstSearchTester {

    static SerializableGraphGenerator<KnapsackProblem.KnapsackNode, String> graphGenerator;

    @BeforeClass
    public static void setUp() {
        /**
         * Setup Knapsack.
         */
        Set<String> objects;
        Map<String, Double> weights;
        Map<String, Double> values;
        Map<Set<String>, Double> bonusPoints;

        objects = new HashSet<String>();
        for (int i = 0; i < 10; i++) {
            objects.add(String.valueOf(i));
        }
        weights = new HashMap<>();
        weights.put("0", 2.30d);
        weights.put("1", 3.10d);
        weights.put("2", 2.90d);
        weights.put("3", 4.40d);
        weights.put("4", 5.30d);
        weights.put("5", 3.80d);
        weights.put("6", 6.30d);
        weights.put("7", 8.50d);
        weights.put("8", 8.90d);
        weights.put("9", 8.20d);
        values = new HashMap<>();
        values.put("0", 92.0d);
        values.put("1", 57.0d);
        values.put("2", 49.0d);
        values.put("3", 68.0d);
        values.put("4", 60.0d);
        values.put("5", 43.0d);
        values.put("6", 67.0d);
        values.put("7", 84.0d);
        values.put("8", 87.0d);
        values.put("9", 72.0d);
        bonusPoints = new HashMap<>();
        Set<String> bonusCombination = new HashSet<>();
        bonusCombination.add("0");
        bonusCombination.add("2");
        bonusPoints.put(bonusCombination, 25.0d);
        KnapsackProblem knapsackProblem = new KnapsackProblem(objects, values, weights, bonusPoints, 50);


        /**
         * Setup RandomCompletionGammaGraphGenerator.
         */
        graphGenerator = knapsackProblem.getGraphGenerator();
    }

//    @Test
//    public void testRDFS() {
//        Random random = new Random(42);
//        Random random2 = new Random(43);
//        RandomizedDepthFirstSearch<KnapsackProblem.KnapsackNode, String> rdfs = new RandomizedDepthFirstSearch<KnapsackProblem.KnapsackNode, String>(graphGenerator, random);
//        RandomizedDepthFirstSearch<KnapsackProblem.KnapsackNode, String> rdfs2 = new RandomizedDepthFirstSearch<>(graphGenerator, random2);
//        try {
//            System.out.println(rdfs.nextSolution());
//            System.out.println(rdfs2.nextSolution());
//        } catch (InterruptedException e) {
//            e.printStackTrace(System.err);
//        }
//    }

}
