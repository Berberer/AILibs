package jaicore.search.algorithms.standard.bestfirst;

import jaicore.search.core.interfaces.ISolutionEvaluator;
import jaicore.search.algorithms.parallel.parallelexploration.distributed.interfaces.SerializableGraphGenerator;
import jaicore.search.testproblems.knapsack.KnapsackProblem;
import jaicore.search.model.travesaltree.Node;
import jaicore.search.structure.graphgenerator.SingleRootGenerator;
import jaicore.search.algorithms.standard.bestfirst.nodeevaluation.RandomCompletionBasedNodeEvaluator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class RandomCompletionBasedNodeEvaluatorTester {

    static SerializableGraphGenerator<KnapsackProblem.KnapsackNode, String> graphGenerator;
    static ISolutionEvaluator<KnapsackProblem.KnapsackNode, Double> solutionEvaluator;

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
        weights.put("0", 2.30d); weights.put("1", 3.10d); weights.put("2", 2.90d); weights.put("3", 4.40d);
        weights.put("4", 5.30d); weights.put("5", 3.80d); weights.put("6", 6.30d); weights.put("7", 8.50d);
        weights.put("8", 8.90d); weights.put("9", 8.20d);
        values = new HashMap<>();
        values.put("0", 92.0d); values.put("1", 57.0d); values.put("2", 49.0d); values.put("3", 68.0d);
        values.put("4", 60.0d); values.put("5", 43.0d); values.put("6", 67.0d); values.put("7", 84.0d);
        values.put("8", 87.0d); values.put("9", 72.0d);
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
        solutionEvaluator = knapsackProblem.getSolutionEvaluator();
    }

//    @Test
//    public void testRCE() {
//        Random random = new Random(42);
//        KnapsackProblem.KnapsackNode root = ((SingleRootGenerator<KnapsackProblem.KnapsackNode>)graphGenerator.getRootGenerator()).getRoot();
//        Node<KnapsackProblem.KnapsackNode, Double> node = new Node<>(null, root);
//        RandomCompletionBasedNodeEvaluator<KnapsackProblem.KnapsackNode, Double> randomCompletionEvaluator = new RandomCompletionBasedNodeEvaluator<>(random, 5, (a,b) -> null, solutionEvaluator);
//        randomCompletionEvaluator.setGenerator(graphGenerator);
//
//        RandomCompletionBasedNodeEvaluator<KnapsackProblem.KnapsackNode, Double> randomCompletionEvaluator2 = new RandomCompletionBasedNodeEvaluator<>(new Random(2), 5, (a,b) -> null, solutionEvaluator);
//        randomCompletionEvaluator2.setGenerator(graphGenerator);
//
//
//        try {
//            Double f1 = randomCompletionEvaluator.f(node);
//            Double f2 = randomCompletionEvaluator2.f(node);
//            System.out.println(f1); // f computes different Random completions
//            System.out.println(f2);
//
//        } catch (Throwable e) {
//            e.printStackTrace(System.err);
//        }
//
//    }

}
