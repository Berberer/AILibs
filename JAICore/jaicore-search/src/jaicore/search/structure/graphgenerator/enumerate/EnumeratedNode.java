package jaicore.search.structure.graphgenerator.enumerate;

import jaicore.search.model.travesaltree.Node;

/**
 * Extends the node class for enumeration purpose.
 * Every node gets an additional value `I index` that represents its place in the enumeration.
 *
 * @param <T> Type of the inner node object.
 * @param <V> Type of the node evaluation.
 * @param <I> Type of the enumeration index.
 */
public class EnumeratedNode <T, V extends Comparable<V>, I extends Comparable<I>> extends Node<T, V> {

    private final I index;

    public EnumeratedNode(EnumeratedNode<T, V, I> parent, T point, I index) {
        super(parent, point);
        this.index = index;
    }

    public I getIndex() {
        return index;
    }
}
