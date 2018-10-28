package jaicore.search.structure.graphgenerator.enumerate;

/**
 *
 * @param <V>
 * @param <I>
 */
public class EnumeratedEvaluation<V, I extends Comparable<I>> implements Comparable<EnumeratedEvaluation<V,I>> {

    private final V evaluation;
    private final I index;

    public EnumeratedEvaluation(V evaluation, I index) {
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
    public int compareTo(EnumeratedEvaluation<V, I> other) {
        return this.index.compareTo(other.index);
    }
}
