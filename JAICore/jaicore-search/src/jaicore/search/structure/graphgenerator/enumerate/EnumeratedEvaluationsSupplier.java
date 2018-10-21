package jaicore.search.structure.graphgenerator.enumerate;

import com.google.common.eventbus.Subscribe;
import jaicore.graph.IGraphAlgorithmListener;
import jaicore.search.algorithms.standard.bestfirst.events.EvaluatedSearchSolutionCandidateFoundEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T> Type of the inner node object.
 * @param <A> Type of the node edge.
 * @param <V> Type of the node evaluation.
 * @param <E>
 * @param <I> Type of the enumeration index.
 */
public class EnumeratedEvaluationsSupplier
        <T, A, V extends Comparable<V>, E, I extends Comparable<I>>
        implements IGraphAlgorithmListener<V, E> {

    /* Contains all f values in sorted order. */
    private final List<EnumeratedEvaluation<V,I>> enumeratedEvaluations = new ArrayList<>();

    /**
     *
     * @param event
     */
    @Subscribe
    public void receiveEvaluatedSolutionCandidateFoundEvent(
            EvaluatedSearchSolutionCandidateFoundEvent<EnumeratedNode<T, I>, A, V> event
    ) {
        V score = event.getSolutionCandidate().getScore();
        List<EnumeratedNode<T,I>> nodes = event.getSolutionCandidate().getNodes();
        I index = nodes.get(nodes.size()-1).getIndex();
        enumeratedEvaluations.add(new EnumeratedEvaluation<>(score, index));
    }

    public List<EnumeratedEvaluation<V,I>> getEvaluations() {
        return enumeratedEvaluations;
    }
}
