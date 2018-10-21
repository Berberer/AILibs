package jaicore.search.structure.graphgenerator.enumerate;

import jaicore.search.model.travesaltree.Node;

/**
 * Wrapper class for a nodes point. For enuemration purpose.
 * Every nodes point gets an additional value `I index` that represents its place in the enumeration.
 *
 * @param <T> Type of the inner node object.
 * @param <I> Type of the enumeration index.
 */
public class EnumeratedNode<T, I extends Comparable<I>> {


    private final T point;
    private final I index;

    public EnumeratedNode(T point, I index) {
        this.point = point;
        this.index = index;
    }

    public T getPoint() {
        return point;
    }

    public I getIndex() {
        return index;
    }
}
