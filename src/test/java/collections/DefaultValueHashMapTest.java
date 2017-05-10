package collections;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DefaultValueHashMapTest {
    @Test
    public void testReturnsDefaultValueIfNoEntryFound() {
        DefaultValueHashMap<String, Integer> map = new DefaultValueHashMap<String, Integer>(0);
        assertThat(map.get("hi there"), equalTo(0));
    }
}
