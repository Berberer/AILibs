package jaicore.search.structure.graphgenerator.enumerate;

public class IntegerTreeEnumerator implements TreeEnumerator<Integer> {
    @Override
    public Integer forRoot(int i) {
        return 0;
    }

    @Override
    public Integer forChildren(EnumeratedNode<?, Integer> node, int i) {
        return node.getIndex() + i;
    }
}
