package jaicore.search.structure.graphgenerator.enumerate;

import jaicore.graphvisualizer.enumerate.EnumeratedNode;
import jaicore.graphvisualizer.enumerate.ListEnumerator;
import jaicore.graphvisualizer.gui.VisualizationWindow;
import jaicore.search.algorithms.standard.bestfirst.BestFirst;
import jaicore.search.algorithms.standard.bestfirst.BestFirstFactory;
import jaicore.search.algorithms.standard.bestfirst.nodeevaluation.RandomCompletionBasedNodeEvaluator;
import jaicore.search.model.probleminputs.GeneralEvaluatedTraversalTree;
import jaicore.search.testproblems.knapsack.KnapsackProblem;
import jaicore.search.testproblems.nqueens.QueenNode;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LandscapeVisualizationTest {

    @Test
    public void setUp() {
        int problemSize = 200;
        int seed = 20;
        int timeout = 100;
        KnapsackProblem knapsack = KnapsackProblem.createRandomProblem(problemSize, seed);
        BestFirst<
                GeneralEvaluatedTraversalTree<
                        EnumeratedNode<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList>,
                        String,
                        Double>,
                EnumeratedNode<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList>,
                String,
                Double> algorithm = null;

        EnumeratedGraphGenerator<KnapsackProblem.KnapsackNode, String, ListEnumerator.EnumerationList> egg = new EnumeratedGraphGenerator<>(knapsack.getGraphGenerator(), new ListEnumerator());
        EnumeratedSolutionEvaluator<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList, Double> ese = new EnumeratedSolutionEvaluator<>(knapsack.getSolutionEvaluator());
        RandomCompletionBasedNodeEvaluator<
                EnumeratedNode<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList>,
                Double> nodeEvaluator = new RandomCompletionBasedNodeEvaluator<>(
                new Random(seed), 3, ese);
        nodeEvaluator.setGenerator(egg);

        BestFirstFactory
                <GeneralEvaluatedTraversalTree
                        <EnumeratedNode<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList>,
                                String,
                                Double>,
                        EnumeratedNode<KnapsackProblem.KnapsackNode, ListEnumerator.EnumerationList>,
                        String,
                        Double> bestFirstFactory = new BestFirstFactory<>();
        bestFirstFactory.setProblemInput(
                new GeneralEvaluatedTraversalTree<>(egg, nodeEvaluator));
        bestFirstFactory.setTimeoutForFComputation(5000, n -> Double.MAX_VALUE);
        algorithm = bestFirstFactory.getAlgorithm();

        VisualizationWindow window = new VisualizationWindow<QueenNode, String>(algorithm);

        EnumeratedEvaluationsSupplier<KnapsackProblem.KnapsackNode, String, Double, String, ListEnumerator.EnumerationList> ees = new EnumeratedEvaluationsSupplier();
        algorithm.registerListener(ees);
        window.addDataSupplier(ees);


        algorithm.setTimeout(timeout * 1000, TimeUnit.MILLISECONDS);
        try {
            algorithm.call();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("algorithm finished with timeout exception, which is ok.");
        }
    }

}
