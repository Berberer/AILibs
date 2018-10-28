package jaicore.search.structure.graphgenerator.enumerate;

import jaicore.search.core.interfaces.ISolutionEvaluator;

import java.util.List;
import java.util.stream.Collectors;

public class EnumeratedSolutionEvaluator<N, I extends Comparable<I>, V extends Comparable<V>> implements ISolutionEvaluator<EnumeratedNode<N, I>, V> {

    ISolutionEvaluator<N,V> solutionEvaluator;

    EnumeratedSolutionEvaluator(ISolutionEvaluator<N,V> solutionEvaluator) {
        this.solutionEvaluator = solutionEvaluator;
    }

    @Override
    public V evaluateSolution(List<EnumeratedNode<N, I>> solutionPath) throws Exception {
        return solutionEvaluator.evaluateSolution(solutionPath
                .stream()
                .map(enumeratedNode -> enumeratedNode.getPoint())
                .collect(Collectors.toList()));
    }

    @Override
    public boolean doesLastActionAffectScoreOfAnySubsequentSolution(List<EnumeratedNode<N, I>> partialSolutionPath) {
        return solutionEvaluator.doesLastActionAffectScoreOfAnySubsequentSolution(partialSolutionPath
                .stream()
                .map(enumeratedNode -> enumeratedNode.getPoint())
                .collect(Collectors.toList()));
    }

    @Override
    public void cancel() {
        solutionEvaluator.cancel();
    }
}
