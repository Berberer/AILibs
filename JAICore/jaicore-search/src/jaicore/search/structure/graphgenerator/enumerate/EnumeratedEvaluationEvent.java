package jaicore.search.structure.graphgenerator.enumerate;

/**
 *
 * @param <V>
 * @param <I>
 */
public class EnumeratedEvaluationEvent<V, I extends Comparable<I>> implements Comparable<EnumeratedEvaluationEvent<V,I>> {

    private final V evaluation;
    private final I index;

    public EnumeratedEvaluationEvent(V evaluation, I index) {
        this.evaluation = evaluation;
        this.index = index;
    }

    public V getEvaluation() {
        return evaluation;
    }

    public I getIndex() {
        return index;
    }

    @Override
    public int compareTo(EnumeratedEvaluationEvent<V, I> other) {
        return this.index.compareTo(other.index);
    }

    @Override
    public String toString() {
        return "[EnumeratedEvaluationEvent: (" + evaluation.toString() + ", " + evaluation.toString() + ")]";
    }
}
