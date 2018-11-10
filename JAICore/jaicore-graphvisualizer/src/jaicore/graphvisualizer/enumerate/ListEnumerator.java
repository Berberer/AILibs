package jaicore.graphvisualizer.enumerate;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListEnumerator implements TreeEnumerator<ListEnumerator.EnumerationList> {

    public static class EnumerationList implements Comparable<EnumerationList> {

        private final ArrayList<Integer> list;

        public EnumerationList(ArrayList<Integer> list) {
            this.list = list;
        }

        @Override
        public int compareTo(EnumerationList o) {
            // Compare itemwise.
            int minLength = o.list.size() < list.size() ? o.list.size() : list.size();
            for(int i=0; i < minLength; i++) {
                int itemCompare = list.get(i).compareTo(o.list.get(i));
                if (itemCompare != 0) return itemCompare;
            }
            // Compare on length.
            int lengthCompare = Integer.compare(list.size(), o.list.size());
            if (lengthCompare != 0) return lengthCompare;
            // Must be equal.
            return 0;
        }

        public int toInt() {
            int sum = 0;
            for(int i=0; i<list.size(); i++) {
                sum += list.get(i) * Math.pow(10, list.size()-i-1);
            }
            return sum;
        }

        @Override
        public String toString() {
            return list.stream()
                    .map(val->String.valueOf(val))
                    .collect(Collectors.joining(","));
        }

        public EnumerationList siblingClone() {
            ArrayList<Integer> listClone = new ArrayList<>();
            list.stream().forEach(ival->listClone.add(new Integer(ival)));
            Integer last = listClone.get(listClone.size()-1);
            last++;
            return new EnumerationList(listClone);
        }

    }

    @Override
    public EnumerationList forRoot(int i) {
        ArrayList<Integer> listForRoot = new ArrayList<>();
        listForRoot.add(i);
        return new EnumerationList(listForRoot);
    }

    @Override
    public EnumerationList forChildren(EnumeratedNode<?, EnumerationList> node, int i) {
        ArrayList<Integer> listCopy = new ArrayList<>(node.getIndex().list);
        listCopy.add(i);
        return new EnumerationList(listCopy);
    }
}
