package jaicore.search.structure.graphgenerator.enumerate;

import jaicore.search.core.interfaces.GraphGenerator;
import jaicore.search.model.travesaltree.NodeExpansionDescription;
import jaicore.search.structure.graphgenerator.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnumeratedGraphGenerator<N, I extends Comparable<I>, A> implements GraphGenerator<EnumeratedNode<N, I>, A> {

    private GraphGenerator<N, A> graphGenerator;
    private TreeEnumerator<I> treeEnumerator;

    EnumeratedGraphGenerator(GraphGenerator<N, A> graphGenerator, TreeEnumerator<I> treeEnumerator) {
        // Only NodeGoalTesters are valid.
        if (!(graphGenerator.getGoalTester() instanceof NodeGoalTester)) {
            throw new IllegalArgumentException("Only GraphGenerator with NodeGoalTester allowed.");
        }
        // Only SingleRootGenerators are valid.
        if (!(graphGenerator.getGoalTester() instanceof NodeGoalTester)) {
            throw new IllegalArgumentException("Only GraphGenerator with NodeGoalTester allowed.");
        }
        this.graphGenerator = graphGenerator;
        this.treeEnumerator = treeEnumerator;
    }

    @Override
    public RootGenerator<EnumeratedNode<N,I>> getRootGenerator() {
        if (graphGenerator.getRootGenerator() instanceof SingleRootGenerator) {
            return new SingleRootGenerator<EnumeratedNode<N, I>>() {
                @Override
                public EnumeratedNode<N, I> getRoot() {
                    N root = ((SingleRootGenerator<N>) graphGenerator.getRootGenerator()).getRoot();
                    return new EnumeratedNode<N,I>(root, treeEnumerator.forRoot(0));
                }
            };
        }
        if (graphGenerator.getRootGenerator() instanceof MultipleRootGenerator) {
            // Cant implement yet because MultipleRootGeneration return a collection (List needed)
            throw new NotImplementedException();
        }
        return null;
    }

    @Override
    public SuccessorGenerator<EnumeratedNode<N, I>, A> getSuccessorGenerator() {
        return (node)->{
            List<NodeExpansionDescription<N,A>> successors = graphGenerator.getSuccessorGenerator().generateSuccessors(node.getPoint());
            return IntStream
                    .range(0, successors.size())
                    .mapToObj(i->new NodeExpansionDescription<EnumeratedNode<N,I>, A>(
                            node,
                            new EnumeratedNode<>(successors.get(i).getTo(), treeEnumerator.forChildren(node, i)),
                            successors.get(i).getAction(),
                            successors.get(i).getTypeOfToNode()
                    ))
                    .collect(Collectors.toList());
        };
    }

    @Override
    public GoalTester<EnumeratedNode<N,I>> getGoalTester() {
        return new NodeGoalTester<EnumeratedNode<N, I>>() {
            @Override
            public boolean isGoal(EnumeratedNode<N, I> node) {
                NodeGoalTester<N> tester = (NodeGoalTester<N>)graphGenerator.getGoalTester();
                return tester.isGoal(node.getPoint());
            }
        };
    }

    @Override
    public boolean isSelfContained() {
        return graphGenerator.isSelfContained();
    }

    @Override
    public void setNodeNumbering(boolean nodenumbering) {
        graphGenerator.setNodeNumbering(nodenumbering);
    }
}
