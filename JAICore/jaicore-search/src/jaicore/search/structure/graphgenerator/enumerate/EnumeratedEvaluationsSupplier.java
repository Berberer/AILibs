package jaicore.search.structure.graphgenerator.enumerate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import jaicore.graph.IGraphAlgorithmListener;
import jaicore.graphvisualizer.enumerate.EnumeratedNode;
import jaicore.graphvisualizer.events.controlEvents.ControlEvent;
import jaicore.graphvisualizer.events.graphEvents.GraphEvent;
import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import jaicore.graphvisualizer.gui.dataSupplier.ISupplier;
import jaicore.search.algorithms.standard.bestfirst.events.EvaluatedSearchSolutionCandidateFoundEvent;

/**
 *
 * @param <N> Type of the inner node object.
 * @param <A> Type of the node edge.
 * @param <V> Type of the node evaluation.
 * @param <E> Basically the same as A?
 * @param <I> Type of the enumeration index.
 */
public class EnumeratedEvaluationsSupplier
        <N, A, V extends Comparable<V>, E, I extends Comparable<I>>
        implements IGraphAlgorithmListener<V, E>, ISupplier {

    private EventBus eventBus = new EventBus();

    /* Contains all f values in sorted order. */
    private final List<EnumeratedEvaluationEvent<V,I>> enumeratedEvaluationEvents = new ArrayList<>();

    /**
     * Receives an `jaicore.search.algorithms.standard.bestfirst.events.EvaluatedSearchSolutionCandidateFoundEvent`
     * extracts the nodes index and f value and publishes it in an `EnumeratedEvaluationEvent` on the event bus..
     * @param event
     */
    @Subscribe
    public void receiveEvaluatedSolutionCandidateFoundEvent(
            EvaluatedSearchSolutionCandidateFoundEvent<EnumeratedNode<N, I>, A, V> event
    ) {
        assert event.getSolutionCandidate() != null;
        assert event.getSolutionCandidate().getScore() != null;
        assert event.getSolutionCandidate().getNodes() != null;

        V score = event.getSolutionCandidate().getScore();
        List<EnumeratedNode<N,I>> nodes = event.getSolutionCandidate().getNodes();
        I index = nodes.get(nodes.size()-1).getIndex();
        EnumeratedEvaluationEvent<V,I> enumeratedEvaluationEvent = new EnumeratedEvaluationEvent<>(score, index);
        // enumeratedEvaluationEvents.add(enumeratedEvaluationEvent);
        eventBus.post(enumeratedEvaluationEvent);
    }



    @Override
    public void registerListener(Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void receiveGraphEvent(GraphEvent event) {

    }

    @Override
    public void receiveControlEvent(ControlEvent event) {

    }

    @Override
    public JsonNode getSerialization() {
        return null;
    }

    public List<EnumeratedEvaluationEvent<V,I>> getEvaluations() {
        return enumeratedEvaluationEvents;
    }
}
