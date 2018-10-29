package jaicore.search.structure.graphgenerator.enumerate;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import jaicore.graphvisualizer.events.misc.EnumeratedEvaluationEvent;
import jaicore.search.algorithms.standard.bestfirst.events.EvaluatedSearchSolutionCandidateFoundEvent;
import jaicore.search.model.other.EvaluatedSearchGraphPath;
import jaicore.search.testproblems.nqueens.NQueenGenerator;
import jaicore.search.testproblems.nqueens.QueenNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumeratedEvaluationsSupplierTest {

    private EnumeratedEvaluationsSupplier<QueenNode, String, Double, String, ListEnumerator.EnumerationList> enumeratedEvaluationsSupplier;
    private EvaluatedSearchSolutionCandidateFoundEventEmitter<EnumeratedNode<QueenNode, ListEnumerator.EnumerationList>, String, Double> solutionEventEmitter;
    private EnumeratedEvaluationListener<QueenNode, Integer> enumeratedEvaluationListener;
    private ArrayList<EvaluatedSearchGraphPath<EnumeratedNode<QueenNode, ListEnumerator.EnumerationList>, String, Double>> evaluatedSearchGraphPaths;


    class EvaluatedSearchSolutionCandidateFoundEventEmitter<N, A, V extends Comparable<V>>  {

        EventBus eventBus = new EventBus();

        public void emit(EvaluatedSearchGraphPath<N,A,V> esgp) {
            eventBus.post(new EvaluatedSearchSolutionCandidateFoundEvent<N,A,V>(esgp));
        }

        public void registerListener(Object listener) {
            eventBus.register(listener);
        }
    }

    class EnumeratedEvaluationListener<V,I extends Comparable<I>> {

        private ArrayList<EnumeratedEvaluationEvent<V,I>> events = new ArrayList<>();

        @Subscribe
        public void receive(EnumeratedEvaluationEvent<V,I> e) {
            events.add(e);
        }

        public ArrayList<EnumeratedEvaluationEvent<V, I>> getEvents() {
            return events;
        }
    }

    @Before
    public void setUp() {
        solutionEventEmitter = new EvaluatedSearchSolutionCandidateFoundEventEmitter<>();
        enumeratedEvaluationsSupplier = new EnumeratedEvaluationsSupplier<>();
        enumeratedEvaluationListener = new EnumeratedEvaluationListener<>();
        // Set up EvaluatedSearchSolutionCandidateFoundEvent's to publish in tests (using `QueenNodes` here).
        NQueenGenerator nQueenGenerator = new NQueenGenerator(5);
        evaluatedSearchGraphPaths = new ArrayList<>();
        for (int i=0; i<=5; i++) {
            EnumeratedNode<QueenNode, ListEnumerator.EnumerationList> enumeratedNode = new EnumeratedNode<>(
                    nQueenGenerator.getRootGenerator().getRoot(),
                    new ListEnumerator.EnumerationList(new ArrayList<>(Arrays.asList(1)))
            );
            List<EnumeratedNode<QueenNode, ListEnumerator.EnumerationList>> nodes = new ArrayList<>();
            nodes.add(enumeratedNode);
            List<String> actions = new ArrayList<>();
            EvaluatedSearchGraphPath<EnumeratedNode<QueenNode, ListEnumerator.EnumerationList>, String, Double> evaluatedSearchGraphPath = new EvaluatedSearchGraphPath<>(
                    nodes, actions,
                    (double) i
            );
            evaluatedSearchGraphPaths.add(evaluatedSearchGraphPath);
        }
        // Register listening.
        solutionEventEmitter.registerListener(enumeratedEvaluationsSupplier);
        enumeratedEvaluationsSupplier.registerListener(enumeratedEvaluationListener);
    }

    /**
     * Tests, if emitted `EvaluatedSearchGraphPath` (emitted by `EvaluatedSearchSolutionCandidateFoundEventEmitter`) are
     * received by the `EnumeratedEvaluationSupplier`, and corresponding EnumeratedEvaluationEvent are published by
     * the `EnumeratedEvaluationSupplier` (and then received by the EnumeratedEvaluationListener.
     */
    @Test
    public void testEventEmitted() {
        // Emit test events.
        for (EvaluatedSearchGraphPath esgp: evaluatedSearchGraphPaths) {
            solutionEventEmitter.emit(esgp);
        }
        // Map to emitted events and received evaluations to its indices.
        List<Integer> emittedIndices = evaluatedSearchGraphPaths
                .stream()
                .map(path->path.getNodes().get(path.getNodes().size()-1).getIndex().toInt())
                .collect(Collectors.toList());
        List<Integer> receivedIndices = enumeratedEvaluationListener.getEvents().stream()
                .map(event->event.getIndex())
                .collect(Collectors.toList());
        // Check equality.
        assertEquals(emittedIndices, receivedIndices);
    }

}
