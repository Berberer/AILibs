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

    /* Contains all nodes in sorted order. */
    private final List<EnumeratedNode<T,V,I>> nodes = new ArrayList<>();

    /**
     *
     * @param event
     */
    @Subscribe
    public void receiveEvaluatedSolutionCandidateFoundEvent(
            EvaluatedSearchSolutionCandidateFoundEvent<T, A, V> event
    ) {

        // No access to the node itself. Only to the inner node object `T node`).
        throw new NotImplementedException();
    }

    public List<EnumeratedNode<T, V, I>> getNodes() {
        return nodes;
    }

    public List<V> getEvaluations() {
        // map the nodes to its f values.
        throw new NotImplementedException();
    }
}
