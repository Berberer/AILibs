package jaicore.search.structure.graphgenerator.enumerate;

public interface TreeEnumerator<I extends Comparable<I>> {

    I forRoot(int i);
    I forChildren(EnumeratedNode<?, I> node, int i);

}
