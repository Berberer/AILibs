package jaicore.graphvisualizer.enumerate;


/**
 * Wrapper class for a nodes point. For enuemration purpose.
 * Every nodes point gets an additional value `I index` that represents its place in the enumeration.
 *
 * @param <N> Type of the inner node object.
 * @param <I> Type of the enumeration index.
 */
public class EnumeratedNode<N, I extends EnumerationIndex<I>> {


    private final N point;
    private final I index;

    public EnumeratedNode(N point, I index) {
        this.point = point;
        this.index = index;
    }

    public N getPoint() {
        return point;
    }

    public I getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EnumeratedNode) {
            return point.equals(((EnumeratedNode)obj).point);
        }
        return false;

    }
}
