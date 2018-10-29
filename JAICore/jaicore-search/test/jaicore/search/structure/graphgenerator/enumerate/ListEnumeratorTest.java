package jaicore.search.structure.graphgenerator.enumerate;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ListEnumeratorTest {

    ListEnumerator listEnumerator;

    @Before
    public void setUp() {
        listEnumerator = new ListEnumerator();
    }

    @Test
    public void testListEnumeratorGeneratesRootCorrectly() {
        ListEnumerator.EnumerationList enumerationList = listEnumerator.forRoot(1);
        // Expectation.
        // ..
    }

    @Test
    public void testEnumerationListToInt() {
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(2, 4, 5, 0, 1, 3));
        ListEnumerator.EnumerationList enumerationList = new ListEnumerator.EnumerationList(testList);
        assertEquals(245013, enumerationList.toInt());
    }

}
