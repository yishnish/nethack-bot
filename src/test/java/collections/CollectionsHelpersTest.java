package collections;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CollectionsHelpersTest {

    @Test
    public void testCreatingASetOfThings() {
        Set<String> same = new HashSet<String>();
        same.add("hello");
        same.add("world");
        same.add("!");
        Set<String> strings = CollectionsHelpers.setOf("hello", "world", "!");

        assertThat(strings, equalTo(same));
    }

    @Test
    public void testCreatingASetOfOneThing(){
        Set<Integer> same = new HashSet<Integer>();
        same.add(69);
        Set<Integer> set = CollectionsHelpers.setOf(69);

        assertThat(set, equalTo(same));
    }

}